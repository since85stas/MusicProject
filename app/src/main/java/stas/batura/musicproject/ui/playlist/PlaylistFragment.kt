package stas.batura.musicproject.ui.playlist

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.method.Touch.onTouchEvent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.filepicker.controller.DialogSelectionListener
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import kotlinx.android.synthetic.main.playlist_fragment.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.MusicApplication
import stas.batura.musicproject.R
import stas.batura.musicproject.databinding.PlaylistFragmentBinding
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.ui.dialogs.DeleteAlertDialog
import stas.batura.musicproject.ui.dialogs.PlaylistListDialog
import stas.batura.musicproject.ui.dialogs.PlaylistNameDialog
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager
import java.io.File

class PlaylistFragment : Fragment (), DialogSelectionListener {

    private var x1   = 0f
    private  var x2  = 0f
    val MIN_DISTANCE = 150

//    private val TAG = PlaylistFragment::class.simpleName

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

//        mainViewModel = ViewModelProviders
//            .of(this.requireActivity(), InjectorUtils.provideMainViewModel(this.requireActivity().application))
//            .get(MainAcivityViewModel::class.java)
        val musicAppl = requireActivity().application as MusicApplication
        mainViewModel = ViewModelProvider(musicAppl.modelStore,
            InjectorUtils.provideMainViewModel(requireActivity().application))
            .get(MainAcivityViewModel::class.java)

        playlistViewModel = ViewModelProviders.of (this,
            InjectorUtils.providePlaylistViewModel(requireActivity().application)).get(PlaylistViewModel::class.java)

        val bindings : PlaylistFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.playlist_fragment ,container, false)

        bindings.playlistViewModel = playlistViewModel
        bindings.lifecycleOwner = this

        val view = bindings.root

        return view
    }

    /**
     * после создания фрагмента
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



//        playlistViewModel.musicRepository.tracksDb.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                print(" tt")
//            }
//        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun addObservers() {
        playlistViewModel.playlistNameClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                openPlaylistSelectDialog()
            }
        })

        playlistViewModel.songListViewModel.observe(viewLifecycleOwner, Observer {
            if (it != null) {
//                val adapter = PlaylistAdapter(mainViewModel)
//                adapter.submitList(it)
//                playlist_recycle_view.adapter = adapter

                val builder: AlbumsDataInfo = AlbumsDataInfo(it)
                val albums = builder.getAlbumsData()
                print("albums")
                val expandAdapter = PlaylistExpandJava(albums, mainViewModel)
                simpleExpandableListView.setAdapter(expandAdapter)
                for (i in 0 until expandAdapter.groupCount) {
                    simpleExpandableListView.expandGroup(i)
                }
                simpleExpandableListView.setSelectedChild(
                    expandAdapter.selAlbId, expandAdapter.selTrackId, true)

                simpleExpandableListView.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
                    override fun onChildClick(
                        parent: ExpandableListView?,
                        v: View?,
                        groupPosition: Int,
                        childPosition: Int,
                        id: Long
                    ): Boolean {
                        val track = expandAdapter.getTrack(groupPosition, childPosition)
                        if (mainViewModel.callbackChanges.value?.state == PlaybackStateCompat.STATE_PLAYING) {
                            mainViewModel.onItemClickedPlayed(track.uri)
                        } else {
                            if (track.isPlaying) {
                                mainViewModel.playClicked()
                            } else {
                                mainViewModel.onItemClicked(track.uri)
                            }
                        }
                        return false
                    }
                })
            }
        })

//        playlist_recycle_view.apply {
//            layoutManager = LinearLayoutManager(requireActivity().baseContext)
//        }
//        playlist_recycle_view.setOnTouchListener({v, event ->
//            onTouchEvent(event)
//            true
//        })

        // наблюдаем за добавлением
        playlistViewModel.addButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                openFileSelectDialog()
            }
        })

        // наблюдаем за сменой основных параметров
        playlistViewModel.mainDataLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("playlist", "pla")
                playlistViewModel.musicRepository.getDbTracks()
            }
        })

        // наблюдаем за кнопкой удаления плейлиста
        playlistViewModel.deleteButtClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                openConfirmDeleteDialog()
            }
        })
    }

    private fun removeObservers() {
        playlistViewModel.playlistNameClicked.removeObservers(viewLifecycleOwner)
        playlistViewModel.songListViewModel.removeObservers(viewLifecycleOwner)
        playlistViewModel.addButtonClicked.removeObservers(viewLifecycleOwner)
        playlistViewModel.mainDataLive.removeObservers(viewLifecycleOwner)
        playlistViewModel.deleteButtClicked.removeObservers(viewLifecycleOwner)
    }

    override fun onStart() {
        addObservers()
        super.onStart()
    }

    override fun onStop() {
        removeObservers()
        super.onStop()
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

    /**
     * открываем диалог с выбором плейлистов
     */
    fun openPlaylistSelectDialog() {
        val dialog = PlaylistListDialog(mainViewModel.playlistListLive.value!!, mainViewModel)
        val fragmentManage = requireActivity().supportFragmentManager
        dialog.show(fragmentManage, "playlist")
    }

    /**
     * создает диалог с подтверждением действия
     */
    fun openConfirmDeleteDialog() {
        val delDialog = DeleteAlertDialog()
        val fragmentManager = requireActivity().supportFragmentManager
        delDialog.setTargetFragment(this, 1111)
        delDialog.show(fragmentManager, "del playlist")
    }

    /**
     * подтверждаем удаление
     */
    fun deletePlaylistOk() {
        playlistViewModel.deletePlaylist()
    }

}