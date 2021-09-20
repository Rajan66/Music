package com.example.musicplayer.fragments

import android.content.ComponentName
import android.content.ServiceConnection
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.MainActivity
import com.example.musicplayer.MainActivity.Companion.buttonPause
import com.example.musicplayer.MainActivity.Companion.buttonPlay
import com.example.musicplayer.MainActivity.Companion.cardViewPlayer
import com.example.musicplayer.MainActivity.Companion.imageViewCurrentSong
import com.example.musicplayer.MainActivity.Companion.textViewCurrentArtist
import com.example.musicplayer.MainActivity.Companion.textViewCurrentSong
import com.example.musicplayer.R
import com.example.musicplayer.adapters.TrackRVAdapter
import com.example.musicplayer.services.BackgroundSongService
import com.example.musicplayer.utils.ItemOnClickListener
import com.example.musicplayer.utils.Track
import java.lang.Exception

class HomeFragment : Fragment(), ItemOnClickListener,ServiceConnection {

    private var trackList: ArrayList<Track> = ArrayList()
    private lateinit var trackRecyclerView : RecyclerView

    private val TAG = "HomeFragment"

    private var playerService: BackgroundSongService? = null

    private var songPosition: Int = 0
    private var isPlaying: Boolean = false

    companion object Player{
        private var mediaPlayer : MediaPlayer? = null
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
            val adapter = TrackRVAdapter(trackList, requireContext(),this)
            trackRecyclerView.adapter = adapter
            Log.i(TAG, "onViewCreated: Not Null $adapter")
        } else {
            Toast.makeText(requireContext(), "No songs to play.... lol", Toast.LENGTH_LONG).show()
        }
    }

    private fun getTrackList():ArrayList<Track>{
        val songList: ArrayList<Track> = ArrayList()
        var songPath: ArrayList<String> = ArrayList()
        val allSongUri:Uri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(MediaStore.Audio.Media.TRACK,MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DATA)

        val cursor: Cursor? = requireContext().contentResolver.query(allSongUri, projection, null,null,null)


        try {
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        var track: Track? = null
                        val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                        val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                        Log.i(TAG, "getTrackList, Song Name: $songName")
                        Log.i(TAG, "getTrackList, Artist Name: $artistName")

                        val songInfo = Track(songName,artistName,songId,songPath)
                        songList.add(songInfo)

                    }while(cursor.moveToNext())
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return songList
    }

    override fun onClickListener(position:Int) {
        Toast.makeText(activity, trackList[position].songName + " clicked!", Toast.LENGTH_SHORT)
            .show()
        songPosition = position
        setFields()
        stopSong()
        playSong()
        createMediaPlayer()
        if (isPlaying) {
            cardViewPlayer.visibility = View.VISIBLE
        }


    }

    private fun setFields(){
        val currentItem = trackList[songPosition]
        val songId = trackList[songPosition].songId
        val uri: Uri = Uri.parse("content://media/external/audio/media/$songId/albumart")
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(imageViewCurrentSong)

        textViewCurrentSong.text = currentItem.songName
        textViewCurrentArtist.text = currentItem.artistName
    }

    private fun createMediaPlayer(){
        try{
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            Log.i(TAG, "createMediaPlayer: " + trackList[songPosition].songPath)
            mediaPlayer!!.setDataSource(trackList[songPosition].songPath)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            buttonPause.visibility = View.VISIBLE
            buttonPlay.visibility = View.GONE
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun stopSong(){
        buttonPause.setOnClickListener(View.OnClickListener {
            mediaPlayer!!.pause()
            buttonPause.visibility = View.GONE
            buttonPlay.visibility = View.VISIBLE
            isPlaying = false
        })
    }

    private fun playSong(){
        buttonPlay.setOnClickListener(View.OnClickListener {
            mediaPlayer!!.start()
            buttonPlay.visibility = View.GONE
            buttonPause.visibility = View.VISIBLE
            isPlaying = true
        })
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

}