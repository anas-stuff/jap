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
    private final Cache cache;

    public CacheManger(String basePath) {
        this.basePath = basePath;
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        cache = loadCache();
    }

    private Cache loadCache() {
        File file = new File(basePath + "/cache.json");
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, Cache.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Cache();
    }

    public void saveResentPath(String resentPath) {
        cache.setResentPath(resentPath);
    }

    public void savePlayList(PlayList playList) {
        cache.setResentPlayList(playList);
    }

    public void saveCurrentVolumeLevel(double volume) {
        cache.setResentVolume(volume);
    }

    public void saveCache() {
        File file = new File(basePath + "/cache.json");
        if (!file.exists())
            createCacheFile(file);
        try {
            objectMapper.writeValue(file, cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCacheFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cache getCache() {
        return cache;
    }

    public PlayList getResentPlayList() {
        if (cache.getResentPlayList() != null) {
            return cache.getResentPlayList();
        }
        return new PlayList();
    }

    public String getResentPath() {
        return cache.getResentPath();
    }

    public double getResentVolumeLevel() {
        return cache.getResentVolume() == -1 ? 0.5 : cache.getResentVolume();
    }

    public Loop getResentLoopOnTrack() {
        return cache.getLoopOnTrack();
    }

    public void saveLoopOnTrack(Loop loop) {
        cache.setLoopOnTrack(loop);
    }
}
