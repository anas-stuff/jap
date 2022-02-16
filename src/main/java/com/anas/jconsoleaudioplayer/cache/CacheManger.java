package com.anas.jconsoleaudioplayer.cache;

import com.anas.jconsoleaudioplayer.player.Loop;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CacheManger {
    private final String basePath;
    private final ObjectMapper objectMapper;
    private final Settings settings;
    private final ResentPlayList resentPlayList;

    public CacheManger(String basePath) {
        this.basePath = basePath;
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        settings = loadSettingsFromCache();
        resentPlayList = loadResentPlayListFromCache();
    }

    private ResentPlayList loadResentPlayListFromCache() {
        File file = new File(basePath + "/resent_playlist.json");
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, ResentPlayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResentPlayList();
    }

    private Settings loadSettingsFromCache() {
        File file = new File(basePath + "/settings.json");
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Settings();
    }

    public void saveResentPath(String resentPath) {
        settings.setResentPath(resentPath);
    }

    public void savePlayList(PlayList playList) {
        if (playList != null && playList.getItems() != null) {
            if (playList.getItems().length > 0) {
                resentPlayList.setRecentPlayList(playList);
                return;
            }
        }
        resentPlayList.setRecentPlayList(null);
    }

        public void saveCurrentVolumeLevel(double volume) {
        settings.setResentVolume(volume);
    }

    public void saveCache() {
        File[] files = { new File(basePath + "/settings.json"),
                        new File(basePath + "/resent_playlist.json") };
        for (File file : files) {
            if (!file.exists())
                createCacheFile(file);
            try {
                switch (file.getName()) {
                    case "settings.json" -> saveSettings(file);
                    case "resent_playlist.json" -> saveResentPlayList(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveResentPlayList(File file) throws IOException {
        objectMapper.writeValue(file, resentPlayList);
    }

    private void saveSettings(File file) throws IOException {
        objectMapper.writeValue(file, settings);
    }

    private void createCacheFile(File file) {
        try {
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs(); // create parent directories if they don't exist
            file.createNewFile(); // create the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings getCache() {
        return settings;
    }

    public PlayList getResentPlayList() {
        if (resentPlayList.getRecentPlayList() != null) {
            return resentPlayList.getRecentPlayList();
        }
        return new PlayList();
    }

    public String getResentPath() {
        return settings.getResentPath();
    }

    public double getResentVolumeLevel() {
        return settings.getResentVolume() == -1 ? 0.5 : settings.getResentVolume();
    }

    public Loop getResentLoopOnTrack() {
        return settings.getLoopOnTrack();
    }

    public void saveLoopOnTrack(Loop loop) {
        settings.setLoopOnTrack(loop);
    }
}
