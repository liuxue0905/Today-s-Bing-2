package com.lx.todaysbing.adapters

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lx.todaysbing.R
import com.lx.todaysbing.api.BingService
import com.lx.todaysbing.data.bingapis.Image
import com.lx.todaysbing.data.bingapis.Thumbnail
import java.net.URI
import java.net.URL

class BingAdapters {
    companion object {
        @JvmStatic
        fun dayFromPosition(context: Context, position: Int): String {
            return when (position) {
                0 -> context.getString(R.string.yestoday)
                else -> context.getString(R.string.days_ago, position + 1)
            }
        }

        @JvmStatic
        fun getVisibility(context: Context, position: Int): Int {
            when (context.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    if (position % 10 == 0 || position % 10 == 1) {
                        return View.VISIBLE
                    }
                    return View.GONE
                }
                Configuration.ORIENTATION_PORTRAIT -> {
                    if (position % 5 == 0) {
                        return View.VISIBLE
                    }
                    return View.GONE
                }
            }
            return View.GONE
        }

        @JvmStatic
        fun mkt2Market(context: Context, mkt: String?): String? {

            val mktArray = context.resources.getStringArray(R.array.mkt)
            val marketArray = context.resources.getStringArray(R.array.market)

            return try {
                marketArray[mktArray.indexOf(mkt)]
            } catch (exception: Exception) {
                return null
            }
        }
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        view.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(ContextCompat.getDrawable(view.context, R.drawable.no_image))
            .error(ContextCompat.getDrawable(view.context, R.drawable.no_image))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = ImageView.ScaleType.CENTER_CROP
                    return false
                }
            })
            .into(view)
    }
}

@BindingAdapter("imageFromUrl2")
fun bindImageFromUrl2(view: ImageView, image: Image) {
    val imageUrl = image.thumbnailUrl

    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.dimensionRatio = Thumbnail.dimensionRatio(image.thumbnail)
    view.layoutParams = layoutParams

    if (imageUrl.isNotEmpty()) {
        view.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(
                ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_image_24).apply {
                    DrawableCompat.setTint(
                        this!!,
                        ContextCompat.getColor(view.context, R.color.grey_200)
                    )
                })
            .error(
                ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_broken_image_24)
                    .apply {
                        DrawableCompat.setTint(
                            this!!,
                            ContextCompat.getColor(view.context, R.color.grey_200)
                        )
                    })
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = ImageView.ScaleType.FIT_CENTER
                    return false
                }
            })
            .into(view)
    }
}

//@BindingAdapter("android:src")
//fun setImage(view: ImageView, imageUrl: String?) {
//    if (!imageUrl.isNullOrEmpty()) {
//        Glide.with(view.context)
//            .load("https://global.bing.com" + imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(view)
//    }
//}

@BindingAdapter("tintColor")
fun bindTintColor(view: TextView, color: String?) {
    color ?: return
    view.setTextColor(Color.parseColor(color))
    DrawableCompat.setTint(view.compoundDrawables[0], Color.parseColor(color))
}

@BindingAdapter("dayFromPosition")
fun dayFromPosition(view: TextView, position: Int) {
    val text = when (position) {
        0 -> view.context.getString(R.string.yestoday)
        else -> view.context.getString(R.string.days_ago, position + 1)
    }
    view.text = text
}

