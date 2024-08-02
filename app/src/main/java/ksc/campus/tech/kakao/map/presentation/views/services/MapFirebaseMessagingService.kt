package ksc.campus.tech.kakao.map.presentation.views.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFirebaseMessagingService:FirebaseMessagingService() {
    private val foregroundMessaging: ForegroundMessaging by lazy{
        ForegroundMessaging(this)
    }

    private fun printToken(token: String) {
        Log.d("KSC", "FCM Token = $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        try {
            message.notification?.body?.let {
                foregroundMessaging.notifyMessage(it)
            }
        }
        catch (e:Exception){
            Log.d("KSC", e.message?:"")
        }
    }

    override fun onNewToken(token: String) {
        printToken(token)
        super.onNewToken(token)
    }
}