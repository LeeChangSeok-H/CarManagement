package com.happylife.carmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.home.HomeFragment
import com.happylife.carmanagement.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var m_mainBottonBar : com.gauravk.bubblenavigation.BubbleNavigationConstraintView? = null
    var m_bottomBar_item_home : com.gauravk.bubblenavigation.BubbleToggleView? = null
    var m_bottomBar_item_search : com.gauravk.bubblenavigation.BubbleToggleView? = null
    var m_tv_main_toolbar_title : TextView? = null
    var m_ib_main_toolbar_setting : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        bottomBarChangeListener()
        clickListener()
    }

    fun initView(){
        m_mainBottonBar = mainBottomBar
        m_bottomBar_item_home = bottomBar_item_home
        m_bottomBar_item_search = bottomBar_item_search
        m_tv_main_toolbar_title = tv_main_toolbar_title
        m_ib_main_toolbar_setting = ib_main_toolbar_setting

        setSupportActionBar(main_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()
    }

    fun bottomBarChangeListener(){
        m_mainBottonBar?.setNavigationChangeListener { view, position ->
            when(view){
                m_bottomBar_item_home -> {
                    m_tv_main_toolbar_title?.setText(R.string.title_home)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()

                }
                m_bottomBar_item_search -> {
                    m_tv_main_toolbar_title?.setText(R.string.title_search)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, SearchFragment()).commit()

                }
            }

        }
    }

    fun clickListener(){
        m_ib_main_toolbar_setting?.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}