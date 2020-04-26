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

            val hasPicture = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE)

            var duration: String =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var dur: Long = duration.toLong()
            metaRetriever.release()
            return dur;
        }
        return 0L
    }

    fun getArtist(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        }
        return "artist"
    }

    fun getAlbum(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        }
        return "album"
    }

    fun getTitle(): String {
        if (filePath != null) {
            var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filePath!!.absolutePath)
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        }
        return "title"
    }

}