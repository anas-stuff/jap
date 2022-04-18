package com.anas.jconsoleaudioplayer.player.players;

import com.anas.jconsoleaudioplayer.player.AudioPosition;
import com.anas.jconsoleaudioplayer.player.Extension;
import com.anas.jconsoleaudioplayer.player.Player;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;

import java.io.File;
import java.util.Map;

public class MainAudioPlayer extends Player {
    private final StreamPlayer streamPlayer;
    private static MainAudioPlayer instance;

    private MainAudioPlayer() {
        super(null);
        streamPlayer = new StreamPlayer();
        this.addStreamPlayerListener();
    }

    private void addStreamPlayerListener() {
        streamPlayer.addStreamPlayerListener(new StreamPlayerListener() {
            @Override
            public void opened(Object dataSource, Map<String, Object> properties) {

            }

            @Override
            public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
                notifyPositionListeners(new AudioPosition(streamPlayer.getDurationInSeconds(), microsecondPosition));
            }

            @Override
            public void statusUpdated(StreamPlayerEvent event) {

            }
        });
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
        return streamPlayer.isOpened() && streamPlayer.isPlaying();
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
        return streamPlayer.getGainValue() / 100;
    }

    @Override
    public void setVolume(double volume) {
        streamPlayer.setGain(volume);
    }



    @Override
    public void exit() {
        streamPlayer.stop();
    }

    @Override
    public void run() { }
}
