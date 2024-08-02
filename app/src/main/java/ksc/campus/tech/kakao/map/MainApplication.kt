package ksc.campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKakaoMapSDK()
        getFCMTokenAndPrint()
    }

    private fun startKakaoMapSDK(){
        try {
            KakaoMapSdk.init(this, resources.getString(R.string.KAKAO_API_KEY))
            // Log.d("KSC","Hash Key = ${KakaoMapSdk.INSTANCE.hashKey}")
        } catch (e: Exception) {
            Log.e("KSC", e.message ?: "")
            Log.e("KSC", e.stackTraceToString())
        }
    }

    private fun getFCMTokenAndPrint(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("KSC", "Fetching FCM registration token failed", task.exception)
            }
            else {
                Log.d("KSC", "FCM Token = ${task.result}")
            }
        }
    }
}