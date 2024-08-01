package ksc.campus.tech.kakao.map.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

@HiltViewModel
class SearchActivityViewModel @Inject constructor(
    private val mapViewRepository: MapViewRepository,
    private val searchResultRepository: SearchResultRepository,
    private val keywordRepository: SearchKeywordRepository
) : ViewModel() {

    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _activeContent: MutableLiveData<ContentType> = MutableLiveData(ContentType.MAP)

    val keywords = keywordRepository.keywords.stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT),
    )

    val selectedLocation = mapViewRepository.selectedLocation.stateIn(
        scope = viewModelScope,
        initialValue = null,
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT),
    )

    val searchText: LiveData<String>
        get() = _searchText
    val activeContent: LiveData<ContentType>
        get() = _activeContent

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mapViewRepository.loadFromSharedPreference()
        }
    }

    private fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchResultRepository.search(query, BuildConfig.KAKAO_REST_API_KEY)
        }
        switchContent(ContentType.SEARCH_LIST)
    }


    private fun deleteKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            keywordRepository.deleteKeyword(keyword)
        }
    }
    private fun addKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            keywordRepository.addKeyword(keyword)
        }
    }
    private fun updateLocation(item: LocationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mapViewRepository.updateSelectedLocation(
                item
            )
            mapViewRepository.updateCameraPositionWithFixedZoom(item.latitude, item.longitude)
        }
    }

    fun clickSearchResultItem(selectedItem: SearchResult) {
        addKeyword(selectedItem.name)
        Log.d("KSC", "lat: ${selectedItem.latitude}, lon: ${selectedItem.longitude}")
        updateLocation(
            LocationInfo(selectedItem.address,
                selectedItem.name,
                selectedItem.latitude,
                selectedItem.longitude)
        )
        switchContent(ContentType.MAP)
    }
    fun submitQuery(value: String) {
        search(value)
    }

    fun clickKeywordDeleteButton(keyword: String) {
        deleteKeyword(keyword)
    }

    fun clickKeyword(keyword: String) {
        _searchText.postValue(keyword)
        search(keyword)
    }

    fun switchContent(type: ContentType) {
        _activeContent.postValue(type)
    }

    enum class ContentType { MAP, SEARCH_LIST }

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}