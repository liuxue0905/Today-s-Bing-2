package com.lx.todaysbing.fragment

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.set
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.lx.todaysbing.R
import com.lx.todaysbing.adapters.GalleryAdapter
import com.lx.todaysbing.databinding.*
import com.lx.todaysbing.viewmodels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val TAG: String? = GalleryFragment::class.java.canonicalName

    private val adapter = GalleryAdapter()
    private var filterJob: Job? = null
    private val viewModel: GalleryViewModel by viewModels()

    private var binding: FragmentGalleryBinding? = null

    private val filtersMap: Map<String, MutableSet<String>> = mapOf(
        Pair("category", mutableSetOf()),
        Pair("color", mutableSetOf()),
        Pair("country", mutableSetOf()),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (binding != null) return binding!!.root

        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        this.binding = binding

        binding.recyclerView.adapter = adapter
        filter(null)
//        subscribeUi(adapter, binding)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.retry.setOnClickListener {
            filter(viewModel.filters)
        }

        val layoutManager = binding.recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        if (position % 10 == 0 || position % 10 == 1) {
                            return 2
                        }
                        return 1
                    }
                    Configuration.ORIENTATION_PORTRAIT -> {
                        if (position % 5 == 0) {
                            return 2
                        }
                        return 1
                    }
                    else ->
                        return 1
                }
                return 1
            }

//            override fun getSpanIndex(position: Int, spanCount: Int): Int {
//                return position % spanCount
//            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> {
//                    navigateToGalleryFilter()
                    showDialogGalleryFilter()
                }
            }
            false
        }

        return binding.root
    }


//    private fun navigateToGalleryFilter() {
//        val directions = GalleryFragmentDirections.actionGalleryFragmentToGalleryFilterFragment()
//        findNavController().navigate(directions)
//    }

    private fun filter(body: Map<String, String?>?) {
        filterJob?.cancel()
        filterJob = lifecycleScope.launch {

            adapter.submitData(PagingData.empty())

            viewModel.filterPictures(body).collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding?.progressBar?.isVisible = loadStates.refresh is LoadState.Loading
                binding?.retry?.isVisible =
                    loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0
//                errorMsg.isVisible = loadState.refresh is LoadState.Error
            }
        }
    }

    private fun showDialogGalleryFilter() {
        fun rectBitmap(srcBmp: Bitmap, color: Int = Color.WHITE): Bitmap {
            val dim = max(srcBmp.width, srcBmp.height)
            val dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(dstBmp)
            canvas.drawColor(color)
            canvas.drawBitmap(
                srcBmp,
                (dim - srcBmp.width) / 2.0F,
                (dim - srcBmp.height) / 2.0F,
                null
            )
            return dstBmp
        }

        fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val bitmap =
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun generateChipIconMap(): Map<String, Bitmap> {
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

//            fun generateMulti(size: Int = 18): Bitmap {
//                val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//
//                for (x in 0 until size) {
//                    for (y in 0 until size) {
//                        val n = x / (size / 3) + y / (size / 3) * 3
//                        val hsv = floatArrayOf((280.0F / 9 * (0.5F + n)), 1.0F, 1.0F)
//                        bitmap[x, y] = Color.HSVToColor(hsv)
//                    }
//                }
//
//                return bitmap
//            }
//            map["Multi"] = generateMulti()

            map["Multi"] = drawableToBitmap(resources.getDrawable(R.drawable.color_multi))

            return map
        }

        val inflater = LayoutInflater.from(context)

        val binding = FragmentGalleryFilterBinding.inflate(inflater)

        binding.categoriesChipGroup.apply {
            removeAllViews()

            val categores_keys = context?.resources?.getStringArray(R.array.categories_keys)
            val categores = context?.resources?.getStringArray(R.array.categories)
            val drawables = listOf(
                R.drawable.ic_baseline_card_travel_24,
                R.drawable.ic_baseline_nature_24,
                R.drawable.ic_baseline_pets_24,
                R.drawable.ic_baseline_sports_24,
                R.drawable.ic_baseline_science_24,
                R.drawable.ic_baseline_home_work_24,
                R.drawable.ic_baseline_stars_24,
                R.drawable.ic_baseline_message_24,
                R.drawable.ic_baseline_do_not_disturb_24,
            )

            for (index in categores_keys!!.indices) {

                val key = categores_keys[index]
                val value = categores!![index]

                ListItemFilterCategoriesBinding.inflate(inflater, this, false).apply {
                    text = value
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip
                    chip.chipIcon = resources.getDrawable(drawables[index])
                    chip.isChecked =
                        viewModel.filters?.get("category")?.split("|")?.contains(key)
                            ?: false

                    chip.setOnCheckedChangeListener { chip, isChecked ->
                        filtersMap["category"]?.apply {
                            if (isChecked) add(key) else remove(key)
                        }
                    }
                }
            }
        }

        binding.colorsChipGroup.apply {
            removeAllViews()

            val colors_keys = context?.resources?.getStringArray(R.array.colors_keys)!!
            val colors = context?.resources?.getStringArray(R.array.colors)!!
            val chipIconMap = generateChipIconMap()

            for (index in colors_keys.indices) {
                val key = colors_keys[index]
                val value = colors[index]

                ListItemFilterColorsBinding.inflate(inflater, this, false).apply {
                    text = value
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip
                    chip.isChecked =
                        viewModel.filters?.get("color")?.split("|")?.contains(key)
                            ?: false

                    chip.setOnCheckedChangeListener { chip, isChecked ->
                        filtersMap["color"]?.apply {
                            if (isChecked) add(key) else remove(key)
                        }
                    }

                    chip.chipIcon = BitmapDrawable(resources, chipIconMap[key])
                }
            }
        }

        binding.countriesChipGroup.apply {
            removeAllViews()

            val countries_keys = context?.resources?.getStringArray(R.array.countries_keys)!!
            val countries = context?.resources?.getStringArray(R.array.countries)!!
            val flags = context?.resources?.obtainTypedArray(R.array.flags)
            for (index in countries_keys.indices) {
                val key = countries_keys[index]
                val value = countries[index]

                ListItemFilterCountriesBinding.inflate(inflater, this, false).apply {
                    text = value
                    executePendingBindings()
                    addView(root)

                    val chip = root as Chip

                    chip.isChecked =
                        viewModel.filters?.get("country")?.split("|")?.contains(key)
                            ?: false

                    chip.setOnCheckedChangeListener { chip, isChecked ->
                        filtersMap["country"]?.apply {
                            if (isChecked) add(key) else remove(key)
                        }
                    }

                    val srcBmp =
                        BitmapFactory.decodeResource(resources, flags!!.getResourceId(index, 0))
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

        AlertDialog.Builder(requireContext())
            .setTitle(context?.getString(R.string.filter))
            .setView(binding.root)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                Log.d(TAG, "getFiltersBody() = ${getFiltersBody()}")

                val filtersBody = getFiltersBody()
                filter(filtersBody)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()

    }

    private fun getFiltersBody(): Map<String, String?>? {
        val map = mutableMapOf<String, String?>()
        filtersMap.map {
            map.put(it.key, it.value.joinToString("|"))
        }

        val iterator = map.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.isNullOrEmpty()) {
                iterator.remove()
            }
        }

        println("getFiltersBody() map = $map")
        println("getFiltersBody() map = ${map.isNotEmpty()}")

        if (map.isNotEmpty()) {
            return map
        }

        return null
    }
}