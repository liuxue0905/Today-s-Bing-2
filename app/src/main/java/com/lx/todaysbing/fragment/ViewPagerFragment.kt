package com.lx.todaysbing.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.lx.todaysbing.R
import com.lx.todaysbing.adapters.BingPagerAdapter
import com.lx.todaysbing.databinding.FragmentViewPagerBinding
import com.lx.todaysbing.viewmodels.BingViewModel
import com.lx.todaysbing.viewmodels.RecyclerViewScrollViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ViewPagerFragment : Fragment() {

    private val TAG: String? = ViewPagerFragment::class.java.canonicalName

    //    private val viewModel: BingViewModel by viewModels()
    private val viewModel: BingViewModel by activityViewModels()
    val viewModelRecyclerViewScroll: RecyclerViewScrollViewModel by activityViewModels()
    private lateinit var adapter: BingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "viewModel = ${viewModel}")

        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = BingPagerAdapter()
        binding.viewPager.adapter = adapter

        this.adapter = adapter

        binding.viewPager.apply {
            offscreenPageLimit = 3
            pageMargin = 0
//            currentItem = (adapter.count / 2) - ((adapter.count / 2) % adapter.realCount)

            (parent as View).setOnTouchListener { v, event ->
                dispatchTouchEvent(event)
            }
        }

        binding.retry.setOnClickListener {
            val mkt = viewModel.getMkt()
            viewModel.setMkt("")
            viewModel.setMkt(mkt)
        }

        subscribeUi(adapter, binding)

//    private fun setupViewPagerLayoutParams() {
//
//    }
//        binding.root.viewTreeObserver.apply {
//            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    setupViewPagerLayoutParams()
//
//                    removeOnGlobalLayoutListener(this)
//                }
//            })
//        }

        return binding.root
    }



    private fun subscribeUi(adapter: BingPagerAdapter, binding: FragmentViewPagerBinding) {
        viewModel.response.observe(viewLifecycleOwner) { response ->

            println("subscribeUi response = $response")

            adapter.submitMkt(viewModel.getMkt())
            adapter.submitColor("#006AC1")
            adapter.submitList(response?.images)
            binding.viewPager.currentItem = (adapter.count / 2) - ((adapter.count / 2) % adapter.realCount)

            viewModelRecyclerViewScroll.scrollToPositionWithOffset(0, 0)
        }

        viewModel.loadState.observe(viewLifecycleOwner) { loadState ->
            if (loadState is LoadState.Error) {
                adapter.submitList(emptyList())
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.viewPager.isVisible = loadState is LoadState.NotLoading
            binding.retry.isVisible = loadState !is LoadState.Loading && loadState is LoadState.Error
        }
    }
}