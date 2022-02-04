package com.anas.code.playlist;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class ListItem {
    private int index;
    private File file;
    private boolean played;

    public ListItem(int index, File file) {
        this.index = index;
        this.file = file;
        played = false;
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

    @Override
    public String toString() {
        return file.getName();
    }
}
