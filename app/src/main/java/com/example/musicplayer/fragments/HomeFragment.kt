package com.example.musicplayer.fragments

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.adapters.TrackRVAdapter
import com.example.musicplayer.utils.Track
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var trackList: ArrayList<Track> = ArrayList()
    lateinit var trackRecyclerView : RecyclerView

    private val TAG = "HomeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            val adapter = TrackRVAdapter(trackList, requireContext())
            trackRecyclerView.adapter = adapter
            Log.i(TAG, "onViewCreated: Not Null $adapter")
        } else {
            Toast.makeText(requireContext(), "No songs to play.... lol", Toast.LENGTH_LONG).show()
        }
    }

    private fun getTrackList():ArrayList<Track>{
        var songList: ArrayList<Track> = ArrayList()
        var songPath: ArrayList<String> = ArrayList()
        val allSongUri:Uri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(MediaStore.Audio.Media.TRACK,MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ARTIST)

        val cursor: Cursor? = requireContext().contentResolver.query(allSongUri, projection, null,null,null)


        try {
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        var track: Track? = null
                        val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))

                        Log.i(TAG, "getTrackList, Song Name: $songName")
                        Log.i(TAG, "getTrackList, Artist Name: $artistName")

                        val songInfo = Track(songName,artistName,songId)
                        songList.add(songInfo)

                    }while(cursor.moveToNext())
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return songList
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}