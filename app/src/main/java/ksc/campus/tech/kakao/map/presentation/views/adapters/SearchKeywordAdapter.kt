package ksc.campus.tech.kakao.map.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.databinding.ItemSearchKeywordBinding


class SearchKeywordAdapter(
    private val clickCallback: SearchKeywordClickCallback
) : ListAdapter<String, SearchKeywordAdapter.SearchKeywordViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = true
}) {

    class SearchKeywordViewHolder(private val binding: ItemSearchKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        private var callback: SearchKeywordClickCallback? = null

        init {
            binding.viewHolder = this
        }

        fun onDeleteButtonClick(){
            callback?.clickRemove(binding.keyword?:"")
        }

        fun onKeywordClick(){
            callback?.clickKeyword(binding.keyword?:"")
        }

        fun setOnClickListener(listener: SearchKeywordClickCallback){
            callback = listener
        }

        fun bind(keyword: String){
            binding.keyword = keyword
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordViewHolder {
        val viewHolderBinding =
            ItemSearchKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = SearchKeywordViewHolder(viewHolderBinding)
        holder.setOnClickListener(clickCallback)
        return holder
    }

    override fun onBindViewHolder(holder: SearchKeywordViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}