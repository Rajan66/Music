package com.example.musicplayer.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.os.Binder
import android.os.IBinder
import android.provider.Settings

class BackgroundSongService : Service() {

    private var player: MediaPlayer = MediaPlayer()
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()
    }
    inner class MyBinder: Binder(){
        fun currentService(): BackgroundSongService{
            return this@BackgroundSongService
        }
    }
}