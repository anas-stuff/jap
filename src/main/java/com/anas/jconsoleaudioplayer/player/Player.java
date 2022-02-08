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

    @Override
    public void play() {

    }

    public void sendEvent(LineEvent event) {
        adaptor.event(event);
    }

    public PlayersAdaptor getPlayersAdaptor() {
        return adaptor;
    }

    public void setPlayersAdaptor(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
    }
}
