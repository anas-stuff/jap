package com.anas.jconsoleaudioplayer.playlist;

import java.io.File;

public class PlayListHelper {
    public static void addAllToPlayList(PlayList targetPlayList, File[] files) {
        for (File file : files) {
           Track track = new Track(0, file);
            targetPlayList.add(track);
        }
    }
}
