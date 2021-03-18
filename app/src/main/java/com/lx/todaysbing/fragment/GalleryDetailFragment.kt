package com.lx.todaysbing.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.lx.todaysbing.R
import com.lx.todaysbing.adapters.RelatedImagesAdapter
import com.lx.todaysbing.data.opalapi.Image
import com.lx.todaysbing.databinding.FragmentGalleryDetailBinding
import com.lx.todaysbing.viewmodels.BingApisViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryDetailFragment : Fragment() {

    private val adapter = RelatedImagesAdapter()
    private val args: GalleryDetailFragmentArgs by navArgs()
    private var searchJob: Job? = null
    private val viewModel: BingApisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGalleryDetailBinding.inflate(inflater, container, false)
        context ?: binding.root

        val image = args.image

        binding.recyclerView.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            this.image = image
            executePendingBindings()

            textView.text = image.text.filter { it.isNotEmpty() }.joinToString("\n")
            seeMoreLayout.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(image.infoLink)))
            }

            if (image.wallpaper) {
                toolbar.menu.findItem(R.id.download).isVisible = true
                toolbar.menu.findItem(R.id.info).isVisible = false
            } else {
                toolbar.menu.findItem(R.id.download).isVisible = false
                toolbar.menu.findItem(R.id.info).isVisible = true
            }

            toolbar.menu.findItem(R.id.download).setOnMenuItemClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Image.getUrl(Image.getS(image, "s5")))
                    )
                )
                false
            }

            toolbar.menu.findItem(R.id.info).setOnMenuItemClickListener { menuItem ->
                Snackbar.make(binding.root, R.string.wallpaper_false, Snackbar.LENGTH_LONG).show()
                false
            }
        }

        loadImage(binding)

        search(image)

        return binding.root
    }

    private fun loadImage(binding: FragmentGalleryDetailBinding) {
        binding.progressBar.visibility = View.VISIBLE
        binding.imageView.setOnClickListener(null)
        Glide.with(this)
            .load(Image.getUrl(Image.getS(binding.image, "s5")))
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.progressBar.visibility = View.GONE
                    binding.imageView.setOnClickListener {
                        loadImage(binding)
                    }

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.progressBar.visibility = View.GONE

                    return false
                }
            })
            .into(binding.imageView)
    }

    private fun search(image: Image) {
        val uri = Uri.parse(image.infoUrl)

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.search(
                form = uri.getQueryParameter("FORM") ?: "",
                q = uri.getQueryParameter("q") ?: ""
            ).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}