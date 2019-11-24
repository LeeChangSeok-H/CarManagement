package com.happylife.carmanagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.common.FirebaseDB
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.dialog_base_edittext.view.*


class SettingActivity : AppCompatActivity() {

    var m_lv_setting : ListView? = null
    var m_tv_setting_workerName : TextView? = null
    
    var settingArray = arrayListOf("이름 변경", "앱 비밀번호 변경")

    val basicInfo = BasicInfo()

    var pref : SharedPreferences? = null
    val db_firestore = FirebaseFirestore.getInstance()
    val firbaseDB = FirebaseDB()

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

    override fun onResume() {
        super.onResume()
        m_tv_setting_workerName?.text = "${pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, "")} 님"
    }
    
    fun initView(){
        m_lv_setting = lv_setting
        m_tv_setting_workerName = tv_setting_workerName
    }

    fun changeWorkerName(){
        val intent = Intent(this, WorkerNameActivity::class.java)
        intent.putExtra(basicInfo.INTENT_OPEN_WORKERNAME, basicInfo.OPENWORKERNAME_SETTING)
        startActivity(intent)
    }

    fun changeAppPassword(){
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        val dialogView = layoutInflater.inflate(R.layout.dialog_base_edittext, null)
        val dialogTextView = dialogView.tv_dialog_base_editText_title
        val dialogEditText = dialogView.et_dialog_base_editText_pw

        dialogTextView.setText("관리자 비밀번호 확인")
        dialogEditText.setHint("관리자 비밀번호를 입력하세요")

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                getPassword_fireStore(dialogEditText.text.toString())
            }
            .setNegativeButton("취소") { dialogInterface, i ->

            }
            .show()
    }

    fun getPassword_fireStore(inputPw: String){
        db_firestore.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_password)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null) {
                    if(inputPw == documentSnapshot.get(basicInfo.db_pw_admin).toString()){
                        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
                        val dialogView = layoutInflater.inflate(R.layout.dialog_base_edittext, null)
                        val dialogTextView = dialogView.tv_dialog_base_editText_title
                        val dialogEditText = dialogView.et_dialog_base_editText_pw

                        dialogTextView.setText("앱 비밀번호 변경")
                        dialogEditText.setHint("변경할 비밀번호를 입력하세요.")

                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->
                                firbaseDB.modifyPassword_fireStore(this, dialogEditText.text.toString())
                            }
                            .setNegativeButton("취소") { dialogInterface, i ->

                            }
                            .show()
                    }
                    else{
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다." , Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
}