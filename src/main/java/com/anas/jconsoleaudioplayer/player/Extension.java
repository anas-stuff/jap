package com.anas.jconsoleaudioplayer.player;

public enum Extension {
    WAV,
    MP3,
    FLAC,
    MP4;

    public String getExtension() {
        return "." + this.name().toLowerCase();
    }

    public boolean fileHasThisExtension(String fileName) {
        return fileName.toLowerCase().endsWith(this.getExtension());
    }
}
