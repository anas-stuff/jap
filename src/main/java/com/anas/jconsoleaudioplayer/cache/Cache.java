package com.anas.jconsoleaudioplayer.cache;

import com.anas.jconsoleaudioplayer.playlist.PlayList;

public class Cache {
    private String resentPath;
    private PlayList resentPlayList;
    private double resentVolume;

    protected Cache() {
        this.resentPath = null;
        this.resentPlayList = null;
        this.resentVolume = -1;
    }

    protected String getResentPath() {
        return resentPath;
    }

    protected void setResentPath(String resentPath) {
        this.resentPath = resentPath;
    }

    protected PlayList getResentPlayList() {
        return resentPlayList;
    }

    protected void setResentPlayList(PlayList resentPlayList) {
        this.resentPlayList = resentPlayList;
    }

    protected double getResentVolume() {
        return resentVolume;
    }

    protected void setResentVolume(double resentVolume) {
        this.resentVolume = resentVolume;
    }
}
