package com.happylife.carmanagement.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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


        view.tv_home_date.text = dateFormat.format(calendar.time)



        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                view?.let { tv_home_date.text = dateFormat.format(calendar.time) }
            }
        }

        view.ib_home_changeDate.setOnClickListener {
            DatePickerDialog(view.context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }



        view.rv_carlist.adapter = CarRvAdapter(carList)
        view.rv_carlist.layoutManager = LinearLayoutManager(view.context)

        view.fabt_add_customerCar.setOnClickListener {
            val intent = Intent(view.context, AddCarActivity::class.java)
            startActivity(intent)
        }


        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .whereEqualTo("date", dateFormat.format(calendar.time))
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }

                carList.clear()
                for( car in querySnapshot!!){
                    carList.add(car.toObject(CarItem::class.java))
                }
                rv_carlist.adapter!!.notifyDataSetChanged()
            }

        return view
    }


}
