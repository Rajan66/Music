package com.example.musicplayer.adapters

import android.content.Context
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.rv_music_item,parent,false)
        return TrackHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val currentItem = trackList[position]

        holder.artistName.text = currentItem.artistName
        holder.songName.text = currentItem.songName
        Glide.with(context)
            .load(currentItem.albumImageUrl)
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
