package ksc.campus.tech.kakao.map

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.data.datasources.SearchKeywordLocalDataSource
import ksc.campus.tech.kakao.map.data.repositoryimpls.SearchKeywordRepositoryImpl
import org.junit.Before
import org.junit.Test

class TestSearchKeywordRepository {
    lateinit var mockDataSource:SearchKeywordLocalDataSource
    lateinit var repository: SearchKeywordRepositoryImpl

    val dummyDataInsideDataSource = listOf("Test Keyword")
    @Before
    fun setupMockedDataSource(){
        mockDataSource = mockk<SearchKeywordLocalDataSource>()

        every {
            mockDataSource.insertOrReplaceKeyword(any())
            mockDataSource.deleteKeyword(any())
        } just runs

        every{
            mockDataSource.queryAllSearchKeywords()
        } returns flow{
            emit(dummyDataInsideDataSource)
        }

        repository = SearchKeywordRepositoryImpl(mockDataSource)
    }

    @Test
    fun `키워드를 추가하면 데이터소스의 데이터 삽입 메소드를 호출한다`(){
        // given
        val keyword = "Keyword"

        // when
        runBlocking {
            repository.addKeyword(keyword)
        }

        // then
        verify {
            mockDataSource.insertOrReplaceKeyword(keyword)
        }
    }
    @Test
    fun `키워드를 삭제하면 데이터소스의 데이터 제거 메소드를 호출한다`(){
        // given
        val keyword = "Keyword"

        // when
        runBlocking {
            repository.deleteKeyword(keyword)
        }

        // then
        verify {
            mockDataSource.deleteKeyword(keyword)
        }
    }

    @Test
    fun `키워드를 요청하면 데이터소스의 키워드 목록을 emit한다`(){
        // when
        var collected: List<String> = listOf()

        runBlocking {
            repository.keywords.collect{
                collected = it
            }
        }

        //then
        assertEquals(collected, dummyDataInsideDataSource)
    }
}