package com.example.musicplayer.services

import android.app.Notification.VISIBILITY_PUBLIC
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.os.Binder
import android.os.IBinder
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.fragments.HomeFragment
import com.example.musicplayer.fragments.HomeFragment.Companion.songPosition
import com.example.musicplayer.fragments.HomeFragment.Companion.trackList
import com.example.musicplayer.utils.ApplicationUtil
import com.example.musicplayer.utils.ApplicationUtil.Companion.CHANNEL_ID
import com.example.musicplayer.utils.ApplicationUtil.Companion.NEXT
import com.example.musicplayer.utils.ApplicationUtil.Companion.PREV
import com.example.musicplayer.utils.ApplicationUtil.Companion.PlAY
import com.example.musicplayer.utils.Track

private const val TAG = "BackgroundSongService"

class BackgroundSongService : Service() {

    private var player: MediaPlayer = MediaPlayer()
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var songList: ArrayList<Track>

    private val homeFragment = HomeFragment()

    override fun onBind(intent: Intent): IBinder {
        mediaSession = MediaSessionCompat(baseContext,"Music")
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.start()
        Log.i(TAG, "onStartCommand: Service started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()
        Log.i(TAG, "onStartCommand: Service destroyed")
    }

    inner class MyBinder: Binder(){
        fun currentService(): BackgroundSongService{
            return this@BackgroundSongService
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setContentTitle(trackList[songPosition].songName)
            .setContentText(trackList[songPosition].artistName)
            .setSmallIcon(R.drawable.ic_song)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_music))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.ic_prev,"Previous",null)
            .addAction(R.drawable.ic_play,"Play",null)
            .addAction(R.drawable.ic_next,"Next",null)
            .build()

        startForeground(10,notification)



    }
}