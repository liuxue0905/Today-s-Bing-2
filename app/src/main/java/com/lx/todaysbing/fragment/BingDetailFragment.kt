package com.lx.todaysbing.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lx.todaysbing.R
import com.lx.todaysbing.data.bing.Image
import com.lx.todaysbing.databinding.FragmentBingDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PipedInputStream

@AndroidEntryPoint
class BingDetailFragment : Fragment() {

    private val TAG = BingDetailFragment::class.java.canonicalName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBingDetailBinding.inflate(inflater, container, false)
        context ?: binding.root

        val args = BingDetailFragmentArgs.fromBundle(requireArguments())
        val images: List<Image> = args.images.toList()
        val image = args.image

        binding.apply {
            visibilityOfCopyrightView = View.VISIBLE
            executePendingBindings()
        }


        binding.copyrightBinding.apply {
            downloadButton.setOnClickListener {
                binding.image?.let { image ->
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Image.getUrl(image))
                        )
                    )
                }
            }
            copyrightSubtitle1TextView.setOnClickListener {
                binding.image?.let { image ->
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Image.getCopyrightlink(image))
                        )
                    )
                }
            }
        }

        subscribeUi(binding, images, image)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentBingDetailBinding, images: List<Image>, image: Image) {
        binding.apply {
            this.image = image
            executePendingBindings()

            imageView.setOnClickListener {
                loadImage(binding, image)
            }

            previousButton.apply {
                isEnabled = images.indexOf(image) < images.size - 1
                alpha = if (isEnabled) 1.0F else 0.7F

                setOnClickListener {
                    subscribeUi(binding, images, images[images.indexOf(image) + 1])
                }
            }

            nextButton.apply {
                isEnabled = images.indexOf(image) > 0
                alpha = if (isEnabled) 1.0F else 0.7F

                setOnClickListener {
                    subscribeUi(binding, images, images[images.indexOf(image) - 1])
                }
            }

            copyrightButton.setOnClickListener {
                visibilityOfCopyrightView =
                    if (visibilityOfCopyrightView == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }

        loadImage(binding, image)
    }

    private fun loadImage(binding: FragmentBingDetailBinding, image: Image) {
        binding.locationView.isVisible = false

        binding.progressBar.isVisible = true
        Glide.with(this)
            .load(Image.getUrl(image))
            .error(R.drawable.no_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.progressBar.isVisible = false

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    try {
                        Log.d(TAG, "resource = ${resource}")
                        val stream = ByteArrayOutputStream()
                        (resource as BitmapDrawable).bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            stream
                        )
                        val inputStream = ByteArrayInputStream(stream.toByteArray())
                        val exifInterface = ExifInterface(inputStream)
                        Log.d(TAG, "exifInterface = ${exifInterface}")
                        Log.d(TAG, "exifInterface.latLong = ${exifInterface.latLong}")

                        binding.locationView.isVisible =
                            exifInterface.latLong?.isNotEmpty() ?: false
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    binding.progressBar.isVisible = false

                    return false
                }
            })
            .into(binding.imageView)
    }
}