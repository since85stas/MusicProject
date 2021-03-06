package stas.batura.musicproject.ui.control

import android.graphics.ColorFilter
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget
import kotlinx.android.synthetic.main.control_fragment_new.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.MusicApplication
import stas.batura.musicproject.R
import stas.batura.musicproject.repository.room.REPEAT_OFF
import stas.batura.musicproject.repository.room.REPEAT_ON
import stas.batura.musicproject.repository.room.REPEAT_ONE
import stas.batura.musicproject.repository.room.SHUFFLE_ON
import stas.batura.musicproject.utils.InjectorUtils


class ControlFragment () : Fragment() {

    private val TAG = "controlfragment"

    private var isPlayButtonClicked = false

    companion object {
        fun newInstance() = ControlFragment()
    }

    private lateinit var mainViewModel: MainAcivityViewModel

    /**
     * создаем фрагмент
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val musicAppl = requireActivity().application as MusicApplication
        mainViewModel = ViewModelProvider(musicAppl.modelStore,
            InjectorUtils.provideMainViewModel(requireActivity().application))
            .get(MainAcivityViewModel::class.java)

        val view = inflater.inflate(R.layout.control_fragment_new, container, false)

        return view
    }

    /**
     * после зоздания фрагмента смотрим за действиями
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        play_pause_button.setOnClickListener( object : View.OnClickListener {
            override fun onClick(view: View) {
                isPlayButtonClicked = true
                mainViewModel.checkServiseCreation()
//                mainViewModel.changePlayState()
            }
        })
//        stop_button.setOnClickListener {
//            if (mainViewModel.musicRepository.tracks.value!!.size > 0)
//                mainViewModel.stopClicked()
//        }
        skip_to_previous_button.setOnClickListener{
            if (mainViewModel.musicRepository.tracks.value!!.size > 0)
                mainViewModel.prevClicked()
        }
        skip_to_next_button.setOnClickListener{
            if (mainViewModel.musicRepository.tracks.value!!.size > 0)
            mainViewModel.nextClicked()
        }

        btnRepeat.setOnClickListener{
            val curStat = mainViewModel.controlsLive.value!!.playStatus
            var newStatus = 0
            if (curStat != SHUFFLE_ON) {
                if (curStat == REPEAT_OFF) {
                    newStatus = REPEAT_ON
                } else if (curStat == REPEAT_ON) {
                    newStatus = REPEAT_ONE
                } else if (curStat == REPEAT_ONE) {
                    newStatus = REPEAT_OFF
                }
            }
            mainViewModel.repository.changerPlayStatus(newStatus)
        }

        btnShuffle.setOnClickListener {
            val curStat = mainViewModel.controlsLive.value!!.playStatus
            var newStatus = 0
            if (curStat != SHUFFLE_ON) {
                newStatus = SHUFFLE_ON
            } else {
                newStatus = REPEAT_OFF
            }
            mainViewModel.repository.changerPlayStatus(newStatus)
        }

//        addTour()

        super.onActivityCreated(savedInstanceState)
    }

    private fun addObservers() {
        // наблюдаем за нажатием кнопок
        mainViewModel.callbackChanges.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.state == PlaybackStateCompat.STATE_PLAYING) {
                    play_pause_button.setImageResource(R.drawable.ic_pause_black_24dp)
                } else if (it.state == PlaybackStateCompat.STATE_PAUSED ) {
                    play_pause_button.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                } else if (it.state == PlaybackStateCompat.STATE_NONE) {
                    Log.d(TAG, "none")
//                    play_pause_button.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE)
                } else {
                    Log.d(TAG, "else state")
                }
            }
        })

        mainViewModel.controlsLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(TAG, "contr")
                mainViewModel.musicRepository.updateContols(it)
                btnRepeat.setImageResource(R.drawable.exo_controls_repeat_off)
                if (it.playStatus == REPEAT_ONE) {
                    btnRepeat.setImageResource(R.drawable.ic_repeat_one_black_24dp)
                } else if (it.playStatus == REPEAT_ON) {
                    btnRepeat.setImageResource(R.drawable.ic_repeat_black_24dp)
                } else if (it.playStatus == REPEAT_OFF || it.playStatus == SHUFFLE_ON) {
                    btnRepeat.setImageResource(R.drawable.ic_repeat_gray_24dp)
                }
                if (it.playStatus == SHUFFLE_ON) {
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_black_24dp)
                } else {
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_gray_24dp)
                }
            }
        })
    }

    private fun removeObservers() {
        mainViewModel.callbackChanges.removeObservers(viewLifecycleOwner)
        mainViewModel.controlsLive.removeObservers(viewLifecycleOwner)
    }

    override fun onStart() {
        addObservers()
        super.onStart()
    }

    override fun onPause() {
        removeObservers()
        super.onPause()
    }

    override fun onStop() {
        isPlayButtonClicked = false

        super.onStop()
    }

    private fun addTour() {
        ShowcaseView.Builder(activity)
            .withMaterialShowcase()
            .setTarget(ViewTarget(play_pause_button))
            .hideOnTouchOutside()
            .setContentTitle("R.string.showcase_fragment_title_2")
            .setContentText("R.string.showcase_fragment_message_2")
            .build();
    }
}
