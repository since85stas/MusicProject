package stas.batura.musicproject.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import stas.batura.musicproject.R;
import stas.batura.musicproject.repository.room.TrackKot;



public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH;
    int playlistId;
    List<File> files;
    List<File> imageFiles;

    // Constructor
    public SongsManager(String string, int playlistId) {
        this.playlistId = playlistId;
        MEDIA_PATH = string;
    }

    public SongsManager() {
        MEDIA_PATH = "/mnt/sdcard/Music/red elvises/The Best of Kick-Ass";
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

    public String getPlaylistName() {
        File home = new File(MEDIA_PATH);

        String name = home.getName();

        return name;
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public List<TrackKot> getPlayList() {
        File home = new File(MEDIA_PATH);

//        File[] files = home.listFiles(new FileExtensionFilter());
        files = new ArrayList<>();
        imageFiles = new ArrayList<>();
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

                Uri imageUri = null;
                //TODO: check finding matches
                for (File imgFile: imageFiles) {
                    try {
                        File parent = file.getParentFile();
                        if (imgFile.getParentFile().getPath().equals(parent.getPath())) {
                            imageUri = Uri.fromFile(new File(getUriValue(imgFile.toString())));
                        }
                    } catch (NullPointerException e) {
                        Log.d("SongsMan", e.toString());
                    }
                }

                TrackKot rackKot = new TrackKot(playlistId,
                        title,
                        artist,
                        album,
                        imageUri,
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
                File[] listFiles = file.listFiles(new FileTracksFilter());
                File[] listImages = file.listFiles(new FilePictureFilter());
                if (listFiles != null) {
                    for (File fileIn : listFiles) {
                        files.add(fileIn);
                    }
                }
                if (listImages != null) {
                    for (File im: listImages) {
                        imageFiles.add(im);
                    }
                }
                File[] dirFiles = file.listFiles(new FileDirFilter());
                if (dirFiles != null) {
                    for (File fileDir : dirFiles
                    ) {
                        getTracksInSubs(fileDir);
                    }
                }
            }
        }
    }

/**
 * Class to filter files which are having .mp3 extension
 */
static class FileTracksFilter implements FilenameFilter {
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

    static class FilePictureFilter implements FilenameFilter  {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".jpeg")
                    ||name.endsWith(".JPEG") || name.endsWith(".jpg") || name.endsWith(".JPG"));
        }
    }

}



