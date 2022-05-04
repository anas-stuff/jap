package com.anas.jconsoleaudioplayer.player.players;

import com.anas.jconsoleaudioplayer.player.AudioPosition;
import com.anas.jconsoleaudioplayer.player.Extension;
import com.anas.jconsoleaudioplayer.player.Player;
import com.anas.jconsoleaudioplayer.player.PlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;

import java.io.File;
import java.util.Map;

public class MainAudioPlayer extends Player implements StreamPlayerListener {
    private final StreamPlayer streamPlayer;
    private double volume;
    private static MainAudioPlayer instance;

    private MainAudioPlayer() {
        super();
        streamPlayer = new StreamPlayer();
        volume = 0.5;
        this.streamPlayer.addStreamPlayerListener(this);
    }

    public static MainAudioPlayer getInstance() {
        if (instance == null) {
            instance = new MainAudioPlayer();
        }
        return instance;
    }

    @Override
    public void play(File audioFile) throws Exception {
        streamPlayer.open(audioFile);
        streamPlayer.play();
        streamPlayer.setGain(volume);
    }

    @Override
    public Extension[] getSupportedExtensions() {
        return new Extension[]{Extension.MP3, Extension.WAV, Extension.FLAC, Extension.OGG, Extension.APE, Extension.SPX};
    }

    @Override
    public boolean isPlaying() {
        return !streamPlayer.isStopped();
    }

    @Override
    public void seekTo(int seekSeconds) throws Exception {
        streamPlayer.seekSeconds(seekSeconds);
    }

    /**
     * @param seekSeconds the amount to seek in seconds
     * @throws Exception if the seek fails
     */
    @Override
    public void seekToSeconds(int seekSeconds) throws Exception {
        streamPlayer.seekSeconds(seekSeconds);
    }

    @Override
    public void stop() {
        streamPlayer.stop();
    }

    @Override
    public void pause() {
        streamPlayer.pause();
    }

    @Override
    public void resume() {
        streamPlayer.resume();
    }

    @Override
    public double getVolume() {
        return streamPlayer.getGainValue();
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        if (streamPlayer.isPausedOrPlaying()) {
            streamPlayer.setGain(volume);
        }
    }


    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {

    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
        notifyPositionListeners(new AudioPosition(streamPlayer.getDurationInSeconds(), microsecondPosition));
    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {
        switch (event.getPlayerStatus()) {
            case PLAYING -> super.notifyPlayerListeners(PlayerEvent.PLAYING);
            case PAUSED -> super.notifyPlayerListeners(PlayerEvent.PAUSED);
            case STOPPED -> super.notifyPlayerListeners(PlayerEvent.STOPPED);
            case SEEKED -> super.notifyPlayerListeners(PlayerEvent.SEEKED);
            case RESUMED -> super.notifyPlayerListeners(PlayerEvent.RESUMED);
            case EOM -> super.notifyPlayerListeners(PlayerEvent.END_OF_MEDIA);
            default -> super.notifyPlayerListeners(PlayerEvent.UNKNOWN);
        }
    }

    @Override
    public void exit() {
        this.stop();
    }

    @Override
    public void run() {
        /*try {
            streamPlayer.play();
        } catch (StreamPlayerException e) {
            throw new RuntimeException(e);
        }*/
    }
}
