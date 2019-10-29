package com.happylife.carmanagement.home.addcar

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.activity_addcar.*

class AddCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcar)

        bt_add_ok.setOnClickListener { finish() }
        ib_addCar_toolbar_setting.setOnClickListener { finish() }

    }

}