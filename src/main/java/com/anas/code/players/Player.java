package com.anas.code.players;

import com.anas.code.playlist.PlayList;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player implements Runnable {
    private final PlayList playlist;
    private AudioInputStream audioInputStream;
    private final Clip clip;
    private boolean isLooping;
    private float soundLevel;
    private boolean isMuted;

    public Player(PlayList playlist) throws LineUnavailableException {
        this.playlist = playlist;
        clip = AudioSystem.getClip();
        isLooping = false;
        soundLevel = 2.0f;
        isMuted = false;
    }

    public void play() throws LineUnavailableException, IOException {
        try {
            audioInputStream = playlist.getAudioInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!clip.isOpen()) {
            clip.open(audioInputStream);
        }
        clip.start();
        playlist.played();
    }

    @Override
    public void run() {
        try {
            play();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.setFramePosition(0);
    }

    public void pause() {
        if (clip.isRunning()) {
            clip.stop();
        } else {
            clip.start();
        }
    }

    public void resume() {
        clip.start();
    }

    public void loop()  {
        if (isLooping) {
            clip.loop(0); // stop looping
            isLooping = false;
        } else {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // start looping
            isLooping = true;
        }
    }

    public void loopOfPlayList() {
        playlist.setLooping(!playlist.isLooping()); // toggle looping
    }

    public void shuffle() {
        playlist.setShuffling(!playlist.isShuffling()); // toggle shuffling
    }

    public void next() throws LineUnavailableException, IOException {
        stop();
        clip.close();
        playlist.next();
        play();
    }

    public void previous() throws LineUnavailableException, IOException {
        stop();
        clip.close();
        playlist.previous();
        play();
    }

    public void setVolume(float volume) {
        if (volume < 0 || volume > 6.02f) {
            System.out.println("Volume must be between 0 and 6.02, volume = " + volume);
            return;
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        soundLevel = gainControl.getValue();
    }

    public void mute() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (isMuted) {
            gainControl.setValue(soundLevel);
            isMuted = false;
        } else {
            gainControl.setValue(0);
            isMuted = true;
        }
    }

    public PlayList getPlayList() {
        return playlist;
    }

    public float getVolume() {
        return soundLevel;
    }

    public void exit() {
        stop();
        clip.close();
    }
}
