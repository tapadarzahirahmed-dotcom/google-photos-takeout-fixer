package com.example.takeoutfixer.data.utils

import android.content.Context
import android.net.Uri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class FFmpegManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val videoFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    suspend fun updateVideoMetadata(fileUri: Uri, timestampSeconds: Long, description: String? = null): Boolean = withContext(Dispatchers.IO) {
        val tempDir = File(context.cacheDir, "ffmpeg_temp")
        if (!tempDir.exists()) tempDir.mkdirs()

        val inputFile = File(tempDir, "input_${System.currentTimeMillis()}.mp4")
        val outputFile = File(tempDir, "output_${System.currentTimeMillis()}.mp4")

        try {
            context.contentResolver.openInputStream(fileUri)?.use { input ->
                inputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return@withContext false

            val dateString = videoFormatter.format(Date(timestampSeconds * 1000))
            
            // Build FFmpeg command with creation_time and description
            val metadataArgs = mutableListOf<String>()
            metadataArgs.add("-metadata creation_time=\"$dateString\"")
            if (!description.isNullOrBlank()) {
                metadataArgs.add("-metadata description=\"$description\"")
                metadataArgs.add("-metadata title=\"$description\"")
                metadataArgs.add("-metadata comment=\"$description\"")
            }
            
            // Step 2: Run FFmpeg to update metadata
            // Use -map_metadata 0 to ensure existing streams are preserved while overriding specific tags
            val command = "-i ${inputFile.absolutePath} -map_metadata 0 ${metadataArgs.joinToString(" ")} -codec copy ${outputFile.absolutePath}"
            
            val session = FFmpegKit.execute(command)
            if (ReturnCode.isSuccess(session.returnCode)) {
                // Step 3: Copy fixed file back to original URI
                context.contentResolver.openOutputStream(fileUri, "wt")?.use { output ->
                    outputFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            inputFile.delete()
            outputFile.delete()
        }
    }
}
