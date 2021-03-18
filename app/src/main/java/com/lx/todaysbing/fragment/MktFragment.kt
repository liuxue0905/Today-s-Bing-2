package com.lx.todaysbing.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lx.todaysbing.R
import com.lx.todaysbing.adapters.MktAdapter
import com.lx.todaysbing.databinding.FragmentMktBinding
import com.lx.todaysbing.viewmodels.BingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MktFragment : Fragment() {

    private val TAG: String? = MktFragment::class.java.canonicalName

    //    private val viewModel: BingViewModel by viewModels()
    private val viewModel: BingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "viewModel = ${viewModel}")

        val binding = FragmentMktBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = MktAdapter()
        binding.recyclerView.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: MktAdapter) {
        val mktArray = resources.getStringArray(R.array.mkt)
        adapter.submitList(mktArray.toList())
    }
}