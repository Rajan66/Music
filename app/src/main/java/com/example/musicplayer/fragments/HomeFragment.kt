package com.example.musicplayer.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.MainActivity.Companion.buttonPause
import com.example.musicplayer.MainActivity.Companion.buttonPlay
import com.example.musicplayer.MainActivity.Companion.cardViewPlayer
import com.example.musicplayer.MainActivity.Companion.imageViewCurrentSong
import com.example.musicplayer.MainActivity.Companion.textViewCurrentArtist
import com.example.musicplayer.MainActivity.Companion.textViewCurrentSong
import com.example.musicplayer.MainActivity.Companion.trackSeekbar
import com.example.musicplayer.MediaPlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.adapters.TrackRVAdapter
import com.example.musicplayer.services.BackgroundSongService
import com.example.musicplayer.utils.ItemOnClickListener
import com.example.musicplayer.utils.Track
import java.lang.Exception


class HomeFragment : Fragment(), ItemOnClickListener, ServiceConnection {

    private var trackList: ArrayList<Track> = ArrayList()
    private lateinit var trackRecyclerView: RecyclerView

    private val TAG = "HomeFragment"

    private var playerService: BackgroundSongService? = null

    private var songPosition: Int = 0
    private var isPlaying: Boolean = false
    private var nextSong: Int = 0
    private var prevSong: Int = 0

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler


    companion object Player {
        private var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackRecyclerView = view.findViewById(R.id.rv_music)
        trackList = getTrackList()
        if (trackList != null) {
            val adapter = TrackRVAdapter(trackList, requireContext(), this)
            trackRecyclerView.adapter = adapter
            Log.i(TAG, "onViewCreated: Track Size " + trackList.size)
        } else {
            Toast.makeText(requireContext(), "No songs to play.... lol", Toast.LENGTH_LONG).show()
        }

        cardViewPlayer.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireActivity(),MediaPlayerActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
        })
    }

    private fun getTrackList(): ArrayList<Track> {
        val songList: ArrayList<Track> = ArrayList()
        val allSongUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? =
            requireContext().contentResolver.query(allSongUri, projection, selection, null, null)


        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        var track: Track? = null
                        val songName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val artistName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val songId =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                        val songPath =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        val duration =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                        Log.i(TAG, "getTrackList, Song Name: $songName")
                        Log.i(TAG, "getTrackList, Artist Name: $artistName")
                        Log.i(TAG, "getTrackList, Artist Name: $duration")
                        Log.i(TAG, "getTrackList, Song Path: $songPath")


                        val songInfo =
                            Track(songName, artistName, songId, songPath, changeToMinutes(duration))
                        songList.add(songInfo)

                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return songList
    }

    override fun onClickListener(position: Int) {
        Toast.makeText(activity, trackList[position].songName + " playing!", Toast.LENGTH_SHORT)
            .show()
        songPosition = position
        nextSong = songPosition + 1
        prevSong = songPosition - 1

        setFields(songPosition)
        stopSong()
        playSong()

        createMediaPlayer(songPosition)
        changeSong()
        updateProgressBar()
        Log.i(TAG, "onClickListener: position when clicked- $songPosition")


        if (isPlaying) {
            cardViewPlayer.visibility = View.VISIBLE
        }
    }

    private fun changeSong() {
        mediaPlayer!!.setOnCompletionListener {
            if (songPosition != trackList.size - 1) {
                createMediaPlayer(nextSong)
                setFields(nextSong)
                trackSeekbar.progress = 0

                this.songPosition = nextSong
                nextSong += 1
//                Log.i(TAG, "changeSong: nextSong - $nextSong")
                Log.i(TAG, "onClickListener, songPosition - $songPosition")
            } else {
                Log.i(TAG, "onClickListener: song stopped, position : $songPosition")
                onSongCompletion()
            }
        }
    }


    private fun setFields(trackPosition: Int) {
        val currentItem = trackList[trackPosition]
        val songId = trackList[trackPosition].songId
        val uri: Uri = Uri.parse("content://media/external/audio/media/$songId/albumart")
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(imageViewCurrentSong)

        textViewCurrentSong.text = currentItem.songName
        textViewCurrentArtist.text = currentItem.artistName
    }

    private fun createMediaPlayer(trackPosition: Int) {
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            Log.i(TAG, "createMediaPlayer: " + trackList[trackPosition].songPath)
            mediaPlayer!!.setDataSource(trackList[trackPosition].songPath)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            buttonPause.visibility = View.VISIBLE
            buttonPlay.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopSong() {
        buttonPause.setOnClickListener(View.OnClickListener {
            mediaPlayer!!.pause()
            buttonPause.visibility = View.GONE
            buttonPlay.visibility = View.VISIBLE
            isPlaying = false
        })
    }

    private fun onSongCompletion() {
        mediaPlayer!!.pause()
        buttonPause.visibility = View.GONE
        buttonPlay.visibility = View.VISIBLE
        isPlaying = false
    }

    private fun playSong() {
        buttonPlay.setOnClickListener(View.OnClickListener {
            mediaPlayer!!.start()
            buttonPlay.visibility = View.GONE
            buttonPause.visibility = View.VISIBLE
            isPlaying = true
        })
    }


    private fun updateProgressBar() {
        handler = Handler()

        runnable = Runnable {
            if (mediaPlayer != null) {
                trackSeekbar.visibility = View.VISIBLE
                trackSeekbar.max = mediaPlayer!!.duration
                val mCurrentPosition: Int = mediaPlayer!!.currentPosition
                Log.i(TAG, "updateProgressBar, mCurrentPosition: $mCurrentPosition")
                Log.i(TAG, "updateProgressBar, Duration: " + trackSeekbar.max)
                trackSeekbar.progress = mCurrentPosition

                handler.postDelayed(runnable, 0)
            }
        }
        handler.postDelayed(runnable, 0)
    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

    private fun changeToMinutes(number: String): String {
        val milliSeconds: Long = number.toLong()

        val minutes = milliSeconds / (1000 * 60)
        val seconds = milliSeconds / 1000 % 60

        var mins = minutes.toString()
        var secs = seconds.toString()
        if(mins.length < 2){
            mins = "0$mins"
        }
        if(secs.length < 2){
            secs = "0$secs"
        }

        return "$mins:$secs"
    }


}