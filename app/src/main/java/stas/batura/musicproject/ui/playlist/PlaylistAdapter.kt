package stas.batura.musicproject.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.playlist_item_view.view.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.databinding.PlaylistFragmentBinding
import stas.batura.musicproject.databinding.PlaylistItemViewBinding
import stas.batura.musicproject.musicservice.MusicRepository

class PlaylistAdapter (val mainAcivityViewModel: MainAcivityViewModel):
    ListAdapter<MusicRepository.Track, PlaylistAdapter.ViewHolder> (TrackDiffCalback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mainAcivityViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder (val binding: PlaylistItemViewBinding,
                      val mainAcivityViewModel: MainAcivityViewModel) : RecyclerView.ViewHolder (binding.root) {

//        lateinit var trackIn : MusicRepository.Track

        fun bind (track: MusicRepository.Track) {
//            trackIn = track
            binding.track = track
            binding.mainViewModel = mainAcivityViewModel
            binding.executePendingBindings()
        }

//        fun onItemClick () {
//            mainAcivityViewModel.onItemClicked(binding.track.uri)
//        }

        companion object {
            fun from(parent: ViewGroup, mainAcivityViewModel: MainAcivityViewModel): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlaylistItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, mainAcivityViewModel)
            }
        }
    }


    class TrackDiffCalback : DiffUtil.ItemCallback<MusicRepository.Track> (){

        override fun areItemsTheSame(
            oldItem: MusicRepository.Track,
            newItem: MusicRepository.Track
        ): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(
            oldItem: MusicRepository.Track,
            newItem: MusicRepository.Track
        ): Boolean {
            return  oldItem == newItem
        }
    }

}