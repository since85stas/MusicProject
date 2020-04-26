package stas.batura.musicproject.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stas.batura.musicproject.R;
import stas.batura.musicproject.repository.room.TrackKot;

public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH;
    int playlistId;

    // Constructor
    public SongsManager(String string, int playlistId) {
        this.playlistId = playlistId;
        MEDIA_PATH = string;
    }

    public SongsManager() {
        MEDIA_PATH = "/mnt/sdcard/Music/red elvises/The Best of Kick-Ass";
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public List<TrackKot> getPlayList() {
        File home = new File(MEDIA_PATH);

        File[] files = home.listFiles(new FileExtensionFilter());

        List<TrackKot> trackKot = new ArrayList<>();
        if ( files != null && files.length > 0) {
            for (File file : files) {
                String fileStr = file.toString();
                String title = file.getName().substring(0, (file.getName().length() - 4));
                String[] pathArray = file.toString().split("/");
                String album = pathArray[pathArray.length - 2];
                String artist = pathArray[pathArray.length - 3];
                int pos = fileStr.indexOf("sdcard/");
                String localPath = fileStr.substring(pos + "sdcard/".length());
                Uri uri = Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() +
                                       "/" + localPath));

                TrackKot rackKot  = new TrackKot(playlistId,
                        title,
                        artist,
                        album,
                        R.drawable.image266680,
                        uri,
                        (3 * 60 + 41) * 1000
                        );
                trackKot.add(rackKot);
            }
        }
        // return songs list array
        return trackKot;
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    static class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
