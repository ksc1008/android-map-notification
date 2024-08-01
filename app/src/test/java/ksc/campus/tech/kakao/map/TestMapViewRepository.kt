package ksc.campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.runs
import io.mockk.verify
import ksc.campus.tech.kakao.map.data.datasources.MapPreferenceLocalDataSource
import ksc.campus.tech.kakao.map.data.repositoryimpls.MapViewRepositoryImpl
import org.junit.Before
import org.junit.Test

class TestMapViewRepository {
    lateinit var mockContext:Context
    lateinit var mockSharedPreferences:SharedPreferences
    lateinit var mockEditor:SharedPreferences.Editor

    @Before
    fun setupMockedSharedPreference(){
        mockContext = mockkClass(Context::class)
        mockSharedPreferences = mockkClass(SharedPreferences::class)
        mockEditor = mockkClass(SharedPreferences.Editor::class)

        every {
            mockContext.getSharedPreferences(any(), any())
        } returns mockSharedPreferences

        every {
            mockSharedPreferences.edit()
        } returns mockEditor

        every {
            mockSharedPreferences.registerOnSharedPreferenceChangeListener(any())
            mockEditor.apply()
        } just runs

        every {
            mockEditor.putString(any(), any())
        } returns mockEditor

        every{
            mockSharedPreferences.getString(any(), any())
        } returns ""
    }

    @Test
    fun `레포지토리를 생성하면 SharedPreference의 onChanged 리스너가 등록된다`(){
        // given
        val dataSource = MapPreferenceLocalDataSource()

        // when
        val repository = MapViewRepositoryImpl(
            dataSource,
            mockContext
        )

        // then
        verify {
            mockSharedPreferences.registerOnSharedPreferenceChangeListener(any())
        }
    }
}