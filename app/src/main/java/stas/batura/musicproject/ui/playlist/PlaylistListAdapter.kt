package stas.batura.musicproject.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.databinding.PlaylistChildItemViewBinding
import stas.batura.musicproject.databinding.PlaylistListItemViewBinding
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.ui.dialogs.onClickLstn

class PlaylistListAdapter(val mainAcivityViewModel: MainAcivityViewModel, val onClickLstn: onClickLstn):
    ListAdapter<Playlist, PlaylistListAdapter.ViewHolder> (PlaylistDiffCalback())
{

    lateinit var listner: onClickLstn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        listner = onClickLstn
        return ViewHolder.from(parent, mainAcivityViewModel, listner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(val binding: PlaylistListItemViewBinding, val mainAcivityViewModel: MainAcivityViewModel, val listnerH: onClickLstn ):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            binding.mainViewModel = mainAcivityViewModel
            binding.playlist = playlist
            binding.viewHolder = this
        }

        fun onItemClecked() {
            mainAcivityViewModel.onNavPlaylistItemClicked(binding.playlist!!.playlistId)
            listnerH.onItemClicked()
        }

        companion object {
            fun from(parent: ViewGroup, mainAcivityViewModel: MainAcivityViewModel, listnerH: onClickLstn): PlaylistListAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlaylistListItemViewBinding.inflate(layoutInflater, parent, false)
                return PlaylistListAdapter.ViewHolder(binding, mainAcivityViewModel, listnerH )
            }
        }


    }

    class PlaylistDiffCalback : DiffUtil.ItemCallback<Playlist> (){

        override fun areItemsTheSame(
            oldItem: Playlist,
            newItem: Playlist
        ): Boolean {
            return oldItem.playlistId == newItem.playlistId
        }

        override fun areContentsTheSame(
            oldItem: Playlist,
            newItem: Playlist
        ): Boolean {
            return  oldItem == newItem
        }
    }


}