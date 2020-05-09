package stas.batura.musicproject.ui.song_decor

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.song_decor_fragment.*
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.R
import stas.batura.musicproject.databinding.SongDecorFragmentBinding
import stas.batura.musicproject.utils.InjectorUtils

class SongDecorFragment : Fragment() {

    private lateinit var mainViewModel: MainAcivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel = ViewModelProviders
            .of(this.activity!!, InjectorUtils.provideMainViewModel(this.activity!!.application))
            .get(MainAcivityViewModel::class.java)

        val bindings : SongDecorFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.song_decor_fragment, container, false)

        bindings.mainViewModel = mainViewModel

        bindings.lifecycleOwner = viewLifecycleOwner

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainViewModel.currentTrackPlaying.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                print("play")
            }
        })

        mainViewModel.callbackChanges.observe(viewLifecycleOwner, Observer {
            if (it != null ) {
                if (it.state == PlaybackStateCompat.STATE_PLAYING) {
                    songTitle.isSelected = true
                } else {
                    songTitle.isSelected = false
                }
            }
        })


        super.onViewCreated(view, savedInstanceState)
    }
}