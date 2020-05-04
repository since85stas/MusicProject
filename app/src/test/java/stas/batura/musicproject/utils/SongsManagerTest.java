package stas.batura.musicproject.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SongsManagerTest {

    SongsManager manager;

    @Before
    void init () {
        manager = new SongsManager();
    }

    @Test
    void getUriValues_correct_string() {
        String testStr ="/mnt/sdcard/Music/The Black Keys/The Black Keys - Turn Blue - [2014]/01 - Weight Of Love.mp3";

//        assertArrayEquals(  manager.getUriValue(testStr), "file:///storage/emulated/0/Music/The%20Black%20Keys/The%20Black%20Keys%20-%20Turn%20Blue%20-%20%5B2014%5D/01%20-%20Weight%20Of%20Love.mp3");
    }

}