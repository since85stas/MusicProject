package stas.batura.musicproject.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.playlist_list_dialog.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.MainActivity
import stas.batura.musicproject.R
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.ui.playlist.PlaylistListAdapter


class PlaylistListDialog(val playlists: List<Playlist>, val mainAcivityViewModel: MainAcivityViewModel): DialogFragment(), onClickLstn {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.playlist_list_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        playlist_list_recycler.layoutManager = LinearLayoutManager(context)

        val adapter = PlaylistListAdapter(mainAcivityViewModel, this)
        adapter.submitList(playlists)
        playlist_list_recycler.adapter = adapter

//        button_yes.setOnClickListener {
//            dialog!!.cancel()
//        }
//        button_no.setOnClickListener {
//            dialog!!.cancel()
//        }

        dialog!!.setTitle("Select Playlist")


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClicked() {
        dialog!!.cancel()
        print("")
    }
}


interface onClickLstn {
    public fun onItemClicked()
}
