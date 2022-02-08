package com.anas.jconsoleaudioplayer.player;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;

public interface SuPlayer {
    /**
     * Plays the song in the playlis
     */
     void play();


    /**
     * Stops the song
     */
     void stop();

    /**
     * Pauses the song
     */
     void pause();

    /**
     * Resumes the playing of the song
     */
     void resume();

    /**
     * Enable and disable looping of the song
     */
     void loop();

    /**
     * Mute and unmute the song
     */
    void mute();

    /**
     * Get the current volume of the song
     * @return the volume
     */
    double getVolume();

    /**
     * Set the volume of the song
     * @param volume the volume of the song
     */
    void setVolume(double volume);

    /**
     * Is supported file by the player
     * @param file the file to check
     * @return true if supported
     */
    boolean isSupportedFile(File file);

    /**
     * Stop and close the player
     */
    void exit();
}