package com.example.musicplayer.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationUtil: Application() {
    //Application class is a base class.. this class runs even without the app running
    //channels will be created regardless of the app running or not
    companion object{
        const val CHANNEL_ID = "channel1"
        const val PlAY = "play"
        const val NEXT = "next"
        const val PREV = "prev"
        const val EXIT = "exit"
    }

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"catJAM", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Description for notification"
            //Just for double assurance.. if not the app might crash at some point
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}