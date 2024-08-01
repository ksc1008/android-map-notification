package ksc.campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            KakaoMapSdk.init(this, resources.getString(R.string.KAKAO_API_KEY))
        } catch (e: Exception) {
            Log.e("KSC", e.message ?: "")
            Log.e("KSC", e.stackTraceToString())
        }
    }
}