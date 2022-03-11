package com.anas.jconsoleaudioplayer;

public enum Extension {
    WAV,
    MP3,
    FLAC,
    MP4,
    JSON;

    public String getExtension() {
        return "." + this.name().toLowerCase();
    }

    public boolean fileHasThisExtension(String fileName) {
        return fileName.toLowerCase().endsWith(this.getExtension());
    }
}
