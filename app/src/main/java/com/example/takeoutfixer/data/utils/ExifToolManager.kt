package com.example.takeoutfixer.data.utils

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ExifToolManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val exifFormatter = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    suspend fun updateMetadata(fileUri: Uri, timestampSeconds: Long, description: String? = null): Boolean = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openFileDescriptor(fileUri, "rw")?.use { pfd ->
                val exif = ExifInterface(pfd.fileDescriptor)
                val dateString = exifFormatter.format(Date(timestampSeconds * 1000))
                
                exif.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, dateString)
                exif.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, dateString)
                exif.setAttribute(ExifInterface.TAG_DATETIME, dateString)
                
                if (!description.isNullOrBlank()) {
                    exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, description)
                    exif.setAttribute(ExifInterface.TAG_USER_COMMENT, description)
                }
                
                exif.saveAttributes()
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateLocation(fileUri: Uri, lat: Double, lon: Double, alt: Double): Boolean = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openFileDescriptor(fileUri, "rw")?.use { pfd ->
                val exif = ExifInterface(pfd.fileDescriptor)
                
                val latRef = if (lat >= 0) "N" else "S"
                val lonRef = if (lon >= 0) "E" else "W"
                val altRef = if (alt >= 0) 0 else 1 // 0 = above sea level, 1 = below
                
                exif.setLatLong(lat, lon)
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latRef)
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lonRef)
                exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, Math.abs(alt).toString())
                exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, altRef.toString())
                
                exif.saveAttributes()
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
