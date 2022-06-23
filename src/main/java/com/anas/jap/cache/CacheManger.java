package com.anas.jap.cache;

import com.anas.jap.player.Loop;
import com.anas.jap.playlist.PlayListsManger;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class CacheManger {
    private final String basePath;
    private final ObjectMapper objectMapper;
    private final Settings settings;

    public CacheManger(String basePath) {
        this.basePath = basePath;
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        settings = loadSettingsFromCache();
        loadPlayListsManager();
    }

    private void loadPlayListsManager() {
        try {
            FileInputStream fileInputStream = new FileInputStream(basePath + "/play_lists_manger.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            PlayListsManger.setInstance((PlayListsManger) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            PlayListsManger.getInstance(); // create new instance if ser file not found
        }
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


    public void saveCurrentVolumeLevel(double volume) {
        settings.setResentVolume(volume);
    }

    public void saveCache() {
        File[] files = { new File(basePath + "/settings.json"),
                        new File(basePath + "/play_lists_manger.ser") };
        for (File file : files) {
            if (!file.exists())
                createCacheFile(file);
            try {
                switch (file.getName()) {
                    case "settings.json" -> saveSettings(file);
                    case "play_lists_manger.ser" -> savePlayListsManger(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlayListsManger(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(PlayListsManger.getInstance());
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getResentPath() {
        return settings.getResentPath();
    }

    public double getRecentVolumeLevel() {
        return settings.getResentVolume() == -1 ? 0.5 : settings.getResentVolume();
    }

    public Loop getRecentLoopOnTrack() {
        return settings.getLoopOnTrack();
    }

    public void saveLoopOnTrack(Loop loop) {
        settings.setLoopOnTrack(loop);
    }

    public void saveResentPath(String resentPath) {
        settings.setResentPath(resentPath);
    }
}
