package stas.batura.musicproject.ui.control

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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.rijckaert.tim.animatedvector.FloatingMusicActionButton
import kotlinx.android.synthetic.main.control_fragment_new.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
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

           mainViewModel = ViewModelProviders
            .of(this.requireActivity(), InjectorUtils.provideMainViewModel(this.requireActivity().application))
            .get(MainAcivityViewModel::class.java)

        val view = inflater.inflate(R.layout.control_fragment_new, container, false)

        return view
    }

    /**
     * после зоздания фрагмента смотрим за действиями
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        play_pause_button.setOnMusicFabClickListener( object : FloatingMusicActionButton.OnMusicFabClickListener {
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

        // наблюдаем за нажатием кнопок
        mainViewModel.callbackChanges.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.state == PlaybackStateCompat.STATE_PLAYING) {
                    Log.d(TAG, "play")
                } else if (it.state == PlaybackStateCompat.STATE_PAUSED ) {
                    Log.d(TAG, "pause")
                } else if (it.state == PlaybackStateCompat.STATE_NONE) {
                    Log.d(TAG, "none")
                }
                if ( !isPlayButtonClicked) {
                    play_pause_button.playAnimation()
                }
                isPlayButtonClicked = false
            }
        })

        super.onActivityCreated(savedInstanceState)
    }


}
