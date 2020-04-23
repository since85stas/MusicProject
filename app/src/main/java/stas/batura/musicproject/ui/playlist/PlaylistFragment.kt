package stas.batura.musicproject.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.filepicker.controller.DialogSelectionListener
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import kotlinx.android.synthetic.main.playlist_fragment.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.databinding.PlaylistFragmentBinding
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager
import java.io.File

class PlaylistFragment : Fragment (), DialogSelectionListener {

    private lateinit var playlistViewModel: PlaylistViewModel

    private lateinit var mainViewModel: MainAcivityViewModel

    /**
     * создаем фрагмент
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        playlistViewModel = ViewModelProviders.of (this,
            InjectorUtils.providePlaylistViewModel(activity!!.application)).get(PlaylistViewModel::class.java)

        mainViewModel = ViewModelProviders
            .of(this.activity!!, InjectorUtils.provideMainViewModel(this.activity!!.application))
            .get(MainAcivityViewModel::class.java)

        val bindings : PlaylistFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.playlist_fragment ,container, false)

        bindings.playlistViewModel = playlistViewModel
        bindings.lifecycleOwner = this
        return bindings.root
    }

    /**
     * после создания фрагмента
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        playlistViewModel.songListViewModel.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val adapter = PlaylistAdapter(mainViewModel)
                adapter.submitList(it)
                playlist_recycle_view.adapter = adapter
            }
        })

        playlist_recycle_view.apply {
            layoutManager = LinearLayoutManager(activity!!.baseContext)
        }

        playlistViewModel.addButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                openFileSelectDialog()
            }
        })

//        playlistViewModel.musicRepository.tracksDb.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                print(" tt")
//            }
//        })

        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * после выборв директории
     */
    override fun onSelectedFilePaths(files: Array<out String>?) {
        print("test select")
        playlistViewModel.addTracksToPlaylist(files!![0])
    }

    /**
     * создаем диалог для выбора директории
     */
    private fun openFileSelectDialog() {
        // test
        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
        properties.root = File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = File(DialogConfigs.DEFAULT_DIR);
        properties.offset = File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        properties.show_hidden_files = false;
        val dialog = FilePickerDialog(activity, properties)
        dialog.setTitle("Select a File")
        dialog.setDialogSelectionListener (this)
        dialog.show()
    }


}