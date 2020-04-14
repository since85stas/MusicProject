package stas.batura.musicproject.ui.control

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.control_fragment.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.utils.InjectorUtils

class ControlFragment () : Fragment() {

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

        return inflater.inflate(R.layout.control_fragment, container, false)
    }

    /**
     * после зоздания фрагмента смотрим за действиями
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        play_button.setOnClickListener { mainViewModel.playClicked() }
        pause_button.setOnClickListener {mainViewModel.pauseyClicked()}
        stop_button.setOnClickListener {mainViewModel.stopClicked()}
        skip_to_previous_button.setOnClickListener{mainViewModel.prevClicked()}
        skip_to_next_button.setOnClickListener{mainViewModel.nextClicked()}

        // наблюдаем за нажатием кнопок
        mainViewModel.callbackChanges.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val playing = it.state == PlaybackStateCompat.STATE_PLAYING
                play_button.isEnabled = !playing
                pause_button.isEnabled = playing
                stop_button.isEnabled = playing
            }
        })

        super.onActivityCreated(savedInstanceState)
    }

}
