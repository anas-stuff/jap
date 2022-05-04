package com.anas.jconsoleaudioplayer.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track implements Serializable {
    private int index;
    private String filePath;
    private boolean played, playing;
    private int previousTrackIndex, nextTrackIndex;

    public Track(int index, String filePath) {
        this.index = index;
        this.filePath = filePath;
        played = false;
        playing = false;
        previousTrackIndex = -1;
        nextTrackIndex = -1;
    }

    public Track() {}

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public File getFile() {
        return new File(filePath);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getNextTrackIndex() {
        return nextTrackIndex;
    }

    public void setNextTrackIndex(int nextTrackIndex) {
        this.nextTrackIndex = nextTrackIndex;
    }

    public int getPreviousTrackIndex() {
        return previousTrackIndex;
    }

    public void setPreviousTrackIndex(int previousTrackIndex) {
        this.previousTrackIndex = previousTrackIndex;
    }
    @Override
    public String toString() {
        File file = new File(filePath);
        return index+1 + "| " + file.getName() + " - " + (file.getUsableSpace() / 1024 / 1024) + " MB";
    }
}
