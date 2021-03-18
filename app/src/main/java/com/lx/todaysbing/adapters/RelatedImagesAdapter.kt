package com.lx.todaysbing.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lx.todaysbing.data.bingapis.Image
import com.lx.todaysbing.databinding.ListItemRelatedImagesBinding

class RelatedImagesAdapter :
    PagingDataAdapter<Image, RelatedImagesAdapter.RelatedImageViewHolder>(RelatedImageDiffCallback()) {

    override fun onBindViewHolder(holder: RelatedImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedImageViewHolder {
        return RelatedImageViewHolder(
            ListItemRelatedImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class RelatedImageViewHolder(
        private val binding: ListItemRelatedImagesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                binding.image?.let { image ->
                    val uri = Uri.parse(image.webSearchUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(image: Image?) {
            binding.apply {
                this.image = image
                executePendingBindings()
            }
        }
    }
}

private class RelatedImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

}