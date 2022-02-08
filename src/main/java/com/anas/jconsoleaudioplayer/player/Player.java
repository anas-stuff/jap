package com.anas.jconsoleaudioplayer.player;

import javax.sound.sampled.LineEvent;
import java.io.File;

public abstract class Player implements SuPlayer, Runnable {
    private PlayersAdaptor adaptor;

    // Constructor
    public Player(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public abstract void play(File audioFile) throws Exception;
    public abstract Extension[] getSupportedExtensions();

    @Override
    @Deprecated
    public final void play() {}

    public void sendEvent(LineEvent event) {
        adaptor.event(event);
    }


    /**
     * Is supported file by the player
     * @param file the file to check
     * @return true if supported
     */
    public boolean isSupportedFile(File file) {
        for (Extension extension : getSupportedExtensions()) {
            if (file.getName().toUpperCase().endsWith(extension.name())) {
                return true;
            }
        }
        return false;
    }

    public PlayersAdaptor getPlayersAdaptor() {
        return adaptor;
    }

    public void setPlayersAdaptor(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
    }
}
