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

        mDialogView.bt_confirmCar_delete.setOnClickListener { carInfoDeleteDialog(context, mAlertDialog, carId) }
    }

    fun carInfoModifyDiaglog(context: Context, alertDialog: AlertDialog, carItem: CarItem, carId : String){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("정비 내역 수정")
        builder.setMessage("수정 페이지로 이동하시겠습니까?")

        builder.setPositiveButton("확인") {dialog, id ->

            val intent = Intent(context, ModifyCarActivity::class.java)
            intent.putExtra("carItem", carItem)
            intent.putExtra("carId", carId)
            context.startActivity(intent)

            alertDialog.dismiss()

        }
        builder.setNegativeButton("취소") {dialog, id ->
        }
        builder.show()
    }

    fun carInfoDeleteDialog(context: Context, alertDialog: AlertDialog, carId : String){
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("정비 내역 삭제")
        builder.setMessage("해당 정비 내역을 삭제하시겠습니까?")

        builder.setPositiveButton("확인") {dialog, id ->
            val firebaseDB = FirebaseDB()
            firebaseDB.deleteCar_fireStore(context, carId)
            alertDialog.dismiss()
        }
        builder.setNegativeButton("취소") {dialog, id ->
        }
        builder.show()
    }

    fun sendPostToFCM(tokenList: ArrayList<String>, msg: String){
        val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
        val SERVER_KEY = "AAAABpOr5o4:APA91bFSytIo0kIjNyhZVjTC-RozXl_0QaOvSY2RqBzlzJhmHoSv4ceAstCM18uLbvJi23Tj82lIBXq-kzoaJyNsXqpfRMEHbvH8k5uu_hdX-GXJSg8LKE6euqxXSaXtzC-KEnCBIxmk"

        for(list in tokenList){
            try {
                // FMC 메시지 생성 start
                val root = JSONObject()
                val notification = JSONObject()
                //notification.put("body", message)
                notification.put("title", msg)
                root.put("notification", notification)
                root.put("to", list)
                // FMC 메시지 생성 end

                val Url = URL(FCM_MESSAGE_URL)
                val conn = Url.openConnection() as HttpURLConnection
                conn.setRequestMethod("POST")
                conn.setDoOutput(true)
                conn.setDoInput(true)
                conn.addRequestProperty("Authorization", "key=$SERVER_KEY")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("Content-type", "application/json")
                val os = conn.getOutputStream()
                os.write(root.toString().toByteArray(charset("utf-8")))
                os.flush()
                conn.getResponseCode()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}