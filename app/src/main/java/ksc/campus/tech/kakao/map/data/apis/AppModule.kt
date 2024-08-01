package ksc.campus.tech.kakao.map.data.apis

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoApiRetrofit

@Module
@InstallIn(SingletonComponent::class)
object SearchKeywordDBModule {
    @Provides
    fun provideSearchKeywordDB(
        @ApplicationContext context: Context
    ): SearchKeywordDB {
        return Room.databaseBuilder(context, SearchKeywordDB::class.java, "MapSearch2")
            .fallbackToDestructiveMigration().build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SearchKakaoRetrofitServiceModule {

    @Provides
    fun provideSearchKakaoRetrofitService(): KakaoSearchRetrofitService {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoSearchRetrofitService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseRemoteConfigModule {
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig
    }
}