package stas.batura.musicproject

import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.musicproject.musicservice.MusicRepository

@BindingAdapter ("titleFormatted")
fun TextView.setTrackTilte (track : MusicRepository.Track) {
    text = track.title
}