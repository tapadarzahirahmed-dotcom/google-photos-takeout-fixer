package com.example.takeoutfixer.domain.repository

import android.net.Uri
import com.example.takeoutfixer.domain.models.FolderNode
import com.example.takeoutfixer.domain.models.TakeoutItem
import kotlinx.coroutines.flow.Flow

interface TakeoutRepository {
    fun scanFolder(rootUri: Uri): Flow<ScanProgress>
    suspend fun getItems(): List<TakeoutItem>
    suspend fun getFolderStructure(): FolderNode?
    suspend fun applyFixes(items: List<TakeoutItem>, options: FixOptions): Flow<FixProgress>
    suspend fun exportLog(uri: Uri, log: String)
}

data class ScanProgress(
    val totalFiles: Int,
    val currentFile: String,
    val itemsFound: Int,
    val isComplete: Boolean = false
)

data class FixProgress(
    val totalItems: Int,
    val processedItems: Int,
    val currentFile: String,
    val isComplete: Boolean = false
)

data class FixOptions(
    val dryRun: Boolean = false,
    val backupOriginal: Boolean = true,
    val overwrite: Boolean = false,
    val reconstructAlbums: Boolean = true,
    val outputFolderUri: Uri? = null
)
