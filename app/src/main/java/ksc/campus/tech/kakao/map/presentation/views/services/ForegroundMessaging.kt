package ksc.campus.tech.kakao.map.presentation.views.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.presentation.views.MainActivity

class ForegroundMessaging(context: Context) {

    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder:NotificationCompat.Builder


    init {
        val pendingIntent = createPendingIntent(context)
        notificationBuilder = createNotificationBuilder(context, pendingIntent)
        createNotificationChannel(context)
    }

    private fun createNotificationChannel(context: Context){
        val name = context.getString(R.string.foreground_notification_title)
        val descriptionText = context.getString(R.string.foreground_notificationChannel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun createPendingIntent(context: Context): PendingIntent{
        val intent = Intent(
            context, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationBuilder(context:Context, pendingIntent:PendingIntent): NotificationCompat.Builder{
        return NotificationCompat.Builder(
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

    fun notifyMessage(message: String){
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(message)
                .build()
        )
    }

    companion object {
        const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
    }
}