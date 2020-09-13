package com.example.hatewait.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hatewait.client.CustomerMenu
import com.example.hatewait.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * FirebaseInstanceIdService is deprecated.
     * this is new on firebase-messaging:17.1.0
     */
    //앱이 재설치되거나 유효기간이 만료되면 자동으로 토큰을 새로 생성해 줍니다.
    override fun onNewToken(token: String?) {
        Log.d(LOG_TAG, "Refreshed Token: $token")
    }
    // 가상기기 현재 토큰 : fiARZ0G9lxs:APA91bENjxB-zasfoMSaD3cfUl-d5wWFS9E50NcuSv6c91WWDXxLJl5-SV_tDEu8aHP3AgR_gTPmQVhW_k6yW73wxd2aVK_bn2n1h-8e-27qp7OiN-qcIKOkJZk94Hwuvqfs_KaKZSRj
    // 노트9 현재 토큰: c1cacBQUc2Q:APA91bFt3MUoTGGaapIjrDj1aFVe_R5zvBAuZNDrPG7VVMsED0IS-lDTwHbDxbSWGG7kfLTTPDMsR2JR_Hb1M0bHMUC3kHOmVw5QclUyympAbz2jAAJUU1oEHutcg4r3T63Cld35p-Lb

    /**
     * this method will be triggered every time there is new FCM Message.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(LOG_TAG, "From: " + remoteMessage.from)
        Log.d(LOG_TAG, "Data: " + remoteMessage.data)

        if (remoteMessage.notification != null) {
            Log.d(LOG_TAG, "Notification Message Body: ${remoteMessage.notification?.body}")

            sendNotification(remoteMessage.notification?.body)
        }
    }

    // 수신된 알림을 기기에 표시 (foreground)
    private fun sendNotification(body: String?) {
        val intent = Intent(this, CustomerMenu::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("Notification", body)
        }

        val channelId = getString(R.string.default_notification_channel_id)
//        val description = "This is Colloc channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        var notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "HateWait", importance)
//            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(true)
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)

        }

        var pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        val fullScreenIntent = Intent(this, CustomerMenu::class.java)
//        val fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
//            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        var notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.main_logo)
            .setContentTitle("HateWait")
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)
//            .setFullScreenIntent(fullScreenPendingIntent, true)


        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val LOG_TAG = "MyFirebaseMessaging"
    }
}