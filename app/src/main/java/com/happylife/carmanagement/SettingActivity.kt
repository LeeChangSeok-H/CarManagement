package com.happylife.carmanagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.common.BasicInfo
import kotlinx.android.synthetic.main.activity_setting.*




class SettingActivity : AppCompatActivity() {

    var m_lv_setting : ListView? = null
    var m_tv_setting_workerName : TextView? = null
    
    var settingArray = arrayListOf("이름 변경", "앱 비밀번호 변경")

    val basicInfo = BasicInfo()

    var pref : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        pref = this.getSharedPreferences(basicInfo.SHAREDPREFERENCES_NAME, 0)

        initView()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, settingArray)
        m_lv_setting?.adapter = arrayAdapter

        m_lv_setting?.setOnItemClickListener { adapterView, view, pos, id ->
            when(pos){
                0 -> {changeWorkerName()}
                1 -> {changeAppPassword()}
            }
        }
    }
    
    fun initView(){
        m_lv_setting = lv_setting
        m_tv_setting_workerName = tv_setting_workerName

        m_tv_setting_workerName?.text = "${pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, "")} 님"
    }

    fun changeWorkerName(){
        val intent = Intent(this, WorkerNameActivity::class.java)
        intent.putExtra(basicInfo.INTENT_OPEN_WORKERNAME, basicInfo.OPENWORKERNAME_SETTING)
        startActivity(intent)
    }

    fun changeAppPassword(){

    }
}