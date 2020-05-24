package stas.batura.musicproject

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.room.SHUFFLE_ON
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
fun TextView.setTrackTitleMove(track: TrackKot?) {
    if (track != null) {
        if (track.isPlaying) {
            visibility = View.VISIBLE
            text =
                "                                              ${track.title}                                                 "
        } else {
            visibility = View.GONE
            text = track.title
        }
    } else {
        visibility = View.GONE
        text =  "Title"
    }
}

@BindingAdapter("trackTitleBindingConst")
fun TextView.setTrackTitleConst(track: TrackKot?) {
    if (track != null) {
        if (track.isPlaying) {
            visibility = View.GONE
            text = track.title
        } else {
            visibility = View.VISIBLE
            text = track.title
        }
    } else {
        visibility = View.VISIBLE
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

@BindingAdapter("artistTitleBinding")
fun TextView.setartistTitle(track: TrackKot?) {
    if (track != null) {
        text = track.artist
    } else {
        text =  "Artist"
    }
}

@BindingAdapter("trackImageBinfing")
fun ImageView.setAlbumImage(track: TrackKot?) {
    if (track != null && track.bitmapUri != null) {
        setImageURI(track.bitmapUri)
    } else {
        setImageDrawable(resources.getDrawable(R.drawable.note_image))
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

/**
 * This binding adapter displays the [ImageApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("imageStatus")
fun bindStatus(statusImageView: ImageView, status: NetApiStatus?) {
    when (status) {
        NetApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        NetApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        NetApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * This binding adapter displays the [ImageApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("textStatus")
fun TextView.bindTextStatus( status: NetApiStatus?) {
    when (status) {
        NetApiStatus.LOADING -> {
            visibility = View.GONE
        }
        NetApiStatus.ERROR -> {
            visibility = View.GONE
        }
        NetApiStatus.DONE -> {
//            if (mainAcivityViewModel.songText != null) {
//                setText(mainAcivityViewModel.songText)
//            }
            visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("textBody")
fun TextView.bindTextBody( texts: String?) {
    if (texts != null) {
        text = texts
    } else {
        text = "No text"
    }
}

//@BindingAdapter("shuffleButtonBinding")
//fun ImageButton.setShuffleIcon(status: Int) {
//    if (status == SHUFFLE_OFF) {
//        setImageResource(R.drawable.ic_shuffle_gray_24dp)
//    } else if (status == SHUFFLE_ON) {
//        setImageResource(R.drawable.ic_shuffle_black_24dp)
//    }
//}

//@BindingAdapter ("onPlaylistItemClick")
//fun LinearLayout.setOnPlayListner (linearLayout: LinearLayout,mainAcivityViewModel: MainAcivityViewModel, track: MusicRepository.Track) {
//    print("test")
//}

class ClickHandler () {

}