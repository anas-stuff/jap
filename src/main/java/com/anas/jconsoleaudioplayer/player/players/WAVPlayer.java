package com.anas.jconsoleaudioplayer.player.players;

import com.anas.jconsoleaudioplayer.player.Extension;
import com.anas.jconsoleaudioplayer.player.Player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class WAVPlayer extends Player {
    private Clip clip;
    private AudioInputStream audioInputStream;
    private boolean paused, userStopped, running;
    private double soundLevel;

    // Singleton instance
    private static WAVPlayer instance;

    public static WAVPlayer getInstance() {
        if (instance == null) {
            instance = new WAVPlayer();
        }
        return instance;
    }
    /**
     * Constructor for WavePlayer
     */
    private WAVPlayer() {
        super(null);
        soundLevel = 0.500;
        paused = false;
        userStopped = false;
        running = false;
    }

    /**
     * Plays the song in the playlist
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException              if file is not found
     */
    @Override
    public void play(File audioFile) throws Exception {
        if (!running) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            userStopped = false;

            // Set the volume
            setVolume(soundLevel);

            clip.start();
            running = true;
            clip.addLineListener(event -> {
                if (!paused && !userStopped || clip.getFramePosition() == clip.getFrameLength()) {
                    sendEvent(event);
                    running = false;
                }
            });
        }
    }

    @Override
    public Extension[] getSupportedExtensions() {
        return new Extension[]{Extension.WAV};
    }

    @Override
    public void run() {
//        try {
//            play();
//        } catch (LineUnavailableException | IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Stops the song
     */
    @Override
    public void stop() {
        clip.setMicrosecondPosition(0L);
        clip.stop();
        clip.close();
        userStopped = true;
        running = false;
    }

    /**
     * Pauses the song
     */
    @Override
    public void pause() {
        clip.stop();
        paused = true;
    }

    /**
     * Resumes the playing of the song
     */
    @Override
    public void resume() {
        clip.start();
        paused = false;
    }

    /**
     * Get the current volume of the song
     * @return the volume
     */
    @Override
    public double getVolume() {
        return soundLevel;
    }

    /**
     * Set the volume of the song
     * @param volume the volume of the song
     */
    @Override
    public void setVolume(double volume) {
        if (volume < 0.0 || volume > 1.0) {
            System.out.println("Volume must be between 0 and 1, volume = " + volume);
            return;
        }
        soundLevel = volume;
        if (clip != null) {
            userStopped = true;
            if (!paused)
                clip.stop();
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float db = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(db);
            if (!paused)
                clip.start();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Stop and close the player
     */
    @Override
    public void exit() {
        if (clip != null) {
            stop();
            clip.close();
        }
    }
}
