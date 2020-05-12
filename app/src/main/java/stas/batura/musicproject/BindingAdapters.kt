package stas.batura.musicproject

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.room.TrackKot

@BindingAdapter ("titleFormatted")
fun TextView.setTrackTilte (track : MusicRepository.Track) {
    text = track.title
    if (track.isPlaying) setBackgroundColor(resources.getColor(R.color.colorAccent))
}

@BindingAdapter ("durationFormatted")
fun TextView.setTrackDurat (track : MusicRepository.Track) {
    val dur = track.duration
    var seconds: String = ((dur % 60000) / 1000).toString()
    var minutes: String = (dur / 60000).toString()
    val out = minutes + ":" + seconds
    var txtTime = ""
    if (seconds.length == 1) {
        txtTime = "0" + minutes + ":0" + seconds
    } else {
        txtTime = "0" + minutes + ":" + seconds
    }
    text = txtTime
}

@BindingAdapter("trackTitleBinding")
fun TextView.setTrackTitle(track: TrackKot?) {
    if (track != null) {
        if (track.isPlaying) {
            text =
                "                                              ${track.title}                                                 "
        } else {
            text = track.title
        }
    } else {
        text =  "Title"
    }
}

@BindingAdapter("albumTitleBinding")
fun TextView.setAlbumTitle(track: TrackKot?) {
    if (track != null) {
        text = track.album
    } else {
        text =  "Album"
    }
}

@BindingAdapter("trackImageBinfing")
fun ImageView.setAlbumImage(track: TrackKot?) {
    if (track != null && track.bitmapUri != null) {
        setImageURI(track.bitmapUri)
    } else {
        setImageDrawable(resources.getDrawable(R.drawable.image208815))
    }
}

@BindingAdapter("playlistNameBinding")
fun TextView.setPlaylistName(name: String?) {
    if (name == null) {
        text = "Playlist"
    } else {
        text = name
    }
}

//@BindingAdapter ("onPlaylistItemClick")
//fun LinearLayout.setOnPlayListner (linearLayout: LinearLayout,mainAcivityViewModel: MainAcivityViewModel, track: MusicRepository.Track) {
//    print("test")
//}

class ClickHandler () {

}