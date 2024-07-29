package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.data.datasources.SearchResultCachedRemoteDataSource
import ksc.campus.tech.kakao.map.data.mapper.KakaoSearchDtoMapper
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultCachedRemoteDataSource
): SearchResultRepository {
    private val _searchResult = MutableSharedFlow<List<SearchResult>>()

    override val searchResult: SharedFlow<List<SearchResult>>
        get() = _searchResult

    override suspend fun search(text: String, apiKey: String){
        _searchResult.emit(listOf())

        val result = searchResultRemoteDataSource.getSearchResult(text, apiKey, BATCH_COUNT).map {
            KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
        }
        _searchResult.emit(result)
    }

    companion object{
        const val BATCH_COUNT = 5
    }
}