package com.anas.jconsoleaudioplayer.player;

import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * This is the super class for all players.
 */
public abstract class Player implements SuPlayer, Runnable {
    private PlayersAdaptor adaptor;
    private ArrayList<PositionListener> positionListeners;

    /**
     * The constructor
     * @param adaptor the players adaptor
     */
    public Player(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
        positionListeners = new ArrayList<>();
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
    public void sendEvent(PlayerEvent event) {
        adaptor.event(event);
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

    public PlayersAdaptor getPlayersAdaptor() {
        return adaptor;
    }


    /**
     * Set the players adaptor
     * @param adaptor the players adaptor
     */
    public void setPlayersAdaptor(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
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
    public abstract boolean isRunning();
}
