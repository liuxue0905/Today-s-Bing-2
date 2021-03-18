package com.lx.todaysbing.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lx.todaysbing.R
import com.lx.todaysbing.databinding.ListItemMktBinding
import com.lx.todaysbing.viewmodels.BingViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager

class MktAdapter : ListAdapter<String, RecyclerView.ViewHolder>(StringDiffCallback()) {

    private val TAG = MktAdapter::class.java.canonicalName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MktViewHolder(
            ListItemMktBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as MktViewHolder).bind(item)
    }

    class MktViewHolder(
        private val binding: ListItemMktBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val TAG = MktViewHolder::class.java.canonicalName

        val mktArray = itemView.context.resources.getStringArray(R.array.mkt)
        val marketArray = itemView.context.resources.getStringArray(R.array.market)

        init {
            binding.setClickListener { view ->
                binding.mkt.let { mkt ->
                    val activity =
                        FragmentComponentManager.findActivity(view.context) as FragmentActivity
                    val viewModel = ViewModelProvider(activity).get(BingViewModel::class.java)
                    Log.d(TAG, "viewModel = ${viewModel}")
                    viewModel.setMkt(mkt!!)
                    view.findNavController().navigateUp()
                }
            }
        }

        fun bind(mkt: String) {
            binding.apply {
                this.mkt = mkt
                this.market = marketArray[mktArray.indexOf(mkt)]
                executePendingBindings()
            }
        }
    }
}

private class StringDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}