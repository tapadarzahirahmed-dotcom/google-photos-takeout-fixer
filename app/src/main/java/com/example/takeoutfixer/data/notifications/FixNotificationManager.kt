package com.example.takeoutfixer.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FixNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "fix_progress_channel"
    private val notificationId = 1001

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fixing Progress",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows the progress of photo and video fixes"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showProgress(current: Int, total: Int, fileName: String) {
        val progress = if (total > 0) (current * 100 / total) else 0
        
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("Fixing Takeout Files")
            .setContentText("Processing: $fileName ($current/$total)")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setProgress(100, progress, false)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showComplete(total: Int) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Fixing Complete")
            .setContentText("Successfully processed $total files.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(false)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun cancel() {
        notificationManager.cancel(notificationId)
    }
}
