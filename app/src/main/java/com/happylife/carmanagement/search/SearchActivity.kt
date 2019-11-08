package com.happylife.carmanagement.search

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    var m_tv_search_title : TextView? = null
    var m_et_search_content : EditText? = null
    var m_ib_search_toolbar_close : ImageButton? = null

    var kind : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val intent = getIntent()
        kind = intent.getIntExtra("pagerPosition", -1)

        initView()

        m_ib_search_toolbar_close?.setOnClickListener { finish() }




    }

    fun initView(){
        m_ib_search_toolbar_close = ib_search_toolbar_close
        m_tv_search_title = tv_search_title
        m_et_search_content = et_search_content

        if(kind == 0){
            m_tv_search_title?.text = "차량 번호"
        }else if(kind == 1){
            m_tv_search_title?.text = "전화 번호"
        }
    }
}