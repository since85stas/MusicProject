package stas.batura.musicproject.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.io.File;
import java.io.FileFilter;
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
//                Uri uri = getUriValue(fileStr);
                Uri uri = Uri.fromFile(new File(getUriValue(fileStr)
                        ));
                Long duration = dataInfo.getDuration();

                String year = dataInfo.getYear();
                int yearInt = 0;
                try {
                    if (!year.equals("")) {
                        yearInt = Integer.parseInt(year);
                    };
                }catch (Exception e) {
                    Log.d("songmanager", "getPlayList: " +e);
                }

                TrackKot rackKot = new TrackKot(playlistId,
                        title,
                        artist,
                        album,
                        R.drawable.image266680,
                        uri,
                        duration,
                        dataInfo.getBitrate(),
                        yearInt
                );
                trackKot.add(rackKot);
            }
        }
        // return songs list array
        return trackKot;
    }

    @VisibleForTesting
    private String getUriValue(String fileStr) {
        int pos = fileStr.indexOf("sdcard/");
        String localPath = fileStr.substring(pos + "sdcard/".length());

        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + localPath;
    }

    private void getTracksInSubs(File file) {
        if (file != null) {
            if (file.isFile()) {
                String ext = getFileExtension(file);
                if (ext.equals(".mp3")) {
                    files.add(file);
                }
            } else {
                File[] listFiles = file.listFiles(new FileExtensionFilter());
                if (listFiles != null) {
                    for (File fileIn : listFiles) {
                        files.add(fileIn);
                    }
                }
                File[] dirFiles = file.listFiles(new FileExtensionFilter());
                if (dirFiles != null) {
                    for (File fileDir : dirFiles
                    ) {
                        getTracksInSubs(fileDir);
                    }
                }
            }
        }
    }

    private static String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }

/**
 * Class to filter files which are having .mp3 extension
 */
static class FileExtensionFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3") || name.endsWith(".MP3"));
    }
}

static class FileDirFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
}
