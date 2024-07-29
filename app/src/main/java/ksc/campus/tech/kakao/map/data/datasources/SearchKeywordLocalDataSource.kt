package ksc.campus.tech.kakao.map.data.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ksc.campus.tech.kakao.map.data.apis.SearchKeywordDB
import ksc.campus.tech.kakao.map.data.entities.SearchKeywordEntity
import javax.inject.Inject

class SearchKeywordLocalDataSource @Inject constructor(
    private val roomDB: SearchKeywordDB
) {
    fun insertOrReplaceKeyword(keyword: String) {
        val data = SearchKeywordEntity(0, keyword)
        roomDB.dao().insert(data)
    }

    fun deleteKeyword(keyword: String) {
        roomDB.dao().deleteWhere(keyword)
    }

    fun queryAllSearchKeywords(): Flow<List<String>> {
        val keywords = roomDB.dao().queryAllKeywords()
        return keywords.map { keyword ->
            keyword.map {
                it.keyword ?:""
            }
        }
    }
}

