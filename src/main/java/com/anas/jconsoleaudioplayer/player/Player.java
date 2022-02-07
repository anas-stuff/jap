package com.anas.jconsoleaudioplayer.player;

import javax.sound.sampled.LineEvent;
import java.io.File;

public abstract class Player implements SuPlayer, Runnable {
    private final PlayersAdaptor adaptor;

    // Constructor
    public Player(PlayersAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public void sendEvent(LineEvent.Type type) {
        adaptor.event(type);
    }

    public abstract void play(File audioFile) throws Exception;

    @Override
    public void play() {

    }

    protected PlayersAdaptor getPlayersAdaptor() {
        return adaptor;
    }
}
