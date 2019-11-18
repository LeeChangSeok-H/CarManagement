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

        isConfirmPassword()
        isConfirmCount()

        m_et_password_input = et_password_input
        m_bt_password_ok = bt_password_ok

        m_bt_password_ok?.setOnClickListener { cofirmPassword() }

        getPassword_fireStore()
    }

    fun isConfirmPassword(){
        if(pref!!.getBoolean(basicInfo.SHAREDPREFERENCES_KEY_ISCONFIRM, false)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun isConfirmCount(){
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
                    m_bt_password_ok?.isClickable = true
                }
            }
    }

    fun cofirmPassword(){
        if(db_password == m_et_password_input?.text.toString()){
            pref_editor!!.putBoolean(basicInfo.SHAREDPREFERENCES_KEY_ISCONFIRM, true).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            count++
            pref_editor!!.putInt(basicInfo.SHAREDPREFERENCES_KEY_ISFAILCOUNT, count)
            if(count != 5){
                Toast.makeText(this, "${count}번 틀리셨습니다. ${5-count} 남았습니다.", Toast.LENGTH_LONG).show()
            }
            if(count == 5){
                m_bt_password_ok?.isClickable = false
                Toast.makeText(this, getString(R.string.password_wrong_over), Toast.LENGTH_LONG).show()
            }
        }
    }
}