package com.lx.todaysbing.views

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.lx.todaysbing.R
import com.lx.todaysbing.data.bing.Image
import com.lx.todaysbing.databinding.FragmentBingImageTodayBinding
import com.lx.todaysbing.fragment.ViewPagerFragmentDirections

class BingImageTodayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        FragmentBingImageTodayBinding.inflate(LayoutInflater.from(context), this, true)

    private var images: List<Image>? = null

    init {
        binding.mktTextView.setOnClickListener { view ->
            val directions = ViewPagerFragmentDirections.actionViewPagerFragmentToMktFragment()
            view.findNavController().navigate(directions)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.image_search -> {
                    navigateToGallery()
                    true
                }
            }
            false
        }

        binding.downloadButton.setOnClickListener { view ->
            binding.image?.let { image ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Image.getUrl(image))))
            }
        }

        binding.setClickListener { view ->
            binding.image?.let { image ->
                val directions =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToBingDetailFragment(
                        images?.toTypedArray()!!,
                        image
                    )
                view.findNavController().navigate(directions)
            }
        }
    }

    private fun navigateToGallery() {
        val directions = ViewPagerFragmentDirections.actionViewPagerFragmentToGalleryFragment()
        findNavController().navigate(directions)
    }

    fun bind(mkt: String?, color: String?, images: List<Image>?) {
        this.images = images

        binding.apply {
            this.image = images?.get(0)
            this.mkt = mkt
            this.color = color

            executePendingBindings()

            loadImage(binding, binding.image)
        }
    }

    private fun loadImage(binding: FragmentBingImageTodayBinding, image: Image?) {
        val imageUrl = Image.getUrl(image)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.errorView.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.errorView.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.imageView)
        }
    }
}