package com.happylife.carmanagement.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.happylife.carmanagement.addcar.AddCarActivity
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.common.BasicUtils
import com.happylife.carmanagement.common.FirebaseDB
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    var m_rl_home_noData : RelativeLayout? = null
    var m_tv_home_date : TextView? = null
    var m_ib_home_changeDate : ImageButton? = null
    var m_rv_carlist : RecyclerView? = null
    var m_fabt_add_customerCar : com.google.android.material.floatingactionbutton.FloatingActionButton? = null
    var m_rl_home : RelativeLayout? = null

    val basicInfo = BasicInfo()
    val basicUtils = BasicUtils()
    val dateFormat = SimpleDateFormat(basicInfo.datePattern)
    val calendar = Calendar.getInstance()

    val db = FirebaseFirestore.getInstance()

    val carList = ArrayList<CarItem>()
    val carList_id = ArrayList<String>()

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

    override fun onResume() {
        super.onResume()

    }

    fun initView(view : View){
        m_tv_home_date = view.tv_home_date
        m_ib_home_changeDate = view.ib_home_changeDate
        m_rl_home_noData = view.rl_home_noData
        m_rv_carlist = view.rv_carlist
        m_fabt_add_customerCar = view.fabt_add_customerCar
        m_rl_home = view.rl_home

        m_tv_home_date?.text = dateFormat.format(calendar.time)

        m_rv_carlist?.adapter = CarRvAdapter(carList){
                carItem , position -> basicUtils.carInfoDialog(context!!, carItem, carList_id[position])
        }
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
            DatePickerDialog(view.context, R.style.DatePickerDialog, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun getCarList(){

//        m_rl_home_noData?.visibility = View.VISIBLE
//        carList.clear()
//        carList_id.clear()

        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .whereEqualTo(basicInfo.db_date, dateFormat.format(calendar.time))
            .orderBy(basicInfo.db_time, Query.Direction.DESCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }
                carList.clear()
                carList_id.clear()

                for( car in querySnapshot!!){
                    carList_id.add(car.id)
                    carList.add(car.toObject(CarItem::class.java))
                    Log.d("qwe", car.get("carNumber").toString())
                }
                if(carList.size != 0){
                    m_rl_home_noData?.visibility = View.GONE
                }else{
                    m_rl_home_noData?.visibility = View.VISIBLE
                }
                m_rv_carlist?.adapter!!.notifyDataSetChanged()
            }
    }
}
