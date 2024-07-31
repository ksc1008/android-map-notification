package ksc.campus.tech.kakao.map.presentation.views.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.presentation.views.ForegroundMessaging
import ksc.campus.tech.kakao.map.presentation.views.MainActivity

class MapFirebaseMessagingService:FirebaseMessagingService() {
    val foregroundMessaging: ForegroundMessaging by lazy{
        ForegroundMessaging(this)
    }
    override fun onCreate() {
        super.onCreate()
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        try {
            foregroundMessaging.notificationManager.notify(
                ForegroundMessaging.NOTIFICATION_ID,
                foregroundMessaging.notificationBuilder.setContentText(message.notification?.body)
                    .build()
            )
        }
        catch (e:Exception){
        }
    }
}