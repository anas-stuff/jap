package com.anas.jconsoleaudioplayer.players;

import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.userinterface.player.PlayerInterface;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player implements Runnable {
    private final PlayList playlist;
    private Clip clip;
    private AudioInputStream audioInputStream;
    private boolean isLooping, isMuted, paused, userStopped, running;
    private double soundLevel;

    /**
     * Constructor for Player
     *
     * @param playlist PlayList to play
     */
    public Player(PlayList playlist) {
        this.playlist = playlist;
        isLooping = false;
        soundLevel = 0.500;
        isMuted = false;
        paused = false;
        userStopped = false;
        running = false;
    }

    /**
     * Plays the song in the playlist
     *
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException              if file is not found
     */
    public void play() throws LineUnavailableException, IOException {
        if (!running) {
            try {
                audioInputStream = playlist.getAudioInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            userStopped = false;

            // Set the volume
            setVolume(soundLevel);

            clip.start();
            playlist.getItems()[playlist.getCurrentIndex()].setPlaying(true);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playlist.played();
                    playlist.getItems()[playlist.getCurrentIndex()].setPlaying(false);

                    if (!userStopped && !paused && !running || clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                        try {
                            next();
                            PlayerInterface.getInstance().rePrint();
                        } catch (LineUnavailableException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            running = true;
        }
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
        clip.setMicrosecondPosition(0L);
        clip.stop();
        clip.close();
        userStopped = true;
        running = false;
    }

    /**
     * Pauses the song
     */
    public void pause() {
        clip.stop();
        paused = true;
    }

    /**
     * Resumes the playing of the song
     */
    public void resume() {
        clip.start();
        paused = false;
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
     *
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException              if file is not found
     */
    public void next() throws LineUnavailableException, IOException {
        if (clip != null) {
            clip.close();
            playlist.next();
            running = false;
            if (!paused)
                play();
        }
    }

    /**
     * Change to the previous song in the playlist
     *
     * @throws LineUnavailableException if line is unavailable
     * @throws IOException              if file is not found
     */
    public void previous() throws LineUnavailableException, IOException {
        if (clip != null) {
            clip.close();
            playlist.previous();
            running = false;
            if (!paused)
                play();
        }
    }

    /**
     * Mute and unmute the song
     */
    public void mute() {
        if (isMuted) {
            setVolume(soundLevel);
            isMuted = false;
        } else {
            setVolume(0.0);
            isMuted = true;
        }
    }

    /**
     * Get the play list
     *
     * @return PlayList
     */
    public PlayList getPlayList() {
        return playlist;
    }

    /**
     * Get the current volume of the song
     *
     * @return the volume
     */
    public double getVolume() {
        return soundLevel;
    }

    /**
     * Set the volume of the song
     *
     * @param volume the volume of the song
     */
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

    /**
     * Stop and close the player
     */
    public void exit() {
        stop();
        clip.close();
    }
}
