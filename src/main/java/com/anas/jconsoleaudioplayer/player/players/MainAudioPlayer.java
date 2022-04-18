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
import java.util.logging.LogManager;

public class MainAudioPlayer extends Player implements StreamPlayerListener {
    private final StreamPlayer streamPlayer;
    private static MainAudioPlayer instance;

    private MainAudioPlayer() {
        super(null);
        streamPlayer = new StreamPlayer();
        LogManager.getLogManager().reset(); // to remove the default console handler
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
    }

    @Override
    public Extension[] getSupportedExtensions() {
        return new Extension[]{Extension.MP3, Extension.WAV, Extension.FLAC, Extension.OGG, Extension.APE};
    }

    @Override
    public boolean isRunning() {
        return streamPlayer.isPlaying();
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
        streamPlayer.setGain(volume);
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
            case PLAYING -> super.sendEvent(PlayerEvent.PLAYING);
            case PAUSED -> super.sendEvent(PlayerEvent.PAUSED);
            case STOPPED -> super.sendEvent(PlayerEvent.STOPPED);
            case SEEKED -> super.sendEvent(PlayerEvent.SEEKED);
            case RESUMED -> super.sendEvent(PlayerEvent.RESUMED);
            case EOM -> super.sendEvent(PlayerEvent.END_OF_MEDIA);
            default -> super.sendEvent(PlayerEvent.UNKNOWN);
        }
    }

    @Override
    public void exit() {
        streamPlayer.stop();
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
