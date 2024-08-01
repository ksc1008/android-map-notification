package ksc.campus.tech.kakao.map.data.apis

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMapConfig @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {

}