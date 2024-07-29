package ksc.campus.tech.kakao.map.domain.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.data.repositoryimpls.MapViewRepositoryImpl
import ksc.campus.tech.kakao.map.data.repositoryimpls.SearchKeywordRepositoryImpl
import ksc.campus.tech.kakao.map.data.repositoryimpls.SearchResultRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapViewRepositoryImpl
    ): MapViewRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchKeywordRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideSearchKeywordRepository(
        searchKeywordRepositoryImpl: SearchKeywordRepositoryImpl
    ): SearchKeywordRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchResultRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchResultRepository(
        searchResultRepositoryImpl: SearchResultRepositoryImpl
    ): SearchResultRepository
}