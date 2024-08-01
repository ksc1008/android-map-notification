package ksc.campus.tech.kakao.map.presentation.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import ksc.campus.tech.kakao.map.databinding.FragmentSearchResultBinding
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchResultViewModel
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchResultAdapter

@BindingAdapter("app:items")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<SearchResult>>?){
    items?.let {
        (recyclerView.adapter as SearchResultAdapter).submitList(it.value)
    }
}


@AndroidEntryPoint
class SearchResultFragment:
    Fragment() {
    private val viewModel: SearchActivityViewModel by activityViewModels()
    private val searchViewModel: SearchResultViewModel by lazy{ ViewModelProvider(requireActivity())[SearchResultViewModel::class.java] }
    private lateinit var binding: FragmentSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater,container, false)
        return binding.root
    }

    private fun initiateBinding(){
        binding.viewModel = searchViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initiateRecyclerView(view: View) {
        with(binding.listSearchResult) {
            adapter = SearchResultAdapter { item: SearchResult, _: Int ->
                viewModel.clickSearchResultItem(item)
            }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            ))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateBinding()
        initiateRecyclerView(view)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.switchContent(SearchActivityViewModel.ContentType.MAP)
                }
            })
    }
}