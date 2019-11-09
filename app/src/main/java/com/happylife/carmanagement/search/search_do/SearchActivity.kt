package com.happylife.carmanagement.search.search_do

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.home.CarItem
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    var m_tv_search_title : TextView? = null
    var m_et_search_content : EditText? = null
    var m_ib_search_toolbar_close : ImageButton? = null
    var m_bt_doSearch : Button? = null

    var kind : Int? = null

    val basicInfo = BasicInfo()

    var m_rv_searchlist : RecyclerView? = null

    val searchList = ArrayList<CarItem>()
    val searchList_id = ArrayList<String>()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val intent = getIntent()
        kind = intent.getIntExtra(basicInfo.INTENT_PAGERPOSITION, -1)

        initView()

        m_ib_search_toolbar_close?.setOnClickListener { finish() }
        m_bt_doSearch?.setOnClickListener { getCarData() }
    }

    fun initView(){
        m_ib_search_toolbar_close = ib_search_toolbar_close
        m_tv_search_title = tv_search_title
        m_et_search_content = et_search_content
        m_bt_doSearch = bt_doSearch
        m_rv_searchlist = rv_searchList

        val searchItem = CarItem("rrr","rrr","rrr","rrr","rrr","rrr","rrr","rrr","rrr")
        searchList.add(searchItem)

        m_rv_searchlist?.adapter = SearchRvAdapter(searchList){
                carItem , position -> {}
        }
        m_rv_searchlist?.layoutManager = LinearLayoutManager(this)

        if(kind == 0){
            m_tv_search_title?.text = getString(R.string.search_carNumber)
        }else if(kind == 1){
            m_tv_search_title?.text = getString(R.string.search_phoneNumber)
        }
    }

    fun getCarData(){
        if(m_et_search_content?.text == null){
            Toast.makeText(this, getString(R.string.search_nothaveContent), Toast.LENGTH_LONG).show()
        }
        else{
            when(kind){
                0->{
                    db.collection(basicInfo.db_ourStore)
                        .document(basicInfo.db_customerCar)
                        .collection(basicInfo.db_carInfo)
                        .whereEqualTo("carNumber", m_et_search_content?.text.toString())
                        .orderBy("date")
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                return@addSnapshotListener
                            }

                            searchList.clear()
                            searchList_id.clear()

                            for( car in querySnapshot!!){
                                searchList_id.add(car.id)
                                searchList.add(car.toObject(CarItem::class.java))
                            }
//                            if(searchList.size != 0){
//                                m_tv_home_noData?.visibility = View.GONE
//                            }else{
//                                m_tv_home_noData?.visibility = View.VISIBLE
//                            }
                            m_rv_searchlist?.adapter!!.notifyDataSetChanged()
                        }
                }
                1->{
                    db.collection(basicInfo.db_ourStore)
                        .document(basicInfo.db_customerCar)
                        .collection(basicInfo.db_carInfo)
                        .whereEqualTo("customerPhoneNumber", m_et_search_content?.text.toString())
                        .orderBy("date")
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                return@addSnapshotListener
                            }

                            searchList.clear()
                            searchList_id.clear()

                            for( car in querySnapshot!!){
                                searchList_id.add(car.id)
                                searchList.add(car.toObject(CarItem::class.java))
                            }
//                            if(searchList.size != 0){
//                                m_tv_home_noData?.visibility = View.GONE
//                            }else{
//                                m_tv_home_noData?.visibility = View.VISIBLE
//                            }
                            m_rv_searchlist?.adapter!!.notifyDataSetChanged()
                        }
                }
            }
        }
    }
}