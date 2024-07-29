package ksc.campus.tech.kakao.map.fake_module

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ksc.campus.tech.kakao.map.domain.repositories.MapRepositoryModule
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepositoryModule
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepositoryModule
import ksc.campus.tech.kakao.map.repositoriesImpl.FakeMapViewRepository
import ksc.campus.tech.kakao.map.repositoriesImpl.FakeSearchKeywordRepository
import ksc.campus.tech.kakao.map.repositoriesImpl.FakeSearchResultRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MapRepositoryModule::class])
abstract class FakeMapRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: FakeMapViewRepository
    ): MapViewRepository
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchKeywordRepositoryModule::class])
abstract class FakeSearchKeywordRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideSearchKeywordRepository(
        searchKeywordRepositoryImpl: FakeSearchKeywordRepository
    ): SearchKeywordRepository
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchResultRepositoryModule::class])
abstract class FakeSearchResultRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchResultRepository(
        searchResultRepositoryImpl: FakeSearchResultRepository
    ): SearchResultRepository
}