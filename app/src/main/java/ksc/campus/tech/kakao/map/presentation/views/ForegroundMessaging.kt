package ksc.campus.tech.kakao.map.presentation.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ksc.campus.tech.kakao.map.R

class ForegroundMessaging(val context: Context) {

    var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationBuilder:NotificationCompat.Builder


    init {
        val intent = Intent(
            context, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationBuilder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.resources.getString(R.string.foreground_notification_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.resources.getString(R.string.foreground_notification_bigText))
            )
            .setAutoCancel(true)
    }
    fun createNotificationChannel(context: Context) {
        val descriptionText = context.resources.getString(R.string.foreground_notificationChannel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }
}