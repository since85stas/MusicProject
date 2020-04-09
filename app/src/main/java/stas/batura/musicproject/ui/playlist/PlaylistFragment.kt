package stas.batura.musicproject.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.playlist_fragment.*
import stas.batura.musicproject.R
import stas.batura.musicproject.utils.InjectorUtils

class PlaylistFragment : Fragment () {

    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        playlistViewModel = ViewModelProviders.of (this,
            InjectorUtils.providePlaylistViewModel(activity!!.application)).get(PlaylistViewModel::class.java)

        return inflater.inflate(R.layout.playlist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        playlistViewModel.songListViewModel.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val adapter = PlaylistAdapter(it)
                playlist_recycle_view.adapter = adapter
            }
        })

        playlist_recycle_view.apply {
            layoutManager = LinearLayoutManager(parentFragment!!.requireContext())
        }

        super.onViewCreated(view, savedInstanceState)
    }
}