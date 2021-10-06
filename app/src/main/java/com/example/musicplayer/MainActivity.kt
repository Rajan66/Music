package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.musicplayer.fragments.FavoriteFragment
import com.example.musicplayer.fragments.HomeFragment

import com.example.musicplayer.fragments.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val favoriteFragment = FavoriteFragment()
    private val settingFragment = SettingFragment()

    lateinit var bottomNavigationView: BottomNavigationView


    companion object{
        lateinit var cardViewPlayer: CardView

        lateinit var imageViewCurrentSong: ImageView

        lateinit var buttonPause: ImageView

        lateinit var buttonPlay: ImageView

        lateinit var textViewCurrentSong : TextView

        lateinit var textViewCurrentArtist : TextView

        lateinit var trackSeekbar : SeekBar

        lateinit var hiddenPanel : ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardViewPlayer = findViewById(R.id.card_view_player)
        buttonPause = findViewById(R.id.button_pause)
        buttonPlay = findViewById(R.id.button_play)
        imageViewCurrentSong = findViewById(R.id.image_view_current_song)
        textViewCurrentArtist = findViewById(R.id.text_view_current_artist)
        textViewCurrentSong = findViewById(R.id.text_view_current_song)
        trackSeekbar = findViewById(R.id.track_seek_bar)

        getPermit()
        displayFragment()

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> changeFragment(homeFragment)
                R.id.menu_favorites -> changeFragment(favoriteFragment)
                R.id.menu_settings -> changeFragment(settingFragment)
            }
            true
        }



//        var notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            // Show controls on lock screen even when user hides sensitive content.
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setSmallIcon(R.drawable.ic_stat_player)
//            // Add media control buttons that invoke intents in your media service
//            .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent) // #0
//            .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent) // #1
//            .addAction(R.drawable.ic_next, "Next", nextPendingIntent) // #2
//            // Apply the media style template
//            .setStyle(MediaNotificationCompat.MediaStyle()
//                .setShowActionsInCompactView(1 /* #1: pause button \*/)
//                .setMediaSession(mediaSession.getSessionToken()))
//            .setContentTitle("Wonderful music")
//            .setContentText("My Awesome Band")
//            .setLargeIcon(albumArtBitmap)
//            .build()
    }



    private fun changeFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    private fun displayFragment(){
        val transaction =  supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,homeFragment)
        transaction.commit()
    }

    private fun getPermit() {
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted:Boolean ->
            if (isGranted){
                Log.i(TAG, "getPermit: Granted")
            }else{
                Log.i(TAG,"getPermit: Failure")
            }

        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission
                    .READ_EXTERNAL_STORAGE
            ) -> {
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}