package stas.batura.musicproject.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import stas.batura.musicproject.R;
import stas.batura.musicproject.repository.room.TrackKot;

public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH;
    int playlistId;
    List<File> files;

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

//        File[] files = home.listFiles(new FileExtensionFilter());
        files = new ArrayList<>();
        getTracksInSubs(home);

        List<TrackKot> trackKot = new ArrayList<>();
        if (files != null && files.size() > 0) {
            for (File file : files) {

                MediaDataInfo dataInfo = new MediaDataInfo(file);
                String fileStr = file.toString();

                String title = dataInfo.getTitle();
                String album = dataInfo.getAlbum();
                String artist = dataInfo.getArtist();
                int pos = fileStr.indexOf("sdcard/");
                String localPath = fileStr.substring(pos + "sdcard/".length());
                Uri uri = Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/" + localPath));

                Long duration = dataInfo.getDuration();

                TrackKot rackKot = new TrackKot(playlistId,
                        title,
                        artist,
                        album,
                        R.drawable.image266680,
                        uri,
                        duration
                );
                trackKot.add(rackKot);
            }
        }
        // return songs list array
        return trackKot;
    }

    private void getTracksInSubs(File file) {
        if (file != null) {
            for (File fileIn : file.listFiles(new FileExtensionFilter())) {
                File[] filesIn = fileIn.listFiles(new FileExtensionFilter());
                if (filesIn != null && filesIn.length > 0) {
                    for (File tracks : filesIn
                    ) {
                        files.add(tracks);
                    }
                }
                if (fileIn.isDirectory()) {
                    getTracksInSubs(fileIn);
                } else if (fileIn.isFile()) {
                    files.add(fileIn);
                }
            }
        }
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
