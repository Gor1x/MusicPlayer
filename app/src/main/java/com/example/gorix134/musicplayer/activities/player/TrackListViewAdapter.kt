package com.example.gorix134.musicplayer.activities.player

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.track_item.view.*


class TrackListViewAdapter(private var data: ArrayList<Track>, private val listener: TrackListViewAdapterInterface) :
    RecyclerView.Adapter<TrackListViewAdapter.ViewHolder>() {

    fun setData(dataChange: ArrayList<Track>) {
        data = dataChange
    }

    interface TrackListViewAdapterInterface {
        fun onListItemClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.track_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.name.text = data[i].name

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.songName

        init {
            view.setOnClickListener {
                val clickedPosition = adapterPosition
                listener.onListItemClickListener(clickedPosition)
            }
        }

    }

}
