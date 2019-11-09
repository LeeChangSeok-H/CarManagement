package com.happylife.carmanagement.search.search_do

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happylife.carmanagement.R
import com.happylife.carmanagement.home.CarItem
import kotlinx.android.synthetic.main.item_rv_search.view.*

class SearchRvAdapter(val carList : ArrayList<CarItem>, val itemClick: (CarItem, Int) -> Unit) : RecyclerView.Adapter<SearchRvAdapter.SearchViewHolder>() {

    var m_carList = carList

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) : SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = m_carList.size

    override fun onBindViewHolder(holer: SearchViewHolder, position: Int) {
        holer.bind(m_carList[position], position)
    }
    inner class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tv_time = itemView.tv_item_rv_search_time
        val tv_companyName = itemView.tv_item_rv_search_companyName
        val tv_customerPhoneNumber = itemView.tv_item_rv_search_customerPhoneNumber
        val tv_carNumber = itemView.tv_item_rv_search_carNumber
        val tv_carType = itemView.tv_item_rv_search_carType
        val tv_drivenDistance = itemView.tv_item_rv_search_driveDistance

        fun bind(carItem: CarItem, position: Int){
            tv_time.text = carItem.time
            tv_companyName.text = carItem.companyName
            tv_customerPhoneNumber.text = carItem.customerPhoneNumber
            tv_carNumber.text = carItem.carNumber
            tv_carType.text = carItem.carType
            tv_drivenDistance.text = carItem.drivenDistance + " km"

            itemView.setOnClickListener { itemClick(carItem, position) }
        }
    }
}