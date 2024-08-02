package ksc.campus.tech.kakao.map.data.datasources

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import ksc.campus.tech.kakao.map.data.entities.RemoteConfigData
import java.lang.Exception
import javax.inject.Inject

class FirebaseMapConfigRemoteDataSource @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {
    init {
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_TEST
            }
        )
    }

    suspend fun fetchAppState(): RemoteConfigData? {
        return try {
            withTimeout(10000) {
                remoteConfig.fetchAndActivate().await()
            }
            RemoteConfigData(remoteConfig[FIREBASE_APP_STATE_KEY].asString(),
                remoteConfig[FIREBASE_APP_MESSAGE_KEY].asString())

        } catch (e: Exception) {
            Log.e("KSC", e.message?:"")
            null
        }
    }

    companion object {
        const val REMOTE_CONFIG_FETCH_INTERVAL_TEST = 0L
        const val REMOTE_CONFIG_FETCH_INTERVAL_PRODUCT = 1000L
        const val FIREBASE_APP_STATE_KEY = "serviceState"
        const val FIREBASE_APP_MESSAGE_KEY = "serviceMessage"
    }
}