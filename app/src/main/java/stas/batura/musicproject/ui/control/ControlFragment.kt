package stas.batura.musicproject.ui.control

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
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
import kotlinx.android.synthetic.main.control_fragment_new.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.utils.InjectorUtils


class ControlFragment () : Fragment() {

//    private var x1   = 0f
//    private  var x2  = 0f
//    val MIN_DISTANCE = 150

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
            .of(this.activity!!, InjectorUtils.provideMainViewModel(this.activity!!.application))
            .get(MainAcivityViewModel::class.java)

        val view = inflater.inflate(R.layout.control_fragment_new, container, false)

//        view.setOnTouchListener(OnTouchListener { v, event ->
//                onTouchEvent(event)
//            true
//        })

        return view
    }

    /**
     * после зоздания фрагмента смотрим за действиями
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        play_button.setOnClickListener {
            if (mainViewModel.musicRepository.tracks.value!!.size > 0)
                mainViewModel.checkServiseCreation()
//            mainViewModel.playClicked()
        }
        pause_button.setOnClickListener {
            if (mainViewModel.musicRepository.tracks.value!!.size > 0)
                mainViewModel.pauseyClicked()
        }
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
                val playing = it.state == PlaybackStateCompat.STATE_PLAYING
                play_button.isEnabled = !playing
                pause_button.isEnabled = playing
//                stop_button.isEnabled = playing
            }
        })



        super.onActivityCreated(savedInstanceState)
    }

//    /**
//     * проверяем слайд вправо и влево
//     */
//    private fun onTouchEvent(ev: MotionEvent?): Boolean {
//
//        val myAction: Int = MotionEventCompat.getActionMasked(ev)
//
//        return when (myAction) {
//            MotionEvent.ACTION_UP -> {
//                x2 = ev!!.x
//                val deltaX = x2 - x1
//                if (Math.abs(deltaX) > MIN_DISTANCE) {
//                    if (deltaX < 0) {
//                        print("right")
//                    } else {
//                        findNavController().navigate(R.id.playlistFragment)
//                    }
//                }
//                true
//            }
//
//            MotionEvent.ACTION_DOWN -> {
//                x1 = ev!!.getX()
//                true
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                false
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                true
//            }
//            else -> false
//        }
//    }



}
