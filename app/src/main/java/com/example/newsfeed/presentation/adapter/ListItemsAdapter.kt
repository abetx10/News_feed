package com.example.newsfeed.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.newsfeed.R
import com.example.newsfeed.data.NewsSource
import com.example.newsfeed.databinding.ItemListBinding
import com.example.newsfeed.domain.model.UnifiedNewsItem

class ListItemsAdapter(
    private var listItems: List<UnifiedNewsItem>,
    private val onItemClickListener: (UnifiedNewsItem) -> Unit,
    private val onFavoriteClickListener: (UnifiedNewsItem, Boolean) -> Unit
) : RecyclerView.Adapter<ListItemsAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = listItems[position]

        Glide.with(holder.itemView)
            .load(currentItem.imageUrl)
            .error(R.drawable.default_image)
            .transform(CenterCrop(), RoundedCorners(32))
            .into(holder.binding.ivMain)

        holder.itemView.setOnClickListener {
            onItemClickListener(currentItem)
        }

        holder.binding.btnFavorite.setOnClickListener {
            val newFavoriteState = !currentItem.isFavorite
            currentItem.isFavorite = newFavoriteState
            holder.binding.btnFavorite.isChecked = newFavoriteState
            onFavoriteClickListener(currentItem, newFavoriteState)
        }

        holder.binding.btnFavorite.isChecked = currentItem.isFavorite
        holder.binding.tvItemTime.text = currentItem.pubDate
        holder.binding.tvItemTitle.text = currentItem.title

        holder.binding.tvItemSource.text = currentItem.source.displayName
        when (currentItem.source) {
            NewsSource.HABR -> {
                holder.binding.tvItemSource.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.habr
                    )
                )
            }
            NewsSource.TECHCRUNCHER -> {
                holder.binding.tvItemSource.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.techcrunch
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun submitList(newList: List<UnifiedNewsItem>) {
        if (newList.isNotEmpty()) {
            listItems = newList
            notifyDataSetChanged()
        }
        Log.d("ListItemsAdapter", "New list submitted with size: ${newList.size}")
    }
}