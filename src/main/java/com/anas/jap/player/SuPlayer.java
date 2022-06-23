package com.anas.jap.player;

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
     * Add a position listener to the player
     * @param positionListener  the position listener to be notified
     */
    void addPositionListener(PositionListener positionListener);

    /**
     * Remove a position listener from the player
     * @param positionListener  the position listener to be removed
     */
    void removePositionListener(PositionListener positionListener);

    /**
     * Stop and close the player
     */
    void exit();
}
