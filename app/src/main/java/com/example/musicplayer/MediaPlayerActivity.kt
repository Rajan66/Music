package com.example.musicplayer

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.musicplayer.fragments.HomeFragment
import com.example.musicplayer.fragments.HomeFragment.Companion.playerService
import com.example.musicplayer.services.BackgroundSongService

private const val TAG = "MediaPlayerActivity"

class MediaPlayerActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()

    private lateinit var backButton : ImageView
    private lateinit var albumArt: ImageView
    private lateinit var prevButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var shuffleButton: ImageView
    private lateinit var loopButton: ImageView
    private lateinit var artistName: TextView
    private lateinit var songName: TextView
    private lateinit var favoriteFilled: ImageView
    private lateinit var favoriteUnFilled: ImageView
    private lateinit var seekBar: SeekBar

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        init()
        homeFragment.updateProgressBar(seekBar)
        homeFragment.playSong(pauseButton,playButton)
        homeFragment.stopSong(pauseButton,playButton)

        if(playerService!!.mediaPlayer!!.isPlaying){
            pauseButton.visibility = View.VISIBLE
            playButton.visibility = View.INVISIBLE
        }
        else{
            pauseButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }
//
        backButton.setOnClickListener(View.OnClickListener { onBackPressed() })


    }

    private fun init(){
        backButton = findViewById(R.id.media_down_arrow)
        albumArt = findViewById(R.id.image_view_albumArt)
        prevButton= findViewById(R.id.media_prev)
        nextButton = findViewById(R.id.media_next)
        playButton = findViewById(R.id.media_play)
        pauseButton = findViewById(R.id.media_pause)
        shuffleButton = findViewById(R.id.media_shuffle)
        loopButton= findViewById(R.id.media_loop)
        artistName = findViewById(R.id.media_current_artist)
        songName = findViewById(R.id.media_current_song)
        favoriteFilled = findViewById(R.id.button_favorite_filled)
        favoriteUnFilled = findViewById(R.id.button_favorite_unfilled)
        seekBar = findViewById(R.id.media_seekbar)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
    }


}