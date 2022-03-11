package com.anas.jconsoleaudioplayer.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class PlayListLoader {
    private final ObjectMapper mapper;
    private static PlayListLoader instance;

    private PlayListLoader() {
        mapper = new ObjectMapper();
    }

    public static PlayListLoader getInstance() {
        if (instance == null) {
            instance = new PlayListLoader();
        }
        return instance;
    }

    public PlayList load(File playlistFile) {
        try {
            return mapper.readValue(playlistFile, PlayList.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
