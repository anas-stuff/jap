package com.anas.jconsoleaudioplayer.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class PlayListHelper {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    public static PlayList load(File playlistFile) {
        try {
            return mapper.readValue(playlistFile, PlayList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void export(PlayList playlist, String playlistPath) {
        try {
            mapper.writeValue(new File(playlistPath + ".json"), playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAllToPlayList(PlayList targetPlayList, File[] files) {
        for (File file : files) {
            Track track = new Track(0, file);
            targetPlayList.add(track);
        }
    }

}
