package ksc.campus.tech.kakao.map.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SearchKeywordRepository {
    val keywords: Flow<List<String>>
    suspend fun addKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun getKeywords()
}
