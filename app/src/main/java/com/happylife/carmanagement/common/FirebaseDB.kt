package com.happylife.carmanagement.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.happylife.carmanagement.home.CarItem
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class FirebaseDB {

    val basicInfo = BasicInfo()
    //val basicUtils = BasicUtils()

    val db = FirebaseFirestore.getInstance()

    fun addCar_fireStore(context: Context, caritem : CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .add(caritem)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "차량 추가 성공", Toast.LENGTH_LONG).show()

                db.collection(basicInfo.db_ourStore)
                    .document("token")
                    .collection("appToken")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if(firebaseFirestoreException != null){
                            return@addSnapshotListener
                        }
                        var tokenList : ArrayList<String>? = ArrayList()
                        for( token in querySnapshot!!){
                            tokenList?.add(token.get("value").toString())
                        }


                        sendPostToFCM(tokenList!!, "${caritem.carNumber}이 추가되었습니다")


                    }

            }
            .addOnFailureListener { e -> Toast.makeText(context, "차량 추가 실패", Toast.LENGTH_LONG).show()}
    }

    fun modifyCar_fireStore(context: Context, carId : String, caritem: CarItem){
        db.collection(basicInfo.db_ourStore)
            .document(basicInfo.db_customerCar)
            .collection(basicInfo.db_carInfo)
            .document(carId!!).set(caritem, SetOptions.merge())
            .addOnSuccessListener { documentReference -> Toast.makeText(context, "차량 수정 성공", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { e -> Toast.makeText(context, "차량 수정 실패", Toast.LENGTH_LONG).show()}
    }

    fun deleteCar_fireStore(context:Context, carId: String){
        db.collection(basicInfo.db_ourStore).document(basicInfo.db_customerCar).collection(basicInfo.db_carInfo).document(carId).delete()
            .addOnSuccessListener { documentReference -> Toast.makeText(context, "차량 삭제 성공", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { e -> Toast.makeText(context, "차량 삭제 실패", Toast.LENGTH_LONG).show()}
    }

    fun addNewToken_fireStore(data : HashMap<String, Any>){
        db.collection(basicInfo.db_ourStore)
            .document("token")
            .collection("appToken")
            .add(data)
            .addOnSuccessListener { documentReference -> Log.d("zxc", "토큰 추가 성공") }
            .addOnFailureListener { e -> Log.d("zxc", "토큰 추가 실패 ${e}")}
    }

    fun sendPostToFCM(tokenList: ArrayList<String>, msg: String){
        val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
        val SERVER_KEY = "AAAABpOr5o4:APA91bFSytIo0kIjNyhZVjTC-RozXl_0QaOvSY2RqBzlzJhmHoSv4ceAstCM18uLbvJi23Tj82lIBXq-kzoaJyNsXqpfRMEHbvH8k5uu_hdX-GXJSg8LKE6euqxXSaXtzC-KEnCBIxmk"

        Thread(Runnable{
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
                    Log.d("cccc", e.toString())
                }

            }
        }).start()

    }

}