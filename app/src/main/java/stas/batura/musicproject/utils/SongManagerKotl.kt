package stas.batura.musicproject.utils

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Deferred
import stas.batura.musicproject.repository.room.TrackKot
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.util.*

class SongsManagerKotl {
    // SDCard Path
    val MEDIA_PATH: String
    var playlistId = 0
    var files: TreeSet<File>? = null
    var imageFiles: MutableList<File>? = null

    // Constructor
    constructor(string: String, playlistId: Int) {
        this.playlistId = playlistId
        MEDIA_PATH = string
    }

    constructor() {
        MEDIA_PATH = "/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"
    }

    val playlistName: String
        get() {
            val home = File(MEDIA_PATH)
            return home.name
        }//TODO: check finding matches
    //                dataInfo.release();
    // return songs list array
//        File[] files = home.listFiles(new FileExtensionFilter());

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    suspend fun getPlayList(): Deferred<List<TrackKot>>
         {
            val home = File(MEDIA_PATH)

            //        File[] files = home.listFiles(new FileExtensionFilter());
            files = TreeSet()
//            val imageFiles = ArrayList()
            getTracksInSubs(home)
            val time = System.currentTimeMillis()
            val trackKot: MutableList<TrackKot> = ArrayList()
            val mediaMetadataRetriever = MediaMetadataRetriever()
            if (files != null && files!!.size > 0) {
                for (file in files!!) {
                    val dataInfo = MediaDataInfo(file, mediaMetadataRetriever)
                    val fileStr = file.toString()
                    val nextT = time - System.currentTimeMillis()
                    val title = dataInfo.getTitle()
                    val album = dataInfo.getAlbum()
                    val artist = dataInfo.getArtist()
                    val uri = Uri.fromFile(
                        File(
                            getUriValue(fileStr)
                        )
                    )
                    val duration = dataInfo.getDuration()
                    val year = dataInfo.getYear()
                    var yearInt = 0
                    try {
                        if (year != "") {
                            yearInt = year.toInt()
                        }
                    } catch (e: Exception) {
                        Log.d("songmanager", "getPlayList: $e")
                    }
                    var imageUri: Uri? = null
                    //TODO: check finding matches
                    for (imgFile in imageFiles!!) {
                        try {
                            val parent = file.parentFile
                            if (imgFile.parentFile.path == parent.path) {
                                imageUri =
                                    Uri.fromFile(File(getUriValue(imgFile.toString())))
                            }
                        } catch (e: NullPointerException) {
                            Log.d("SongsMan", e.toString())
                        }
                    }
                    val rackKot = TrackKot(
                        playlistId,
                        title,
                        artist,
                        album,
                        imageUri,
                        uri,
                        duration,
                        dataInfo.getBitrate(),
                        yearInt
                    )
                    trackKot.add(rackKot)
                    //                dataInfo.release();
                }
            }
            mediaMetadataRetriever.release()
            val nextT = time - System.currentTimeMillis()
            // return songs list array
            return trackKot
        }

    @VisibleForTesting
    private fun getUriValue(fileStr: String): String {
        val pos = fileStr.indexOf("sdcard/")
        val localPath = fileStr.substring(pos + "sdcard/".length)
        return Environment.getExternalStorageDirectory().absolutePath +
                "/" + localPath
    }

    private fun getTracksInSubs(file: File?) {
        if (file != null) {
            if (file.isFile) {
                val ext = getFileExtension(file)
                if (ext == ".mp3") {
                    files!!.add(file)
                }
            } else {
                val listFiles =
                    file.listFiles(FileTracksFilter())
                val listImages =
                    file.listFiles(FilePictureFilter())
                if (listFiles != null) {
                    for (fileIn in listFiles) {
                        files!!.add(fileIn)
                    }
                }
                if (listImages != null) {
                    for (im in listImages) {
                        imageFiles!!.add(im)
                    }
                }
                val dirFiles =
                    file.listFiles(FileDirFilter())
                if (dirFiles != null) {
                    for (fileDir in dirFiles) {
                        getTracksInSubs(fileDir)
                    }
                }
            }
        }
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    internal class FileTracksFilter : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            return name.endsWith(".mp3") || name.endsWith(".MP3")
        }
    }

    internal class FileDirFilter : FileFilter {
        override fun accept(pathname: File): Boolean {
            return pathname.isDirectory
        }
    }

    internal class FilePictureFilter : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            return (name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".jpeg")
                    || name.endsWith(".JPEG") || name.endsWith(".jpg") || name.endsWith(".JPG"))
        }
    }

    companion object {
        private fun getFileExtension(file: File?): String {
            var extension = ""
            try {
                if (file != null && file.exists()) {
                    val name = file.name
                    extension = name.substring(name.lastIndexOf("."))
                }
            } catch (e: Exception) {
                extension = ""
            }
            return extension
        }
    }
}