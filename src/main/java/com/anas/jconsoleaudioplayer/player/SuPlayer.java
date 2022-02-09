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
     * Stop and close the player
     */
    void exit();
}
