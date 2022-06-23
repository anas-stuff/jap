package com.anas.jap.cache;

import com.anas.jap.player.Loop;

public class Settings {
    private String resentPath;
    private double resentVolume;
    private Loop loopOnTrack;

    protected Settings() {
        this.resentPath = null;
        this.resentVolume = -1;
        this.loopOnTrack = Loop.NO_LOOP;
    }

    protected String getResentPath() {
        return resentPath;
    }

    protected void setResentPath(String resentPath) {
        this.resentPath = resentPath;
    }

    protected double getResentVolume() {
        return resentVolume;
    }

    protected void setResentVolume(double resentVolume) {
        this.resentVolume = resentVolume;
    }

    protected Loop getLoopOnTrack() {
        return loopOnTrack;
    }

    protected void setLoopOnTrack(Loop loopOnTrack) {
        this.loopOnTrack = loopOnTrack;
    }
}
