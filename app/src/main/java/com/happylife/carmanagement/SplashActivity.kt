package com.happylife.carmanagement

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.common.BasicInfo
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var m_tv_splash_info : TextView? = null

    val db_firestore = FirebaseFirestore.getInstance()
    val basicInfo = BasicInfo()

    var pref : SharedPreferences? = null
    var pref_editor : SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        pref = this.getSharedPreferences(basicInfo.SHAREDPREFERENCES_NAME, 0)
        pref_editor = pref?.edit()

        val animationView = findViewById<LottieAnimationView>(R.id.animationView)
        m_tv_splash_info = tv_splash_info

        animationView.setAnimation("lottie_splash_image.json")
        animationView.loop(true)
        animationView.playAnimation()
        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        getServerKey()
    }

    fun getServerKey(){
        m_tv_splash_info?.setText(R.string.splash_getServerKey)
        db_firestore.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_token)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null) {
                    val serverKey = documentSnapshot.get(basicInfo.db_serverKey).toString()
                    pref_editor!!.putString(basicInfo.SHAREDPREFERENCES_KEY_SERVERKEY, serverKey).apply()

                    getPassword_fireStore()
                }
            }
    }

    fun getPassword_fireStore(){
        m_tv_splash_info?.setText(R.string.splash_getPassword)
        db_firestore.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_password)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null) {
                    val db_password = documentSnapshot.get(basicInfo.db_pw_value).toString()
                    Handler().postDelayed({
                        confirmAlreadyPass(db_password)
                    }, 3000)


                }
            }
    }

    fun confirmAlreadyPass(password : String){
        if(pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISPASSWORDCONFIRM, "") == password){
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
        else{
            val intent = Intent(this, PasswordActivity::class.java)
            intent.putExtra(basicInfo.INTENT_PASSWORD, password)
            startActivity(intent)
            finish()
        }
    }
}