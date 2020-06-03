package stas.batura.musicproject.utils

import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.File

class MediaDataInfo {

    private var filePath: File? = null;
   lateinit var metaRetriever: MediaMetadataRetriever

    constructor( file: File, metaRetrieverIn: MediaMetadataRetriever) {
        filePath = file
        metaRetriever = metaRetrieverIn
        metaRetriever.setDataSource(filePath!!.absolutePath)
    }

    fun getDuration(): Long {
        if (filePath != null) {
            var duration: String =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var dur: Long = duration.toLong()
            return dur;
        }
        return 0L
    }

    fun getYear(): String {
        if (filePath != null) {
            val year = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
            if (year != null) {
                return year
            } else {
                return ""
            }
        }
        return ""
    }

    fun getBitrate(): String {
        if (filePath != null) {
            val year = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
            return year
        }
        return ""
    }

    fun getArtist(): String {
        if (filePath != null) {
            val artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            if (artist != null) return artist
        }
        return "artist"
    }

    fun getAlbum(): String {
        if (filePath != null) {
            val album = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            if (album != null )return album
        }
        return "album"
    }

    fun getTitle(): String {
        if (filePath != null) {
            val title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            if (title != null) return title
        }
        return "title"
    }

//    fun release() {
//        metaRetriever.release()
//    }

}