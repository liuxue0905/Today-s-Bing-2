package com.lx.todaysbing.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.lx.todaysbing.R
import com.lx.todaysbing.databinding.FragmentGalleryFilterBinding
import com.lx.todaysbing.databinding.ListItemFilterCategoriesBinding
import com.lx.todaysbing.databinding.ListItemFilterColorsBinding
import com.lx.todaysbing.databinding.ListItemFilterCountriesBinding
import com.lx.todaysbing.viewmodels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class GalleryFilterFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()

    private val filtersMap: Map<String, MutableList<String>> = mapOf(
        Pair("categories", mutableListOf()),
        Pair("colors", mutableListOf()),
        Pair("countries", mutableListOf()),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGalleryFilterBinding.inflate(inflater, container, false)
        context ?: return binding.root

//        val filters = viewModel.filters.value

        binding.categoriesChipGroup.apply {
            removeAllViews()

            for ((index, category) in context?.resources?.getStringArray(R.array.categories)!!
                .withIndex()) {
                ListItemFilterCategoriesBinding.inflate(inflater, this, false).apply {
                    text = category
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip
//                    chip.isChecked =
//                        filters?.get("categories")?.split("|")?.contains(category) ?: false

                    chip.setOnCheckedChangeListener { chip, isChecked ->
                        if (isChecked) {
                            filtersMap["categories"]?.add(category)
                        } else {
                            filtersMap["categories"]?.remove(category)
                        }
                    }
                }
            }
        }

        binding.colorsChipGroup.apply {
            val colors_keys = context?.resources?.getStringArray(R.array.colors_keys)
            val colors = context?.resources?.getStringArray(R.array.colors)

            val chipIconMap = generateChipIconMap()

            removeAllViews()

//            val colorsValues = filters?.get("colors")?.split("|")

//            println("colors = $colorsValues")

            for ((index, colorsKey) in colors_keys!!.withIndex()) {
                ListItemFilterColorsBinding.inflate(inflater, this, false).apply {
                    text = colors!![index]
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip
//                    chip.isChecked =
//                        filters?.get("colors")?.split("|")?.contains(colors[index]) ?: false

                    chip.chipIcon = BitmapDrawable(resources, chipIconMap[colorsKey])
                }
            }
        }

        binding.countriesChipGroup.apply {

            removeAllViews()

            val countries = context?.resources?.getStringArray(R.array.countries)
            val flags = context?.resources?.obtainTypedArray(R.array.flags)

//            val countriesValues = filters?.get("countries")?.split("|")
//            println("countries = $countriesValues")

            for (i in countries!!.indices) {
                val country = countries[i]
                val flag = flags!!.getResourceId(i, 0)

                ListItemFilterCountriesBinding.inflate(inflater, this, false).apply {
                    text = country
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip
//                    chip.isChecked =
//                        filters?.get("countries")?.split("|")?.contains(country) ?: false

                    val srcBmp = BitmapFactory.decodeResource(resources, flag)
                    if (srcBmp != null) {
                        chip.chipIcon = BitmapDrawable(
                            resources,
                            rectBitmap(srcBmp, resources.getColor(R.color.grey_200))
                        )
                    }
                }
            }

            flags?.recycle()
        }

        return binding.root
    }

    private fun generateChipIconMap(): Map<String, Bitmap> {
        val map = hashMapOf<String, Bitmap>()

        mapOf(
            Pair("Orange", "#ef6d36"),
            Pair("Blue", "#1eb4e7"),
            Pair("Yellow", "#fbc43d"),
            Pair("Green", "#2db25c"),
            Pair("Brown", "#7d5b3c"),
            Pair("Black", "#000000"),
            Pair("Pink", "#f280ae"),
            Pair("Red", "#ee3635"),
            Pair("White", "#e2e2e2"),
            Pair("Purple", "#793f9b"),
        ).map { entry ->
            val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            bitmap.setPixel(0, 0, Color.parseColor(entry.value))
            map.put(entry.key, bitmap)
        }

        fun generateMulti(size: Int = 18): Bitmap {
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

            for (x in 0 until size) {
                for (y in 0 until size) {
                    val hsv = floatArrayOf(0.0F, 1.0F, 1.0F)
                    bitmap[x, y] = Color.HSVToColor(hsv)
                }
            }

            return bitmap
        }

//        map["Multi"] = drawableToBitmap(resources.getDrawable(R.drawable.color_multi))
        map["Multi"] = generateMulti()

        return map
    }

//    fun drawableToBitmap(drawable: Drawable): Bitmap {
//        if (drawable is BitmapDrawable) {
//            return drawable.bitmap
//        }
//        val bitmap =
//            Bitmap.createBitmap(
//                drawable.intrinsicWidth,
//                drawable.intrinsicHeight,
//                Bitmap.Config.ARGB_8888
//            )
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//        return bitmap
//    }

    fun rectBitmap(srcBmp: Bitmap, color: Int = Color.WHITE): Bitmap {
        val dim = max(srcBmp.width, srcBmp.height)
        val dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dstBmp)
        canvas.drawColor(color)
        canvas.drawBitmap(
            srcBmp,
            (dim - srcBmp.getWidth()) / 2.0F,
            (dim - srcBmp.getHeight()) / 2.0F,
            null
        )
        return dstBmp
    }

    override fun onDestroy() {


        println("onDestroy")
        println("onDestroy viewModel = $viewModel")
//        println("onDestroy viewModel = ${viewModel.filters.value}")
//        println("onDestroy categoriesList = ${categoriesList}")

        val map = hashMapOf<String, String?>()

//        map.put("categries", categoriesList.joinToString("|"))
//        viewModel.filters.value = map

        println("onDestroy map = ${map}")

        super.onDestroy()
    }
}