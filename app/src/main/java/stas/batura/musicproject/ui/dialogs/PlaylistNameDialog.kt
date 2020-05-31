package stas.batura.musicproject.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.playlist_name_dialog.*
import kotlinx.android.synthetic.main.playlist_name_dialog.view.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.MainActivity
import stas.batura.musicproject.R
import stas.batura.musicproject.generated.callback.OnClickListener

class PlaylistNameDialog:DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.playlist_name_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_yes.setOnClickListener {
            (activity as MainActivity).mainViewModel.addNewPlaylist(playlist_name.text.toString())
            dialog!!.cancel()
        }
        button_no.setOnClickListener {
            dialog!!.cancel()
        }
        dialog!!.setTitle(requireActivity().baseContext.getString(R.string.title))
        super.onViewCreated(view, savedInstanceState)
    }

}