package ksc.campus.tech.kakao.map.domain.repositories

import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.domain.models.SearchResult


interface SearchResultRepository {
    val searchResult: SharedFlow<List<SearchResult>>

    suspend fun search(text: String, apiKey: String)
}
