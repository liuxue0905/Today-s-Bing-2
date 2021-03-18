package com.lx.todaysbing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lx.todaysbing.data.bing.Image
import com.lx.todaysbing.databinding.ListItemBingImageNdayBinding
import com.lx.todaysbing.fragment.ViewPagerFragmentDirections

class BingImageNDayAdapter : ListAdapter<Image, RecyclerView.ViewHolder>(ImageDiffCallback()) {

    override fun getItemCount(): Int {
        return super.getItemCount() - 1
    }

    override fun getItem(position: Int): Image {
        return super.getItem(position + 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            ListItemBingImageNdayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image = getItem(position)
        (holder as ImageViewHolder).bind(currentList, position, image)
    }

    class ImageViewHolder(
        private val binding: ListItemBingImageNdayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var images: List<Image>? = null

        init {
            binding.setClickListener { view ->
                binding.image?.let { image ->
                    navigateToImage(image, view)
                }
            }
        }

        private fun navigateToImage(
            image: Image,
            view: View
        ) {
            val directions =
                ViewPagerFragmentDirections.actionViewPagerFragmentToBingDetailFragment(
                    images?.toTypedArray()!!,
                    image
                )
            view.findNavController().navigate(directions)
        }

        fun bind(images: List<Image>, position: Int, image: Image) {
            this.images = images

            binding.apply {
                this.position = position
                this.image = image
                executePendingBindings()
            }
        }
    }
}

private class ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}
