package com.happylife.carmanagement.common

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.happylife.carmanagement.MainActivity
import com.happylife.carmanagement.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

        private val TAG = "FirebaseService"

        /**
         * FirebaseInstanceIdService is deprecated.
         * this is new on firebase-messaging:17.1.0
         */
        val firebaseDB = FirebaseDB()
        val basicInfo = BasicInfo()

        override fun onNewToken(token : String) {
            Log.d(TAG, "new Token: $token")

            val data = hashMapOf<String, Any>(basicInfo.db_tokenValue to token)
            firebaseDB.addNewToken_fireStore(data)
        }

        /**
         * this method will be triggered every time there is new FCM Message.
         */
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            Log.d(TAG, "From: " + remoteMessage.from)

            if(remoteMessage.notification != null) {
                Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
                sendNotification(remoteMessage.notification?.body)
            }
        }

        private fun sendNotification(body: String?) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("Notification", body)
            }

            var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            var notificationBuilder = NotificationCompat.Builder(this,"Notification")
                .setSmallIcon(R.mipmap.ic_launcher_car2_round)
                .setContentTitle("Push Notification FCM")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)

            var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //notificationManager.notify(0, notificationBuilder.build())
        }

}