package ksc.campus.tech.kakao.map.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.databinding.ItemSearchResultBinding
import ksc.campus.tech.kakao.map.domain.models.SearchResult

class SearchResultAdapter(
    val onItemClicked: ((item: SearchResult, index: Int) -> Unit)
) :
    ListAdapter<SearchResult, SearchResultAdapter.SearchResultViewHolder>(object :
        DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem == newItem

    }) {
    class SearchResultViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        private var onClickCallback: ((SearchResult, Int)->Unit)? = null

        init{
            binding.viewHolder = this
        }

        fun clickItem(){
            binding.searchResult?.let {
                onClickCallback?.invoke(it, bindingAdapterPosition)
            }
        }

        fun setOnClickListener(listener: (SearchResult, Int)->Unit){
            onClickCallback = listener
        }

        fun bindData(item: SearchResult) {
            binding.searchResult = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val holder = SearchResultViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.setOnClickListener(onItemClicked)
        return holder
    }


    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindData(item)
    }
}