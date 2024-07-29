package ksc.campus.tech.kakao.map

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.MainActivity
import ksc.campus.tech.kakao.map.presentation.views.fragments.KakaoMapFragment
import ksc.campus.tech.kakao.map.presentation.views.fragments.SearchResultFragment
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class MainActivityTest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeKeywordRepository 사용
     * keywords 기본값으로 "1", "2", "hello", "world" 포함
     * addKeyword(), deleteKeyword() 호출 시 별도의 db 작업 없이 즉시 데이터에 반영
     */

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var mapViewRepository: MapViewRepository

    @Before
    fun setup(){
        hiltRule.inject()
    }
    private fun checkTextExists(text:String){
        onView(withId(R.id.list_search_result))
            .check(
                matches(
                    ViewMatchers.hasDescendant(
                        withText(text)
                    )
                )
            )
    }

    private fun checkContainerHasKakaoMapFragment(){
        activityRule.scenario.onActivity {
            assertEquals(checkHasFragmentOfType<KakaoMapFragment>(it.supportFragmentManager.fragments), true)

            assertEquals(checkHasFragmentOfType<SearchResultFragment>(it.supportFragmentManager.fragments), false)
        }
    }

    private fun checkContainerHasSearchResultFragment(){
        activityRule.scenario.onActivity {
            assertEquals(checkHasFragmentOfType<KakaoMapFragment>(it.supportFragmentManager.fragments), false)

            assertEquals(checkHasFragmentOfType<SearchResultFragment>(it.supportFragmentManager.fragments), true)
        }
    }

    @Test
    fun mapViewShownOnStart(){
        checkContainerHasKakaoMapFragment()
    }

    @Test
    fun searchListShownOnSearchBarClick(){

        // given
        checkContainerHasKakaoMapFragment()

        // when
        onView(withId(R.id.input_search))
            .perform(click())

        // then
        checkContainerHasSearchResultFragment()
    }

    @Test
    fun searchListShownOnKeywordClick(){

        // given
        checkContainerHasKakaoMapFragment()

        // when
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText("1")))
            .perform(click())

        // then
        checkContainerHasSearchResultFragment()
    }

    @Test
    fun searchAsKeywordOnKeywordClick(){

        // given
        val keywordToClick = "world"
        checkContainerHasKakaoMapFragment()

        // when
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText(keywordToClick)))
            .perform(click())

        Thread.sleep(UI_DELAY_SHORT)

        // then
        checkTextExists("name $keywordToClick 0")
    }

    @Test
    fun navigateBackToMapViewOnBackButtonPressed(){

        // given
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText("1")))
            .perform(click())
        checkContainerHasSearchResultFragment()

        // when
        onView(isRoot())
            .perform(ViewActions.pressBackUnconditionally())

        // then
        checkContainerHasKakaoMapFragment()
    }

    @Test
    fun navigateToMapViewOnListItemClicked(){

        // given
        val keywordToClick = "1"
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText(keywordToClick)))
            .perform(click())
        checkContainerHasSearchResultFragment()

        Thread.sleep(UI_DELAY_SHORT)

        // when
        onView(withText("name $keywordToClick 0"))
            .perform(click())

        // then
        checkContainerHasKakaoMapFragment()

    }

    @Test
    fun bottomSheetShownOnMapView(){
        onView(withId(R.id.text_location_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun bottomSheetShownOnSearchView(){
        activityRule.scenario.onActivity {
            it.searchViewModel.switchContent(SearchActivityViewModel.ContentType.SEARCH_LIST)
        }

        onView(withId(R.id.text_location_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun nameAndAddressDisplayedWhenLocationSelected(){

        // given
        val location = LocationInfo("Loc Addr", "Loc Name", 12.12, 12.56)

        // when
        activityRule.scenario.onActivity {
            runBlocking {
                mapViewRepository.updateSelectedLocation(location)
            }
        }

        Thread.sleep(UI_DELAY_SHORT)

        // then
        onView(withId(R.id.text_location_name))
            .check(matches(withText(location.name)))
        onView(withId(R.id.text_location_address))
            .check(matches(withText(location.address)))
    }

    private inline fun <reified F:Fragment> checkHasFragmentOfType(list:List<Fragment>):Boolean{
        return list.find {
            it::class.java == F::class.java
        } != null
    }

    companion object {
        const val UI_DELAY_SHORT = 100L
    }
}