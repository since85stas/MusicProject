package stas.batura.musicproject.utils

import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.File

class MediaDataInfo {

    private var filePath: File? = null;

    constructor( file: File) {
        filePath = file
    }

    fun getDuration(): Long {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            var duration: String =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var dur: Long = duration.toLong()
            metaRetriever.release()
            return dur;
        }
        return 0L
    }

    fun getYear(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)

            val year = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)

            metaRetriever.release()
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
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)

            val year = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

            metaRetriever.release()
            return year
        }
        return ""
    }

    fun getArtist(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            val artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            if (artist != null) return artist
        }
        return "artist"
    }

    fun getAlbum(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            val album = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            if (album != null )return album
        }
        return "album"
    }

    fun getTitle(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            val title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            if (title != null) return title
        }
        return "title"
    }



}