package com.example.hatewait

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG="FirebaseService"

    override fun onNewToken(token:String?){
        Log.d(TAG,"new Token:$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        Log.d(TAG,"From:"+remoteMessage.from)

        NotiAlarm()

        if(remoteMessage.notification!=null) {
            Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
            sendNotification(remoteMessage.notification?.title,remoteMessage.notification?.body)
        }

        if(remoteMessage.data!=null){
            Log.d(TAG, "Notification Message body: ${remoteMessage.data.getValue("body").toString()}")
            sendNotification(remoteMessage.data?.getValue("title").toString(),remoteMessage.data.getValue("body").toString())
        }
    }

    private fun NotiAlarm(){
        val CHANNEL_ID = "Notification"
        val CHANNEL_NAME = "Channel"
        val description = "This is notification channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        var notificationManager: NotificationManager=
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance)
            channel.description=description
            channel.enableLights(true)
            channel.lightColor= Color.RED
            channel.enableVibration(true)
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title:String?,body:String?){
        val intent = Intent(this, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
        }

        // FLAG_ONE_SHOT : 일회성 intent
        var pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        var notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0,notificationBuilder.build())
    }

}