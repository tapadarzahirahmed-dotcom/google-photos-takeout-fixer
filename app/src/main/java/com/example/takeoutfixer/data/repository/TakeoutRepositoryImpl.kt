package com.example.takeoutfixer.data.repository

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.takeoutfixer.data.parser.TakeoutJsonParser
import com.example.takeoutfixer.data.utils.ExifToolManager
import com.example.takeoutfixer.data.utils.FFmpegManager
import android.util.Log
import com.example.takeoutfixer.domain.models.FixStatus
import com.example.takeoutfixer.domain.models.FolderNode
import com.example.takeoutfixer.domain.models.ItemType
import com.example.takeoutfixer.domain.models.TakeoutItem
import com.example.takeoutfixer.domain.repository.FixOptions
import com.example.takeoutfixer.domain.repository.FixProgress
import com.example.takeoutfixer.domain.repository.ScanProgress
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.coroutines.flow.channelFlow

@Singleton
class TakeoutRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val jsonParser: TakeoutJsonParser,
    private val exifToolManager: ExifToolManager,
    private val ffmpegManager: FFmpegManager
) : TakeoutRepository {

    private val scannedItems = CopyOnWriteArrayList<TakeoutItem>()
    private var rootFolderNode: FolderNode? = null
    
    // Cache for output directories to avoid expensive findFile calls and race conditions
    private val directoryCache = ConcurrentHashMap<List<String>, DocumentFile>()
    private val directoryLock = Mutex()

    override fun scanFolder(rootUri: Uri): Flow<ScanProgress> = flow {
        Log.e("TakeoutScan", "!!! STARTING OPTIMIZED PARALLEL SCAN !!!")
        scannedItems.clear()
        
        val rootDoc = DocumentFile.fromTreeUri(context, rootUri)
        if (rootDoc == null) {
            emit(ScanProgress(0, "Error: Cannot access folder", 0, isComplete = true))
            return@flow
        }

        val mediaFilesWithPaths = CopyOnWriteArrayList<Pair<DocumentFile, List<String>>>()
        val jsonFilesMap = ConcurrentHashMap<String, DocumentFile>()
        
        emit(ScanProgress(0, "Scanning deep structure...", 0))
        
        coroutineScope {
            val rootChildren = rootDoc.listFiles().map { file ->
                async(Dispatchers.IO) {
                    processNodeWithPaths(file, mediaFilesWithPaths, jsonFilesMap, emptyList())
                }
            }.awaitAll()
            
            rootFolderNode = FolderNode(rootDoc.name ?: "Root", isDirectory = true, children = rootChildren)
        }

        Log.e("TakeoutScan", "SCAN RESULT: Found ${mediaFilesWithPaths.size} media files")

        if (mediaFilesWithPaths.isEmpty()) {
            emit(ScanProgress(0, "No media found. Try a different folder.", 0, isComplete = true))
            return@flow
        }

        var itemsFound = 0
        mediaFilesWithPaths.forEachIndexed { index, (file, path) ->
            val jsonFile = findJsonForFile(file, jsonFilesMap)
            val type = if (isVideoFile(file.name ?: "")) ItemType.VIDEO else ItemType.IMAGE
            
            val item = TakeoutItem(
                id = UUID.randomUUID().toString(),
                name = file.name ?: "",
                fileUri = file.uri,
                jsonUri = jsonFile?.uri,
                type = type,
                size = file.length(),
                jsonSize = jsonFile?.length() ?: 0L,
                albumName = if (path.isNotEmpty()) path.last() else null,
                relativePath = path
            )
            scannedItems.add(item)
            itemsFound++
            
            if (index % 10 == 0 || index == mediaFilesWithPaths.size - 1) {
                emit(ScanProgress(mediaFilesWithPaths.size, file.name ?: "", itemsFound))
            }
        }
        emit(ScanProgress(mediaFilesWithPaths.size, "Complete", itemsFound, isComplete = true))
    }.flowOn(Dispatchers.IO)

    private suspend fun processNodeWithPaths(
        file: DocumentFile,
        mediaList: MutableList<Pair<DocumentFile, List<String>>>,
        jsonMap: MutableMap<String, DocumentFile>,
        currentPath: List<String>
    ): FolderNode = coroutineScope {
        val name = file.name ?: "Unknown"
        if (file.isDirectory) {
            val nextPath = currentPath + name
            val children = file.listFiles().map { child ->
                async(Dispatchers.IO) {
                    processNodeWithPaths(child, mediaList, jsonMap, nextPath)
                }
            }.awaitAll()
            
            FolderNode(name, isDirectory = true, children = children)
        } else {
            val lowerName = name.lowercase()
            val isVideo = isVideoFile(lowerName)
            val isImage = !isVideo && isMediaFile(lowerName)
            val type = if (isVideo) ItemType.VIDEO else if (isImage) ItemType.IMAGE else ItemType.UNKNOWN
            
            if (type != ItemType.UNKNOWN) {
                mediaList.add(file to currentPath)
            } else if (lowerName.endsWith(".json")) {
                jsonMap[name] = file
            }
            FolderNode(name, isDirectory = false, type = type)
        }
    }

    private fun isMediaFile(name: String): Boolean {
        val ext = name.lowercase()
        return ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".png") ||
               ext.endsWith(".heic") || ext.endsWith(".mp4") || ext.endsWith(".mov")
    }

    private fun isVideoFile(name: String): Boolean {
        val ext = name.lowercase()
        return ext.endsWith(".mp4") || ext.endsWith(".mov")
    }

    private fun findJsonForFile(mediaFile: DocumentFile, jsonMap: Map<String, DocumentFile>): DocumentFile? {
        val name = mediaFile.name ?: return null
        val pattern1 = "$name.json"
        jsonMap[pattern1]?.let { return it }
        
        val nameWithoutExtension = name.substringBeforeLast('.')
        val pattern2 = "$nameWithoutExtension.json"
        jsonMap[pattern2]?.let { return it }

        jsonMap.keys.find { it.equals(pattern1, ignoreCase = true) }?.let { return jsonMap[it] }
        
        jsonMap.keys.find { it.startsWith(nameWithoutExtension) && it.endsWith(".json") }?.let {
            return jsonMap[it]
        }
        return null
    }

    override suspend fun getItems(): List<TakeoutItem> = scannedItems

    override suspend fun getFolderStructure(): FolderNode? = rootFolderNode

    override suspend fun applyFixes(items: List<TakeoutItem>, options: FixOptions): Flow<FixProgress> = channelFlow {
        val rootOutputDir = options.outputFolderUri?.let { uri ->
            DocumentFile.fromTreeUri(context, uri)
        }
        
        // Clear cache for a new run
        directoryCache.clear()

        val total = items.size
        val processedCount = AtomicInteger(0)
        val semaphore = Semaphore(4)

        coroutineScope {
            items.forEach { item ->
                launch(Dispatchers.IO) {
                    semaphore.withPermit {
                        try {
                            processSingleFixInternal(item, options, rootOutputDir)
                        } catch (e: Exception) {
                            Log.e("TakeoutRepository", "Error processing ${item.name}", e)
                        }
                        val current = processedCount.incrementAndGet()
                        send(FixProgress(total, current, item.name))
                    }
                }
            }
        }
        send(FixProgress(total, total, "Finished", isComplete = true))
    }.flowOn(Dispatchers.IO)

    private suspend fun processSingleFixInternal(item: TakeoutItem, options: FixOptions, rootOutputDir: DocumentFile?) {
        if (item.jsonUri == null) {
            Log.d("TakeoutRepository", "Skipping ${item.name} - No JSON metadata")
            return
        }
        
        val metadata = jsonParser.parse(item.jsonUri)
        val timestamp = metadata?.photoTakenTime ?: metadata?.creationTime
        
        if (timestamp != null && !options.dryRun) {
            val targetUri: Uri? = if (rootOutputDir != null) {
                val targetDir = ensureDirectoryExists(rootOutputDir, item.relativePath)
                if (targetDir == null) {
                    Log.e("TakeoutRepository", "Failed to create directory for ${item.relativePath}")
                    return
                }

                // Get actual MIME type or fallback to extension-based guess
                val mimeType = context.contentResolver.getType(item.fileUri) 
                    ?: if (item.type == ItemType.VIDEO) "video/mp4" else "image/jpeg"

                val newFile = targetDir.createFile(mimeType, item.name)
                
                if (newFile != null) {
                    context.contentResolver.openInputStream(item.fileUri)?.use { input ->
                        context.contentResolver.openOutputStream(newFile.uri)?.use { output ->
                            input.copyTo(output)
                        }
                    }
                    newFile.uri
                } else {
                    Log.e("TakeoutRepository", "Failed to create file: ${item.name} in ${targetDir.name}")
                    null
                }
            } else {
                // Warning: editing in-place might be restricted on modern Android without permission
                item.fileUri
            }

            if (targetUri == null) return

            if (item.type == ItemType.IMAGE) {
                exifToolManager.updateMetadata(targetUri, timestamp, metadata?.description ?: metadata?.title)
                val geo = metadata?.geoDataExif ?: metadata?.geoData
                geo?.let { g ->
                    exifToolManager.updateLocation(targetUri, g.latitude, g.longitude, g.altitude)
                }
            } else if (item.type == ItemType.VIDEO) {
                ffmpegManager.updateVideoMetadata(targetUri, timestamp, metadata?.description ?: metadata?.title)
            }
        } else if (timestamp == null) {
            Log.w("TakeoutRepository", "No timestamp found for ${item.name}")
        }
    }

    private suspend fun ensureDirectoryExists(root: DocumentFile, path: List<String>): DocumentFile? {
        if (path.isEmpty()) return root
        
        // Check cache first (fast path)
        directoryCache[path]?.let { return it }

        // Synchronize to prevent duplicate directory creation (slow path)
        return directoryLock.withLock {
            // Re-check cache after acquiring lock
            directoryCache[path]?.let { return@withLock it }

            var current = root
            val currentPath = mutableListOf<String>()
            
            for (folderName in path) {
                currentPath.add(folderName)
                val cached = directoryCache[currentPath.toList()]
                if (cached != null) {
                    current = cached
                    continue
                }

                val existing = current.findFile(folderName)
                if (existing != null && existing.isDirectory) {
                    current = existing
                } else if (existing != null) {
                    // File with same name exists, create directory with unique name
                    current = current.createDirectory("${folderName}_folder") ?: return@withLock null
                } else {
                    current = current.createDirectory(folderName) ?: return@withLock null
                }
                directoryCache[currentPath.toList()] = current
            }
            current
        }
    }

    override suspend fun exportLog(uri: Uri, log: String) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { 
            it.write(log.toByteArray())
        } ?: Unit
    }
}
