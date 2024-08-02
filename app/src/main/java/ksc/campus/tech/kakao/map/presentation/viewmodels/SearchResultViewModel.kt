package ksc.campus.tech.kakao.map.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    searchResultRepository: SearchResultRepository): ViewModel(){
    init {
        Log.d("KSC", "viewmodel init")
    }

    val searchResult: StateFlow<List<SearchResult>> = searchResultRepository.searchResult.stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )

    override fun onCleared() {
        Log.d("KSC", "viewmodel canceled")
        super.onCleared()
    }

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}