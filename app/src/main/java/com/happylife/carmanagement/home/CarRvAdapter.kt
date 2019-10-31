package com.happylife.carmanagement.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.item_rv_home.view.*
import kotlin.collections.ArrayList

class CarRvAdapter(val carList : ArrayList<CarItem>) : RecyclerView.Adapter<CarRvAdapter.MainViewHolder>() {

    var items = carList

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) : MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_home, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let {
                item -> with(holer) {
                tv_time.text = item.time
                tv_companyName.text = item.companyName
                tv_customerPhoneNumber.text = item.customerPhoneNumber
                tv_carNumber.text = item.carNumber
                tv_carType.text = item.carType
                tv_drivenDistance.text = item.drivenDistance + " km"
            }
        }
    }
    inner class MainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tv_time = itemView.tv_item_rv_home_time
        val tv_companyName = itemView.tv_item_rv_home_companyName
        val tv_customerPhoneNumber = itemView.tv_item_rv_home_customerPhoneNumber
        val tv_carNumber = itemView.tv_item_rv_home_carNumber
        val tv_carType = itemView.tv_item_rv_home_carType
        val tv_drivenDistance = itemView.tv_item_rv_home_driveDistance
    }
    
}