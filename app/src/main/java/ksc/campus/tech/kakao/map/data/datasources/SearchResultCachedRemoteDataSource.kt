package ksc.campus.tech.kakao.map.data.datasources

import ksc.campus.tech.kakao.map.data.entities.Document
import javax.inject.Inject

class SearchResultCachedRemoteDataSource @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultRemoteDataSource
) {
    private var _cachedQuery: String = ""
    private var _cache: List<Document> = listOf()

    suspend fun getSearchResult(query: String, apiKey: String, batchCount: Int): List<Document> {
        if (query != _cachedQuery) {
            // 서버로부터 데이터를 불러오고 해당 값을 캐시에 저장
            _cache = searchResultRemoteDataSource.getSearchResult(
                query,
                apiKey,
                batchCount
            )
            _cachedQuery = query
        }

        // (갱신된) 캐시 값을 반환
        return _cache
    }
}