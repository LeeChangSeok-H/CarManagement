package com.happylife.carmanagement.home.addcar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.home.CarItem
import kotlinx.android.synthetic.main.activity_addcar.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddCarActivity : AppCompatActivity() {

    val calendar = Calendar.getInstance()

    val db = FirebaseFirestore.getInstance()

    val basicInfo = BasicInfo()
    val dateFormat = SimpleDateFormat(basicInfo.datePattern)
    val timeFormat = SimpleDateFormat(basicInfo.timePattern)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcar)

        bt_add_ok.setOnClickListener {
            addCar()
            finish()
        }
        ib_addCar_toolbar_close.setOnClickListener { finish() }

        dateChangeProcess()
        timeChangeProcess()

        updateDateView()
        updateTimeView()

    }

    fun addCar(){

        val caritem = CarItem(
            tv_add_date.text.toString(),
            tv_add_time.text.toString(),
            et_add_company.text.toString(),
            et_add_customerPhoneNumber.text.toString(),
            et_add_carNumber.text.toString(),
            et_add_carType.text.toString(),
            et_add_distanceDriven.text.toString(),
            et_add_workList.text.toString(),
            et_add_etc.text.toString())

        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .add(caritem)
            .addOnSuccessListener { documentReference -> Toast.makeText(this, "차량 추가 성공", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "차량 추가 실패", Toast.LENGTH_LONG).show()}
    }

    fun dateChangeProcess(){
        var dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateView()
            }
        }

        bt_add_changeDate.setOnClickListener { DatePickerDialog(this,
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show() }
    }

    fun timeChangeProcess(){

        val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)
            updateTimeView()

        }),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true)

        bt_add_changeTime.setOnClickListener { tpd.show() }
    }

    fun updateDateView(){
        tv_add_date.text = dateFormat.format(calendar.time)
    }

    fun updateTimeView(){
        tv_add_time.text = timeFormat.format(calendar.time)
    }

}