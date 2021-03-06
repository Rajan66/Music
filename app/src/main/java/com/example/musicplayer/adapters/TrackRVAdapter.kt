package com.example.musicplayer.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.utils.ItemOnClickListener
import com.example.musicplayer.utils.Track
import kotlin.reflect.KFunction1

class TrackRVAdapter(private var trackList: ArrayList<Track>, var context: Context, var clickListener: ItemOnClickListener) :
    RecyclerView.Adapter<TrackRVAdapter.TrackHolder>() {

    companion object{
        lateinit var uri: Uri
    }

    private val TAG = "Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.rv_music_item,parent,false)
        return TrackHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val currentItem = trackList[position]
        val songId = trackList[position].songId
        uri = Uri.parse("content://media/external/audio/media/$songId/albumart")

        holder.artistName.text = currentItem.artistName
        Log.i(TAG, "onBindViewHolder: " + currentItem.artistName)
        holder.songName.text = currentItem.songName
        Log.i(TAG, "onBindViewHolder: " + currentItem.songName)
        holder.songDuration.text = currentItem.duration
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .into(holder.albumImage)


        holder.cardView.setOnClickListener(View.OnClickListener {
            clickListener.onClickListener(position)
        })

    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val songName: TextView = itemView.findViewById(R.id.text_view_song_name)
        val artistName: TextView = itemView.findViewById(R.id.text_view_artist_name)
        val albumImage: ImageView = itemView.findViewById(R.id.image_view_song)
        val cardView: CardView = itemView.findViewById(R.id.card_view_music)
        val songDuration: TextView = itemView.findViewById(R.id.text_view_song_duration)

    }
}
