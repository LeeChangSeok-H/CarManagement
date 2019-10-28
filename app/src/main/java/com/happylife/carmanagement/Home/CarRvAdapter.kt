package com.happylife.carmanagement.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.item_rv_home.view.*

class CarRvAdapter : RecyclerView.Adapter<CarRvAdapter.MainViewHolder>() {

    var items: MutableList<CarItem> = mutableListOf(CarItem("Title1", "Content1"),
        CarItem("Title2", "Content2"),CarItem("Title3", "Content3"))

    //override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) : MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_home, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let {
                item -> with(holer) {
                tvTitle.text = item.date
                tvContent.text = item.companyName
            }
        }
    }
    inner class MainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.tv_item_rv_home_date
        val tvContent = itemView.tv_item_rv_home_carNumber
    }

//    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.item_rv_home, parent, false)) {
//        val tvTitle = itemView.tv_item_rv_home_date
//        val tvContent = itemView.tv_item_rv_home_carNumber
//    }
}