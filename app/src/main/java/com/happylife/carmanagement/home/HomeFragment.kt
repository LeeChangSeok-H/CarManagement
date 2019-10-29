package com.happylife.carmanagement.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.happylife.carmanagement.home.addcar.AddCarActivity
import com.happylife.carmanagement.R
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)

        view.rv_carlist.adapter = CarRvAdapter()
        view.rv_carlist.layoutManager = LinearLayoutManager(view.context)

        view.fabt_add_customerCar.setOnClickListener {
            val intent = Intent(view.context, AddCarActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}
