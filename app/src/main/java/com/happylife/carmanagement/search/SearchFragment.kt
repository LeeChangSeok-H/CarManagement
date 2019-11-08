package com.happylife.carmanagement.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.ArrayList

class SearchFragment : Fragment()  {

    val pagerList = ArrayList<SearchPagerItem>()

    var pagerAdapter : SearchPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, null)

        pagerList.add(SearchPagerItem(R.drawable.common_google_signin_btn_icon_dark, "차량 번호로 검색", R.color.colorBlack))
        pagerList.add(SearchPagerItem(R.drawable.common_google_signin_btn_icon_light, "전화 번호로 검색", R.color.colorCustome_blue))

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