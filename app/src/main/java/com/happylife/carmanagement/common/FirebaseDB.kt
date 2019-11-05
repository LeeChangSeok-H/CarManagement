package com.happylife.carmanagement.common

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.happylife.carmanagement.home.CarItem

class FirebaseDB {

    val basicInfo = BasicInfo()

    val db = FirebaseFirestore.getInstance()

    fun addCar_fireStore(context: Context, caritem : CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .add(caritem)
            .addOnSuccessListener { documentReference -> Toast.makeText(context, "차량 추가 성공", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { e -> Toast.makeText(context, "차량 추가 실패", Toast.LENGTH_LONG).show()}
    }

    fun modifyCar_fireStore(carId : String, caritem: CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .document(carId!!).set(caritem, SetOptions.merge())
    }

    fun deleteCar_fireStore(carId: String){
        db.collection(basicInfo.db_ourStore).document(basicInfo.db_customerCar).collection(basicInfo.db_carInfo).document(carId).delete()
    }

}