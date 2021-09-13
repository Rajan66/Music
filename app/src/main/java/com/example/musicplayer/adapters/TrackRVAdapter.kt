package com.example.musicplayer.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.utils.Track

class TrackRVAdapter(private var trackList: ArrayList<Track>, var context: Context) :
    RecyclerView.Adapter<TrackRVAdapter.TrackHolder>() {

    private val TAG = "Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.rv_music_item,parent,false)
        return TrackHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val currentItem = trackList[position]
        val songId = trackList[position].songId
        val uri: Uri = Uri.parse("content://media/external/audio/media/$songId/albumart")

        holder.artistName.text = currentItem.artistName
        Log.i(TAG, "onBindViewHolder: " + currentItem.artistName)
        holder.songName.text = currentItem.songName
        Log.i(TAG, "onBindViewHolder: " + currentItem.songName)
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .into(holder.albumImage)

    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val songName: TextView = itemView.findViewById(R.id.text_view_song_name)
        val artistName: TextView = itemView.findViewById(R.id.text_view_artist_name)
        val albumImage: ImageView = itemView.findViewById(R.id.image_view_song)


    }
}
