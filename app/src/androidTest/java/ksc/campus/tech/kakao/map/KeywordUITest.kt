package ksc.campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.presentation.views.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class KeywordUITest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeKeywordRepository 사용
     *
     * keywords 기본값으로 "1", "2", "hello", "world" 포함
     * addKeyword(), deleteKeyword() 호출 시 별도의 db 작업 없이 즉시 데이터에 반영
     */


    @get:Rule
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var keywordRepository: SearchKeywordRepository

    @Before
    fun setup(){
        hiltRule.inject()
    }


    @Test
    fun keywordAddOnMethodCalled(){

        // given
        val checkingKeyword = "AAFFCC"
        checkNoTextExists(checkingKeyword)

        // when
        activityScenarioRule.scenario.onActivity {

            runBlocking {
                keywordRepository.addKeyword(checkingKeyword)
            }
        }

        Thread.sleep(100)
        // then
        checkTextExists(checkingKeyword)
    }

    @Test
    fun keywordRemovedOnMethodCalled() {

        // given
        val checkingKeyword = "hello"
        checkTextExists(checkingKeyword)

        // when
        activityScenarioRule.scenario.onActivity {

            runBlocking {
                keywordRepository.deleteKeyword(checkingKeyword)
            }
        }

        // wait until animation of ListAdapter finished
        Thread.sleep(100)

        // then
        checkNoTextExists(checkingKeyword)
    }

    private fun checkNoTextExists(text:String){
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText(text), isDisplayed()))
            .check((doesNotExist()))
    }

    private fun checkTextExists(text:String){
        onView(withId(R.id.saved_search_bar))
            .check(
                matches(
                    hasDescendant(
                        withText(text)
                    )
                )
            )
    }
}