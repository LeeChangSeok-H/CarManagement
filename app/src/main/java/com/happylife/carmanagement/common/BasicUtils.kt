package com.happylife.carmanagement.common

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.happylife.carmanagement.R
import com.happylife.carmanagement.home.CarItem
import com.happylife.carmanagement.modifycar.ModifyCarActivity
import kotlinx.android.synthetic.main.dialog_confirmcar.view.*

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class BasicUtils {

    val basicInfo = BasicInfo()

    public fun carInfoDialog(context: Context, carItem: CarItem, carId : String){
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

        mDialogView.bt_confirmCar_modify.setOnClickListener { carInfoModifyDiaglog(context, mAlertDialog, carItem, carId) }

        mDialogView.bt_confirmCar_delete.setOnClickListener { carInfoDeleteDialog(context, mAlertDialog, carItem, carId) }
    }

    fun carInfoModifyDiaglog(context: Context, alertDialog: AlertDialog, carItem: CarItem, carId : String){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle(context.getString(R.string.dialog_modifyCar_title))
        builder.setMessage(context.getString(R.string.dialog_modifyCar_confirm))

        builder.setPositiveButton(context.getString(R.string.dialog_ok)) {dialog, id ->

            val intent = Intent(context, ModifyCarActivity::class.java)
            intent.putExtra(basicInfo.INTENT_CARITEM, carItem)
            intent.putExtra(basicInfo.INTENT_CARID, carId)
            context.startActivity(intent)

            alertDialog.dismiss()

        }
        builder.setNegativeButton(context.getString(R.string.dialog_cancel)) {dialog, id ->
        }
        builder.show()
    }

    fun carInfoDeleteDialog(context: Context, alertDialog: AlertDialog, carItem: CarItem, carId : String){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle(context.getString(R.string.dialog_deleteCar_title))
        builder.setMessage(context.getString(R.string.dialog_deleteCar_confirm))

        builder.setPositiveButton(context.getString(R.string.dialog_ok)) {dialog, id ->
            val firebaseDB = FirebaseDB()
            firebaseDB.deleteCar_fireStore(context, carItem, carId)
            alertDialog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.dialog_cancel)) {dialog, id ->
        }
        builder.show()
    }
}