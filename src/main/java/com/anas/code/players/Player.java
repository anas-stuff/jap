package com.anas.code.players;

import com.anas.code.playlist.PlayList;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player implements Runnable {
    private final PlayList playlist;
    private final Clip clip;
    private AudioInputStream audioInputStream;
    private boolean isLooping;
    private float soundLevel;
    private boolean isMuted;
    private final boolean paused;

    /**
     * Constructor for Player
     * @param playlist PlayList to play
     * @throws LineUnavailableException if line is unavailable
     */
    public Player(PlayList playlist) throws LineUnavailableException {
        this.playlist = playlist;
        clip = AudioSystem.getClip();
        isLooping = false;
        soundLevel = 2.0f;
        isMuted = false;
        paused = false;
    }

    /**
     * Plays the song in the playlist
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException if file is not found
     */
    public void play() throws LineUnavailableException, IOException {
        try {
            audioInputStream = playlist.getAudioInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!clip.isOpen()) {
            try {
                clip.open(audioInputStream);
            } catch (IllegalStateException e) {
                System.err.println("Clip is already open");
            }
        }
        clip.start();
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                try {
                    next();
                } catch (LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * Stops the song
     */
    public void stop() {
        clip.setFramePosition(0);
    }

    /**
     * Pauses the song
     */
    public void pause() {
        clip.start();
    }

    /**
     * Resumes the playing of the song
     */
    public void resume() {
        clip.start();
    }

    /**
     * Enable and disable looping of the song
     */
    public void loop() {
        if (isLooping) {
            clip.loop(0); // stop looping
            isLooping = false;
        } else {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // start looping
            isLooping = true;
        }
    }

    /**
     * Enable and disable looping of the play list
     */
    public void loopOfPlayList() {
        playlist.setLooping(!playlist.isLooping()); // toggle looping
    }

    /**
     * Enable and disable the shuffle mode
     */
    public void shuffle() {
        playlist.setShuffling(!playlist.isShuffling()); // toggle shuffling
    }

    /**
     * Change to the next song in the playlist
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException if file is not found
     */
    public void next() throws LineUnavailableException, IOException {
        clip.close();
        playlist.next();
        play();
    }

    /**
     * Change to the previous song in the playlist
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException if file is not found
     */
    public void previous() throws LineUnavailableException, IOException {
        clip.close();
        playlist.previous();
        play();
    }

    /**
     * Mute and unmute the song
     */
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

    /**
     * Get the play list
     * @return PlayList
     */
    public PlayList getPlayList() {
        return playlist;
    }

    /**
     * Get the current volume of the song
     * @return the volume
     */
    public float getVolume() {
        return soundLevel;
    }

    /**
     * Set the volume of the song
     * @param volume the volume of the song
     */
    public void setVolume(float volume) {
        if (volume < 0 || volume > 6.02f) {
            System.out.println("Volume must be between 0 and 6.02, volume = " + volume);
            return;
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        soundLevel = gainControl.getValue();
    }

    /**
     * Stop and close the player
     */
    public void exit() {
        stop();
        clip.close();
    }
}
