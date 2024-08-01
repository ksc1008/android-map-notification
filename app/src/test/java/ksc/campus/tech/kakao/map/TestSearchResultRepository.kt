package ksc.campus.tech.kakao.map

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.data.datasources.SearchResultCachedRemoteDataSource
import ksc.campus.tech.kakao.map.data.entities.Document
import ksc.campus.tech.kakao.map.data.mapper.KakaoSearchDtoMapper
import ksc.campus.tech.kakao.map.data.repositoryimpls.SearchResultRepositoryImpl
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import org.junit.Before
import org.junit.Test

class TestSearchResultRepository {
    lateinit var mockCachedRemoteDataSource:SearchResultCachedRemoteDataSource
    private fun makeDummyResponseDocument(query: String):List<Document> = listOf(
        Document("$query id", "$query name", "$query category", "0", "0",
            "1", "$query address", "1", "1", "1", "1", "1"))

    @Before
    fun setupMockedDataSource() {
        mockCachedRemoteDataSource = mockk<SearchResultCachedRemoteDataSource>()
    }

    @Test
    fun `레포지토리에 검색 메소드를 호출하면 searchResult에 SearchResult로 매핑된 response가 emit된다`(){
        // given
        val query = "Query 0"
        val expectedResponse = makeDummyResponseDocument(query).map {
            KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
        }

        coEvery {
            mockCachedRemoteDataSource.getSearchResult(any(), any(), any())
        } returns makeDummyResponseDocument(query)

        val repository = SearchResultRepositoryImpl(mockCachedRemoteDataSource)
        var collectedData:List<SearchResult>? = null


        //when
        runBlocking {
            val job = launch{
                repository.searchResult.collectLatest{
                    collectedData = it
                }
            }
            repository.search(query, "")
            delay(100)
            job.cancel()
        }

        //then
        runBlocking {
            assertEquals(collectedData, expectedResponse)
        }
    }
}