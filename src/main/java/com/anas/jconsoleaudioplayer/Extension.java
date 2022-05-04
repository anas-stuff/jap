package com.anas.jconsoleaudioplayer;

public enum Extension {
    WAV,
    MP3,
    /**
     * <b>FLAC</b> (Free Lossless Audio Codec) is an audio coding format for lossless compression of digital audio,
     * developed by the Xiph.Org Foundation, and is also the name of the free software project producing the FLAC tools,
     * the reference software package that includes a codec implementation.
     * Digital audio compressed by FLAC's algorithm can
     * typically be reduced to between 50 and 70 percent of its original size and decompresses
     * to an identical copy of the original audio data.
     * <a href="https://en.wikipedia.org/wiki/FLAC">More info</a>
     */
    FLAC,
    OGG,
    /**
     * <b>Monkey's Audio</b> is an algorithm and file format for lossless audio data compression.
     * Lossless data compression does not discard data during the process of encoding,
     * unlike lossy compression methods such as AAC, MP3, Vorbis, and Opus.
     * Therefore, it may be decompressed to a file that is identical to the source material.
     * <a href="https://en.wikipedia.org/wiki/Monkey's_Audio">More info</a>
     */
    APE,
    /**
     * <b>Speex</b> is an audio compression codec specifically tuned for the reproduction of human speech
     * and also a free software speech codec that may be used on VoIP applications and podcasts.
     * It is based on the CELP speech coding algorithm.
     * <a href="https://en.wikipedia.org/wiki/Speex">More info</a>
     */
    SPX,

    /**
     * Play list
     */
    JSON;

    public String getExtension() {
        return "." + this.name().toLowerCase();
    }

    public boolean fileHasThisExtension(String fileName) {
        return fileName.toLowerCase().endsWith(this.getExtension());
    }
}
