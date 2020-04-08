package stas.batura.musicproject.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stas.batura.musicproject.R
import stas.batura.musicproject.musicservice.MusicRepository

class PlaylistAdapter (val data : Array<MusicRepository.Track> ) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_view,parent,false)
        return ViewHolder(
            view
        )
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ViewHolder (val view : View) : RecyclerView.ViewHolder (view) {

        fun bind (track: MusicRepository.Track) {

        }

    }

}