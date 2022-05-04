package com.anas.jconsoleaudioplayer.player;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the super class for all players.
 */
public abstract class Player implements SuPlayer, Runnable {
    private final ArrayList<PositionListener> positionListeners;
    private final ArrayList<PlayerListener> playerListeners;

    /**
     * The default constructor
     */
    public Player() {
        positionListeners = new ArrayList<>();
        playerListeners = new ArrayList<>();
    }

    /**
     *  Play the audio file
     * @param audioFile the audio file to play
     * @throws Exception if you can't play the file
     */
    public abstract void play(File audioFile) throws Exception;
    public abstract Extension[] getSupportedExtensions();

    @Override
    @Deprecated
    public final void play() {}

    /**
     * Send event to the players adaptor to notify that the player is ended playing
     * @param event the event
     */
    public void notifyPlayerListeners(PlayerEvent event) {
        for (PlayerListener listener : playerListeners) {
            listener.onPlayerEvent(event);
        }
    }

    public void addPlayerListener(PlayerListener listener) {
        playerListeners.add(listener);
    }

    public void removePlayerListener(PlayerListener listener) {
        playerListeners.remove(listener);
    }


    /**
     * Is supported file by the player
     * @param file the file to check
     * @return true if supported
     */
    public boolean isSupportedFile(File file) {
        for (Extension extension : getSupportedExtensions()) {
            if (extension.fileHasThisExtension(file.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addPositionListener(PositionListener listener) {
        positionListeners.add(listener);
    }

    @Override
    public void removePositionListener(PositionListener listener) {
        positionListeners.remove(listener);
    }

    /**
     * Notify the position listeners
     * @param position the position
     */
    protected void notifyPositionListeners(AudioPosition position) {
        for (PositionListener listener : positionListeners) {
            listener.onPositionChanged(position);
        }
    }

    /**
     *  Player is running
     * @return true if running false otherwise
     */
    public abstract boolean isPlaying();

    /**
     * Seek to the position
     * @param seekSeconds the amount to seek in seconds, can not be negative
     * @throws Exception if you can't seek
     */
    public abstract void seekTo(int seekSeconds) throws Exception;

    /**
     * Seek to the position in the current track in seconds, can be negative
     * @param seekSeconds the amount to seek in seconds
     * @throws Exception if you can't seek
     */
    public abstract void seekToSeconds(int seekSeconds) throws Exception;
}
