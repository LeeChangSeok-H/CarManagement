package com.happylife.carmanagement.home

import android.app.DatePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.MainActivity
import com.happylife.carmanagement.home.addcar.AddCarActivity
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    var m_tv_home_noData : TextView? = null
    var m_tv_home_date : TextView? = null
    var m_ib_home_changeDate : ImageButton? = null
    var m_rv_carlist : RecyclerView? = null
    var m_fabt_add_customerCar : com.google.android.material.floatingactionbutton.FloatingActionButton? = null

    val basicInfo = BasicInfo()
    val dateFormat = SimpleDateFormat(basicInfo.datePattern)
    val calendar = Calendar.getInstance()

    val db = FirebaseFirestore.getInstance()

    val carList = ArrayList<CarItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)


        initView(view)

        dateChangeProcess(view)

        m_fabt_add_customerCar?.setOnClickListener {
            val intent = Intent(view.context, AddCarActivity::class.java)
            startActivity(intent)
        }


        getCarList()

        return view
    }

    fun initView(view : View){
        m_tv_home_date = view.tv_home_date
        m_ib_home_changeDate = view.ib_home_changeDate
        m_tv_home_noData = view.tv_home_noData
        m_rv_carlist = view.rv_carlist
        m_fabt_add_customerCar = view.fabt_add_customerCar

        m_tv_home_date?.text = dateFormat.format(calendar.time)

        m_rv_carlist?.adapter = CarRvAdapter(carList)
        m_rv_carlist?.layoutManager = LinearLayoutManager(view.context)
    }

    fun dateChangeProcess(view : View){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                view?.let { m_tv_home_date?.text = dateFormat.format(calendar.time) }
                getCarList()
            }
        }

        m_ib_home_changeDate?.setOnClickListener {
            DatePickerDialog(view.context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun getCarList(){
        carList.clear()
        m_tv_home_noData?.visibility = View.VISIBLE
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .whereEqualTo("date", dateFormat.format(calendar.time))
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }

                for( car in querySnapshot!!){
                    carList.add(car.toObject(CarItem::class.java))
                }
                if(carList.size != 0){
                    m_tv_home_noData?.visibility = View.GONE
                }
                m_rv_carlist?.adapter!!.notifyDataSetChanged()
            }


    }


}
