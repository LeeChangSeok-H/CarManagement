package com.happylife.carmanagement.home.addcar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.common.FirebaseDB
import com.happylife.carmanagement.home.CarItem
import kotlinx.android.synthetic.main.activity_addcar.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddCarActivity : AppCompatActivity() {

    var m_tv_add_date : TextView? = null
    var m_tv_add_time : TextView? = null
    var m_et_add_company : EditText? = null
    var m_et_add_customerPhoneNumber : EditText? = null
    var m_et_add_carNumber : EditText? = null
    var m_et_add_carType : EditText? = null
    var m_et_add_distanceDriven : EditText? = null
    var m_et_add_workList : EditText? = null
    var m_et_add_etc : EditText? = null
    var m_bt_add_ok : Button? = null
    var m_ib_addCar_toolbar_close : ImageButton? = null
    var m_bt_add_changeDate : Button? = null
    var m_bt_add_changeTime : Button? = null

    val calendar = Calendar.getInstance()

    val basicInfo = BasicInfo()
    val dateFormat = SimpleDateFormat(basicInfo.datePattern)
    val timeFormat = SimpleDateFormat(basicInfo.timePattern)

    val firebaseDB = FirebaseDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcar)

        initView()

        m_bt_add_ok?.setOnClickListener {
            addCar()
            finish()
        }
        m_ib_addCar_toolbar_close?.setOnClickListener { finish() }

        dateChangeProcess()
        timeChangeProcess()

        updateDateView()
        updateTimeView()

    }

    fun initView(){
        m_tv_add_date = tv_add_date
        m_tv_add_time = tv_add_time
        m_et_add_company = et_add_company
        m_et_add_customerPhoneNumber = et_add_customerPhoneNumber
        m_et_add_carNumber = et_add_carNumber
        m_et_add_carType = et_add_carType
        m_et_add_distanceDriven = et_add_distanceDriven
        m_et_add_workList = et_add_workList
        m_et_add_etc = et_add_etc
        m_bt_add_ok = bt_add_ok
        m_ib_addCar_toolbar_close = ib_addCar_toolbar_close
        m_bt_add_changeDate = bt_add_changeDate
        m_bt_add_changeTime = bt_add_changeTime
    }

    fun addCar(){

        val caritem = CarItem(
            m_tv_add_date?.text.toString(),
            m_tv_add_time?.text.toString(),
            m_et_add_company?.text.toString(),
            m_et_add_customerPhoneNumber?.text.toString(),
            m_et_add_carNumber?.text.toString(),
            m_et_add_carType?.text.toString(),
            m_et_add_distanceDriven?.text.toString(),
            m_et_add_workList?.text.toString(),
            m_et_add_etc?.text.toString())


        firebaseDB.addCar_fireStore(this, caritem)
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

        m_bt_add_changeDate?.setOnClickListener { DatePickerDialog(this,
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

        m_bt_add_changeTime?.setOnClickListener { tpd.show() }
    }

    fun updateDateView(){
        m_tv_add_date?.text = dateFormat.format(calendar.time)
    }

    fun updateTimeView(){
        m_tv_add_time?.text = timeFormat.format(calendar.time)
    }

}