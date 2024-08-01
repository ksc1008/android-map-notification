package ksc.campus.tech.kakao.map.repositoriesImpl

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class FakeSearchResultRepository @Inject constructor(): SearchResultRepository {
    private var _searchValue = listOf<SearchResult>()
    private val _searchResult = MutableSharedFlow<List<SearchResult>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val searchResult
        get() = _searchResult
    private fun getDummyData(prefix:String):List<SearchResult>{
        val result = mutableListOf<SearchResult>()
        for(i in 0..15) {
            result.add(
                SearchResult(
                    i.toString(),
                    "name $prefix $i",
                    "address $prefix $i",
                    "type $i",
                    0.0,
                    0.0
                )
            )
        }

        return result
    }



    override suspend fun search(text: String, apiKey: String){
        _searchValue = getDummyData(text)
        _searchResult.emit(_searchValue)
    }
}