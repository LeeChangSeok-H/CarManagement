package com.happylife.carmanagement.common

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.happylife.carmanagement.R
import com.happylife.carmanagement.home.CarItem
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class FirebaseDB {

    val basicInfo = BasicInfo()

    var pref : SharedPreferences? = null
    val db = FirebaseFirestore.getInstance()

    fun addCar_fireStore(context: Context, caritem : CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .add(caritem)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, R.string.firebaseDB_addCar_success, Toast.LENGTH_LONG).show()
                getToken_fireStore(context, caritem, basicInfo.ACTION_ADDCAR)
            }
            .addOnFailureListener { e -> Toast.makeText(context, R.string.firebaseDB_addCar_fail, Toast.LENGTH_LONG).show()}
    }

    fun modifyCar_fireStore(context: Context, carId : String, caritem: CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .document(carId!!).set(caritem, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, R.string.firebaseDB_modifyCar_success, Toast.LENGTH_LONG).show()
                getToken_fireStore(context, caritem, basicInfo.ACTION_MODIFYCAR)
            }
            .addOnFailureListener { e -> Toast.makeText(context, R.string.firebaseDB_modifyCar_fail, Toast.LENGTH_LONG).show()}
    }

    fun deleteCar_fireStore(context:Context,caritem: CarItem, carId: String){
        db.collection(basicInfo.db_ourStore).document(basicInfo.db_customerCar).collection(basicInfo.db_carInfo).document(carId).delete()
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, R.string.firebaseDB_deleteCar_success, Toast.LENGTH_LONG).show()
                getToken_fireStore(context, caritem, basicInfo.ACTION_DELETECAR)
            }
            .addOnFailureListener { e -> Toast.makeText(context, R.string.firebaseDB_deleteCar_fail, Toast.LENGTH_LONG).show()}
    }

    fun addNewToken_fireStore(data : HashMap<String, Any>){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_token)
            .collection(basicInfo.db_appToken)
            .add(data)
    }

    fun getToken_fireStore(context: Context, caritem : CarItem, mode : Int){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_token)
            .collection(basicInfo.db_appToken)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }
                var tokenList : ArrayList<String>? = ArrayList()
                for( token in querySnapshot!!){
                    tokenList?.add(token.get(basicInfo.db_tokenValue).toString())
                }

                pref = context.getSharedPreferences(basicInfo.SHAREDPREFERENCES_NAME, 0)
                val workerName = pref!!.getString(basicInfo.SHAREDPREFERENCES_KEY_ISNAMECONFIRM, "")

                when(mode){
                    basicInfo.ACTION_ADDCAR -> sendPostToFCM(tokenList!!,  "${workerName}님이 ${context.getString(R.string.FCM_addCar)}" ,"${caritem.date} : ${caritem.carNumber}")
                    basicInfo.ACTION_MODIFYCAR -> sendPostToFCM(tokenList!!, "${workerName}님이 ${context.getString(R.string.FCM_modifyCar)}" ,"${caritem.date} : ${caritem.carNumber}")
                    basicInfo.ACTION_DELETECAR -> sendPostToFCM(tokenList!!, "${workerName}님이 ${context.getString(R.string.FCM_deleteCar)}" ,"${caritem.date} : ${caritem.carNumber}")
                }

            }
    }

    fun sendPostToFCM(tokenList: ArrayList<String>, title: String, msg : String){
        val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
        val SERVER_KEY = "AAAABpOr5o4:APA91bFSytIo0kIjNyhZVjTC-RozXl_0QaOvSY2RqBzlzJhmHoSv4ceAstCM18uLbvJi23Tj82lIBXq-kzoaJyNsXqpfRMEHbvH8k5uu_hdX-GXJSg8LKE6euqxXSaXtzC-KEnCBIxmk"

        Thread(Runnable{
            for(list in tokenList){
                try {
                    // FMC 메시지 생성 start
                    val root = JSONObject()
                    val notification = JSONObject()
                    //notification.put("body", message)
                    notification.put("title", title)
                    notification.put("body",msg)
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
        }).start()

    }

}