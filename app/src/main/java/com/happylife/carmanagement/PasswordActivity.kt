package com.happylife.carmanagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.common.BasicInfo
import kotlinx.android.synthetic.main.activity_password.*
import java.util.prefs.Preferences

class PasswordActivity : AppCompatActivity() {

    var m_et_password_input : EditText? = null
    var m_bt_password_ok : Button? = null

    val db_firestore = FirebaseFirestore.getInstance()
    val basicInfo = BasicInfo()

    var db_password : String? = null

    var pref : SharedPreferences? = null
    var pref_editor : SharedPreferences.Editor? = null
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        pref = this.getSharedPreferences(basicInfo.SHAREDPREFERENCES_NAME, 0)
        pref_editor = pref?.edit()

        passwordFailCount()

        m_et_password_input = et_password_input
        m_bt_password_ok = bt_password_ok

        m_bt_password_ok?.setOnClickListener { cofirmPassword() }

        getPassword_fireStore()
    }

    fun confirmAlreadyPass(){
        if(pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISPASSWORDCONFIRM, "") == db_password){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun passwordFailCount(){
        count = pref!!.getInt(basicInfo.SHAREDPREFERENCES_KEY_ISFAILCOUNT, 0)
        if(count == 5){
            m_bt_password_ok?.isClickable = false
        }
    }

    fun getPassword_fireStore(){
        db_firestore.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_password)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null) {
                    db_password = documentSnapshot.get(basicInfo.db_pw_value).toString()
                    confirmAlreadyPass()
                    m_bt_password_ok?.isClickable = true
                }
            }
    }

    fun cofirmPassword(){
        if(db_password == m_et_password_input?.text.toString()){
            pref_editor!!.putString(basicInfo.SHAREDPREFERENCES_KEY_ISPASSWORDCONFIRM, db_password).apply()
            confirmWorkerName()
        }else{
            count++
            pref_editor!!.putInt(basicInfo.SHAREDPREFERENCES_KEY_ISFAILCOUNT, count)
            if(count != 5){
                Toast.makeText(this, "${count}번 틀리셨습니다. ${5-count}번 남았습니다.", Toast.LENGTH_LONG).show()
            }
            if(count == 5){
                m_bt_password_ok?.isClickable = false
                Toast.makeText(this, getString(R.string.password_wrong_over), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun confirmWorkerName(){
        if(pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, "") == ""){
            val intent = Intent(this, WorkerNameActivity::class.java)
            intent.putExtra(basicInfo.INTENT_OPEN_WORKERNAME, basicInfo.OPENWORKERNAME_PASSWORD)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}