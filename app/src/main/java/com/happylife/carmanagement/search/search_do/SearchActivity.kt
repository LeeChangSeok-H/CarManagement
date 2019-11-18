package com.happylife.carmanagement.search.search_do

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happylife.carmanagement.R
import com.happylife.carmanagement.common.BasicInfo
import com.happylife.carmanagement.home.CarItem
import kotlinx.android.synthetic.main.activity_search.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.Query
import com.happylife.carmanagement.common.BasicUtils


class SearchActivity : AppCompatActivity() {

    var m_tv_search_title : TextView? = null
    var m_et_search_content : EditText? = null
    var m_ib_search_toolbar_close : ImageButton? = null
    var m_bt_doSearch : Button? = null
    var kind : Int? = null
    val basicInfo = BasicInfo()
    var m_rv_searchlist : RecyclerView? = null
    var m_progress : ProgressBar? = null
    val searchList = ArrayList<CarItem>()
    val searchList_id = ArrayList<String>()
    var m_tv_search_noData : TextView? = null

    val basicUtils = BasicUtils()
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
        m_progress = progressBar_search
        m_tv_search_noData = tv_search_noData

        m_rv_searchlist?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        m_rv_searchlist?.adapter = SearchRvAdapter(searchList){
                carItem , position -> basicUtils.carInfoDialog(this, carItem, searchList_id[position])
        }
        m_rv_searchlist?.layoutManager = LinearLayoutManager(this)

        if(kind == 0){
            m_tv_search_title?.text = getString(R.string.search_carNumber)
        }else if(kind == 1){
            m_tv_search_title?.text = getString(R.string.search_phoneNumber)
        }
    }

    fun getCarData(){
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        if(m_et_search_content?.text == null){
            Toast.makeText(this, getString(R.string.search_nothaveContent), Toast.LENGTH_LONG).show()
        }
        else{
            when(kind){
                0->{
                    m_tv_search_noData?.visibility = View.VISIBLE
                    db.collection(basicInfo.db_ourStore)
                        .document(basicInfo.db_customerCar)
                        .collection(basicInfo.db_carInfo)
                        .whereEqualTo(basicInfo.db_carNumber, m_et_search_content?.text.toString())
                        .orderBy(basicInfo.db_date, Query.Direction.DESCENDING)
                        .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                return@addSnapshotListener
                            }

                            m_progress?.visibility = View.VISIBLE
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            searchList.clear()
                            searchList_id.clear()

                            for( car in querySnapshot!!){
                                searchList_id.add(car.id)
                                searchList.add(car.toObject(CarItem::class.java))
                            }

                            m_rv_searchlist?.adapter!!.notifyDataSetChanged()
                            m_tv_search_noData?.visibility = View.GONE
                            m_progress?.visibility = View.GONE
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                }
                1->{
                    m_tv_search_noData?.visibility = View.VISIBLE
                    db.collection(basicInfo.db_ourStore)
                        .document(basicInfo.db_customerCar)
                        .collection(basicInfo.db_carInfo)
                        .whereEqualTo(basicInfo.db_customerPhoneNumber, m_et_search_content?.text.toString())
                        .orderBy(basicInfo.db_date, Query.Direction.DESCENDING)
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                return@addSnapshotListener
                            }
                            m_progress?.visibility = View.VISIBLE
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            searchList.clear()
                            searchList_id.clear()

                            for( car in querySnapshot!!){
                                searchList_id.add(car.id)
                                searchList.add(car.toObject(CarItem::class.java))
                            }

                            m_rv_searchlist?.adapter!!.notifyDataSetChanged()

                            m_tv_search_noData?.visibility = View.GONE
                            m_progress?.visibility = View.GONE
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                }
            }
        }
    }
}