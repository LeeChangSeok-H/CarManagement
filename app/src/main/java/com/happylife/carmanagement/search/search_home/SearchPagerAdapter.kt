package com.happylife.carmanagement.search.search_home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.NonNull

import androidx.viewpager.widget.PagerAdapter
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.search.search_do.SearchActivity
import kotlinx.android.synthetic.main.fragment_search_pageritem.view.*

import java.util.ArrayList

class SearchPagerAdapter(val mContext: Context) : PagerAdapter() {

    private val mItems = ArrayList<SearchPagerItem>()

    val basicInfo = BasicInfo()

    fun addItem(it: SearchPagerItem) {
        mItems.add(it)
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.fragment_search_pageritem, container, false)

        val imageView = v.iv_search_pagerItem
        val textView = v.tv_search_pagerItem
        val background = v.rl_search_viewPager

        imageView.setImageResource(mItems[position].imageId)
        textView.setText(mItems[position].text)
        background.setBackground(mItems[position].color)
        container.addView(v)

        background.setOnClickListener {
            val intent = Intent(mContext, SearchActivity::class.java)
            intent.putExtra(basicInfo.INTENT_PAGERPOSITION, position)
            mContext.startActivity(intent)
        }

        return v
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //prevent adding multiple image view
        container.removeView(`object` as RelativeLayout)
    }
}