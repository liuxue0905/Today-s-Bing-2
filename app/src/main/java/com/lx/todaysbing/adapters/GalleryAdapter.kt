package com.lx.todaysbing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lx.todaysbing.data.opalapi.Image
import com.lx.todaysbing.databinding.ListItemGalleryBinding
import com.lx.todaysbing.fragment.GalleryFragmentDirections

class GalleryAdapter :
    PagingDataAdapter<Image, GalleryAdapter.GalleryViewHolder>(GalleryDiffCallback()) {

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(position, photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ListItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class GalleryViewHolder(
        private val binding: ListItemGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                binding.image?.let { photo ->
                    navigateToImage(photo, view)
                }
            }
        }

        private fun navigateToImage(
            image: Image,
            view: View
        ) {
            val directions =
                GalleryFragmentDirections.actionGalleryFragmentToGalleryDetailFragment(image)
            view.findNavController().navigate(directions)
        }

        fun bind(position: Int, image: Image?) {
            binding.apply {
                this.position = position
                this.image = image
                executePendingBindings()
            }
        }

    }
}

private class GalleryDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.infoUrl == newItem.infoUrl
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

}