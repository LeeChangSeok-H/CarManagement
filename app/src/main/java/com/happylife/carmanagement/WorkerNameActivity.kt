package com.happylife.carmanagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.common.BasicInfo
import kotlinx.android.synthetic.main.activity_workername.*

class WorkerNameActivity : AppCompatActivity() {

    var m_et_workerName_input : EditText? = null
    var m_bt_workerName_ok : Button? = null

    val basicInfo = BasicInfo()

    var pref : SharedPreferences? = null
    var pref_editor : SharedPreferences.Editor? = null

    var WHAT_OPEN = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workername)

        pref = this.getSharedPreferences(basicInfo.SHAREDPREFERENCES_NAME, 0)
        pref_editor = pref?.edit()

        WHAT_OPEN = intent.getIntExtra(basicInfo.INTENT_OPEN_WORKERNAME, -1)

        initView()

        m_bt_workerName_ok?.setOnClickListener { setWorkerName() }
    }

    fun initView(){
        m_et_workerName_input = et_workerName_input
        m_bt_workerName_ok = bt_workerName_ok

        m_et_workerName_input?.setText(pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, ""))
    }

    fun setWorkerName(){
        if(m_et_workerName_input?.text.toString() != ""){
            pref_editor!!.putString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, m_et_workerName_input?.text.toString()).apply()
            if(WHAT_OPEN == basicInfo.OPENWORKERNAME_PASSWORD){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(WHAT_OPEN == basicInfo.OPENWORKERNAME_SETTING){
                Toast.makeText(this, getString(R.string.workerName_changeComplete), Toast.LENGTH_LONG).show()
                finish()
            }

        }
        else{
            Toast.makeText(this, getString(R.string.workerName_wrongText), Toast.LENGTH_LONG).show()
        }
    }
}