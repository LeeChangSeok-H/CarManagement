package com.happylife.carmanagement.search.search_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.viewpagerdots.DotsIndicator
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.fragment_search_pager.view.*

import java.util.ArrayList

class SearchPagerFragment : Fragment()  {

    val pagerList = ArrayList<SearchPagerItem>()

    var pagerAdapter : SearchPagerAdapter? = null

    var dotsIndicator : DotsIndicator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_pager, null)

        dotsIndicator = view.dots_vpSearch
        dotsIndicator?.setDotTintRes(R.color.colorCustome_blue)

        pagerList.add(
            SearchPagerItem(
                R.drawable.common_google_signin_btn_icon_dark,
                getString(R.string.search_pagerName_carNumber),
                R.color.colorBlack
            )
        )
        pagerList.add(
            SearchPagerItem(
                R.drawable.common_google_signin_btn_icon_light,
                getString(R.string.search_pagerName_phoneNumber),
                R.color.colorCustome_blue
            )
        )

        pagerAdapter = SearchPagerAdapter(context!!)
        view.vp_search.adapter = pagerAdapter
        for(item in pagerList){
            pagerAdapter?.addItem(item)
        }
        pagerAdapter?.notifyDataSetChanged()

        view.vp_search.setClipToPadding(false);
        view.vp_search.setPadding(40, 0, 40, 0);
        view.vp_search.setPageMargin(0);

        view.dots_vpSearch.attachViewPager(view.vp_search)

        // 처리
        return view
    }

}