package com.anas.code.playlist;

import java.io.File;

public class Track {
    private int index;
    private File file;
    private boolean played, playing;
    private Track previousTrack, nextTrack;

    public Track(int index, File file) {
        this.index = index;
        this.file = file;
        played = false;
        playing = false;
        previousTrack = null;
        nextTrack = null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return file.getName();
    }

    public String getFilePath() {
        return file.getPath();
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

    public Track getNextTrack() {
        return nextTrack;
    }

    public void setNextTrack(Track nextTrack) {
        this.nextTrack = nextTrack;
    }

    public Track getPreviousTrack() {
        return previousTrack;
    }

    public void setPreviousTrack(Track previousTrack) {
        this.previousTrack = previousTrack;
    }
    @Override
    public String toString() {
        return index + "| " + file.getName() + " - " + (file.getUsableSpace() / 1024 / 1024) + " MB";
    }
}
