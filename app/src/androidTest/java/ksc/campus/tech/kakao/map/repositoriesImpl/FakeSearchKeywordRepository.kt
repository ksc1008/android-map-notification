package ksc.campus.tech.kakao.map.repositoriesImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import javax.inject.Inject


class FakeSearchKeywordRepository @Inject constructor(): SearchKeywordRepository {
    private val dummyData:List<String> = listOf(
        "1", "2", "hello", "world"
    )

    private val _keywords: MutableStateFlow<List<String>> = MutableStateFlow(dummyData)
    override val keywords: Flow<List<String>>
        get() = _keywords

    override suspend fun addKeyword(keyword: String) {
        Log.d("KSC", "adding $keyword")
        _keywords.emit(addElementWithoutDuplicates(_keywords.value, keyword))
    }

    private fun <T>removeElement(list:List<T>, elem:T): List<T>{
        val index = list.indexOf(elem)
        if(index == -1) {
            return list
        }
        return list.subList(0, index) + list.subList(index, list.size).drop(1)
    }

    private fun <T>addElementWithoutDuplicates(list:List<T>, elem:T):List<T>{
        var result = list
        if(list.contains(elem)){
            result = removeElement(list, elem)
        }
        return (result + listOf(elem))
    }

    override suspend fun deleteKeyword(keyword: String) {
        _keywords.emit(removeElement(_keywords.value, keyword))
    }

    override suspend fun getKeywords() {
        _keywords.emit(dummyData)
    }

}