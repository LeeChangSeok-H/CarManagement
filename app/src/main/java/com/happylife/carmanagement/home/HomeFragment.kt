package com.happylife.carmanagement.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.home.addcar.AddCarActivity
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.common.FirebaseDB
import com.happylife.carmanagement.home.modifycar.ModifyCarActivity
import kotlinx.android.synthetic.main.dialog_confirmcar.view.*
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
    var m_rl_home : RelativeLayout? = null

    val basicInfo = BasicInfo()
    val dateFormat = SimpleDateFormat(basicInfo.datePattern)
    val calendar = Calendar.getInstance()

    val db = FirebaseFirestore.getInstance()

    val carList = ArrayList<CarItem>()
    val carList_id = ArrayList<String>()

    val firebaseDB = FirebaseDB();


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
        m_rl_home = view.rl_home

        m_tv_home_date?.text = dateFormat.format(calendar.time)

        m_rv_carlist?.adapter = CarRvAdapter(carList){
                carItem , position -> carInfoDialog(carItem, position)
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
            DatePickerDialog(view.context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun getCarList(){

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

                carList.clear()
                carList_id.clear()

                for( car in querySnapshot!!){
                    carList_id.add(car.id)
                    carList.add(car.toObject(CarItem::class.java))
                }
                if(carList.size != 0){
                    m_tv_home_noData?.visibility = View.GONE
                }else{
                    m_tv_home_noData?.visibility = View.VISIBLE
                }
                m_rv_carlist?.adapter!!.notifyDataSetChanged()
            }
    }

    fun carInfoDialog(carItem: CarItem, pos: Int){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmcar, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)

        //show dialog
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mDialogView.tv_confirmCar_carNumber.text = carItem.carNumber
        mDialogView.tv_confirmCar_Date_time.text = carItem.date + " " + carItem.time
        mDialogView.tv_confirmCar_companyName.setText(carItem.companyName)
        mDialogView.tv_confirmCar_customerPhoneNumber.setText(carItem.customerPhoneNumber)
        mDialogView.tv_confirmCar_carType.setText(carItem.carType)
        mDialogView.tv_confirmCar_drivenDistance.setText(carItem.drivenDistance)
        mDialogView.tv_confirmCar_workList.setText(carItem.workList)
        mDialogView.tv_confirmCar_etc.setText(carItem.etc)

        //cancel button click of custom layout
        mDialogView.ib_confirmCar_close.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }

        mDialogView.bt_confirmCar_modify.setOnClickListener { carInfoModifyDiaglog(mAlertDialog, pos) }

        mDialogView.bt_confirmCar_delete.setOnClickListener { carInfoDeleteDialog(mAlertDialog, pos) }
    }

    fun carInfoModifyDiaglog(alertDialog: AlertDialog, pos: Int){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("정비 내역 수정")
        builder.setMessage("수정 페이지로 이동하시겠습니까?")

        builder.setPositiveButton("확인") {dialog, id ->

            val intent = Intent(context, ModifyCarActivity::class.java)
            intent.putExtra("carItem", carList[pos])
            intent.putExtra("carId", carList_id[pos])
            startActivity(intent)

            alertDialog.dismiss()

        }
        builder.setNegativeButton("취소") {dialog, id ->
        }
        builder.show()
    }

    fun carInfoDeleteDialog(alertDialog: AlertDialog, pos: Int){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("정비 내역 삭제")
        builder.setMessage("해당 정비 내역을 삭제하시겠습니까?")

        builder.setPositiveButton("확인") {dialog, id ->
            deleteCar(pos)
            alertDialog.dismiss()
        }
        builder.setNegativeButton("취소") {dialog, id ->
        }
        builder.show()
    }


    fun deleteCar(pos: Int){
        firebaseDB.deleteCar_fireStore(carList_id[pos])
    }


}
