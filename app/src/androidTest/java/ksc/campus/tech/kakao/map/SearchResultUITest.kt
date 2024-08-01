package ksc.campus.tech.kakao.map

import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchResultUITest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeSearchResultRepository 사용
     *
     * 검색 결과값은 검색어에 따라 결정
     * X를 검색 시, 검색 결과의 이름은 각 각 ["name X 0", "name X 1", ... , "name X 15"]
     * X를 검색 시, 검색 결과의 주소는 각 각 ["address X 0", "address X 1", ... , "address X 15"]
     */

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun setup(){
        hiltRule.inject()

        ViewModelProvider.AndroidViewModelFactory
        activityRule.scenario.onActivity {
            it.searchViewModel.switchContent(SearchActivityViewModel.ContentType.SEARCH_LIST)
        }
    }

    private fun checkTextExists(text:String){
        Espresso.onView(ViewMatchers.withId(R.id.list_search_result))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.hasDescendant(
                        ViewMatchers.withText(text)
                    )
                )
            )
    }

    @Test
    fun searchResultAppearOnListViewOnSearch(){
        // given
        val query = "hello"

        // when
        activityRule.scenario.onActivity {
            it.searchViewModel.submitQuery(query)
        }

        Thread.sleep(100)

        // then
        for(i in 0..5) {
            checkTextExists("name $query $i")
            checkTextExists("address $query $i")
        }
    }

    @Test
    fun searchResultContainsAddressAndType(){
        // given
        val query = "Test Input"
        val expectedSearchResults = arrayOf("name Test Input 0", "address Test Input 0", "type 0")

        // when
        activityRule.scenario.onActivity {
            it.searchViewModel.submitQuery(query)
        }

        Thread.sleep(100)

        // then
        for(expect in expectedSearchResults){
            checkTextExists(expect)
        }
    }
}