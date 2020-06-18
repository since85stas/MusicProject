package stas.batura.musicproject.ui.playlist

//import android.support.v4.media.session.PlaybackStateCompat
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import stas.batura.musicproject.MainAcivityViewModel
//import stas.batura.musicproject.R
//import stas.batura.musicproject.musicservice.MusicRepository
//
//class PlaylistAdapter (val mainAcivityViewModel: MainAcivityViewModel):
//    ListAdapter<MusicRepository.Track, PlaylistAdapter.ViewHolder> (TrackDiffCalback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent, mainAcivityViewModel)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class ViewHolder (val binding: ,
//                      val mainAcivityViewModel: MainAcivityViewModel) : RecyclerView.ViewHolder (binding.root) {
//
//        fun bind (track: MusicRepository.Track) {
//            binding.track = track
//            binding.mainViewModel = mainAcivityViewModel
//            binding.viewHolder = this
//            binding.executePendingBindings()
//        }
//
//        fun onItemClicked () {
//            if (mainAcivityViewModel.callbackChanges.value?.state == PlaybackStateCompat.STATE_PLAYING) {
//                mainAcivityViewModel.onItemClicked(binding.track!!.uri)
//            } else {
//                if (binding.track!!.isPlaying) {
//                    mainAcivityViewModel.playClicked()
//                } else {
//                    mainAcivityViewModel.onItemClicked(binding.track!!.uri)
//                }
//            }
//        }
//
//        companion object {
//            fun from(parent: ViewGroup, mainAcivityViewModel: MainAcivityViewModel): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = PlaylistChildItemViewBinding.inflate(layoutInflater, parent, false)
//                return ViewHolder(binding, mainAcivityViewModel)
//            }
//        }
//    }
//
//
//    class TrackDiffCalback : DiffUtil.ItemCallback<MusicRepository.Track> (){
//
//        override fun areItemsTheSame(
//            oldItem: MusicRepository.Track,
//            newItem: MusicRepository.Track
//        ): Boolean {
//            return oldItem.trackId == newItem.trackId
//        }
//
//        override fun areContentsTheSame(
//            oldItem: MusicRepository.Track,
//            newItem: MusicRepository.Track
//        ): Boolean {
//            return  oldItem == newItem
//        }
//    }
//
//
//
//}