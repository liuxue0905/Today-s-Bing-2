package com.lx.todaysbing.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lx.todaysbing.adapters.BingImageNDayAdapter
import com.lx.todaysbing.data.bing.Image
import com.lx.todaysbing.databinding.FragmentBingImageNdayBinding
import com.lx.todaysbing.viewmodels.RecyclerViewScrollViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import java.util.*

class BingImageNDayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val TAG: String = BingImageNDayView::class.java.getSimpleName()

    private val binding =
        FragmentBingImageNdayBinding.inflate(LayoutInflater.from(context), this, true)

    private var adapter: BingImageNDayAdapter

    private val viewModel: RecyclerViewScrollViewModel =
        ViewModelProvider(FragmentComponentManager.findActivity(context) as AppCompatActivity).get(
            RecyclerViewScrollViewModel::class.java
        )

    private var images: List<Image>? = null

    init {
        val adapter = BingImageNDayAdapter()
        binding.recyclerView.adapter = adapter

        this.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val layoutManager = binding.recyclerView.layoutManager as GridLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(
                            position
                        )
                        val offset = viewHolder!!.itemView.top

                        viewModel.scrollToPositionWithOffset(position, offset)
                    }
                }

            }

//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                println("onScrolled dx = ${dx}, dy = ${dy}")
//            }
        })

        viewModel.data.observe(FragmentComponentManager.findActivity(context) as AppCompatActivity) { value ->
            val layoutManager = binding.recyclerView.layoutManager as GridLayoutManager
            layoutManager.scrollToPositionWithOffset(value.position, value.offset)
        }
    }

    fun bind(images: List<Image>?) {
        this.images = images

//        adapter.submitList(images?.subList(1, images.size))
        adapter.submitList(images)
    }
}