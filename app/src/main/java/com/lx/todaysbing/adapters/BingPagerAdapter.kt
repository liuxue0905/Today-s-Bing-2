package com.lx.todaysbing.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.lx.todaysbing.data.bing.Image
import com.lx.todaysbing.views.BingImageNDayView
import com.lx.todaysbing.views.BingImageTodayView

class BingPagerAdapter : PagerAdapter() {

    private var mkt: String? = null
    private var color: String? = null
    private var mList: List<Image>? = null

    fun submitList(list: List<Image>?) {
        mList = list
        notifyDataSetChanged()
    }

    fun submitMkt(mkt: String?) {
        this.mkt = mkt
        notifyDataSetChanged()
    }

    fun submitColor(color: String?) {
        this.color = color
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        if (mList.isNullOrEmpty()) {
            return 0
        }
        return Int.MAX_VALUE
    }

    val realCount: Int
        get() = 2

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        return super.instantiateItem(container, position)

        when (position % realCount) {
            0 -> {
                val view = BingImageTodayView(container.context)
                container.addView(view)
                view.bind(mkt, color, mList)
                return view
            }
            1 -> {
                val view = BingImageNDayView(container.context)
                container.addView(view)
                view.bind(mList)
                return view
            }
        }

        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
//        return super.getItemPosition(`object`)
        return POSITION_NONE
    }

    override fun getPageWidth(position: Int): Float {
//        return super.getPageWidth(position)
        return 0.9f
    }
}