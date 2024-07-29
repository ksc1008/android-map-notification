package ksc.campus.tech.kakao.map.presentation.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.databinding.ActivityMainBinding
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordAdapter
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordClickCallback
import ksc.campus.tech.kakao.map.presentation.views.fragments.KakaoMapFragment
import ksc.campus.tech.kakao.map.presentation.views.fragments.SearchResultFragment

@BindingAdapter("app:keywords")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<String>>?){
    items?.let {
        (recyclerView.adapter as SearchKeywordAdapter).submitList(it.value)
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var searchFragment: Fragment
    private lateinit var mapFragment: Fragment
    private lateinit var mainActivityBinding: ActivityMainBinding

    val searchViewModel: SearchActivityViewModel by viewModels()

    private fun initiateBinding(){
        mainActivityBinding.viewModel = searchViewModel
        mainActivityBinding.lifecycleOwner = this
        mainActivityBinding.rootActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        initiateBinding()
        setContentView(mainActivityBinding.root)

        initiateFragments()
        initiateViews()
        initiateLiveDataObservation()
    }

    private fun initiateLiveDataObservation() {
        searchViewModel.searchText.observe(this) {
            mainActivityBinding.inputSearch.setQuery(it, false)
        }

        searchViewModel.activeContent.observe(this) {
            if (it == SearchActivityViewModel.ContentType.MAP) {
                switchToMapMenu()
            } else {
                switchToSearchMenu()
            }
        }
    }

    private fun initiateSaveKeywordRecyclerView() {
        val adapter =
            SearchKeywordAdapter(object : SearchKeywordClickCallback {
                override fun clickKeyword(keyword: String) {
                    searchViewModel.clickKeyword(keyword)
                }

                override fun clickRemove(keyword: String) {
                    searchViewModel.clickKeywordDeleteButton(keyword)
                }
            })

        mainActivityBinding.savedSearchBar.adapter = adapter
        mainActivityBinding.savedSearchBar.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initiateSearchView() {
        mainActivityBinding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.submitQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mainActivityBinding.inputSearch.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchViewModel.switchContent(SearchActivityViewModel.ContentType.SEARCH_LIST)
            }
        }
    }

    private fun initiateFragments() {
        mapFragment = KakaoMapFragment()
        searchFragment = SearchResultFragment()
    }

    private fun initiateViews() {
        initiateSearchView()
        initiateSaveKeywordRecyclerView()
    }

    private fun switchToSearchMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, searchFragment)
        fragmentReplaceTransaction.commit()
    }

    private fun switchToMapMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, mapFragment)
        fragmentReplaceTransaction.commit()
    }
}
