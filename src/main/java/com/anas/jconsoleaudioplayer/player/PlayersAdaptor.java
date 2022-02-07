package com.anas.jconsoleaudioplayer.player;

import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.userinterface.player.PlayerInterface;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;

public class PlayersAdaptor implements SuPlayer {
    private final Player[] players;
    private Player currentPlayer;
    private PlayList playList;

    public PlayersAdaptor(PlayList playList, Player... players) {
        this.playList = playList;
        this.players = players;
    }

    public void play() {
        currentPlayer = players[0];
        for (Player player : players) {
            if (player.isSupportedFile(playList.getItems()[playList.getCurrentIndex()].getFile())) {
                currentPlayer = player;
                break;
            }
        }
        try {
            currentPlayer.play(playList.getCurrentTrack().getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        currentPlayer.stop();
    }

    @Override
    public void pause() {
        currentPlayer.pause();
    }

    @Override
    public void resume() {
        currentPlayer.resume();
    }

    @Override
    public void loop() {
        currentPlayer.loop();
    }

    /**
     * Enable and disable looping of the play list
     */
    public void loopOfPlayList() {
        playList.setLooping(!playList.isLooping()); // toggle looping
    }

    /**
     * Enable and disable looping of the current player
     */
    public void shuffle() {
        playList.setShuffling(!playList.isShuffling()); // toggle shuffling
    }

    /**
     * Change to the next song in the playlist
     */
    public void next() {
        currentPlayer.stop();
        playList.next();
        this.play();
    }

    /**
     * Change to the previous song in the playlist
     */
    public void previous() {
        currentPlayer.stop();
        playList.previous();
        this.play();
    }

    @Override
    public void mute() {
        currentPlayer.mute();
    }

    @Override
    public double getVolume() {
        return 0;
    }

    @Override
    public void setVolume(double volume) {
        currentPlayer.setVolume(volume);
    }

    @Deprecated
    @Override
    public boolean isSupportedFile(File file) {
        return false;
    }

    @Override
    public void exit() {
        currentPlayer.exit();
    }

    /**
     * Get the play list
     * @return PlayList
     */
    public PlayList getPlayList() {
        return playList;
    }

    public void setPlayList(PlayList playList) {
        this.playList = playList;
    }

    /**
     * Get the current player
     *
     * @return Player
     */
    public SuPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void event(LineEvent.Type type) {
        if (type == LineEvent.Type.STOP) {
            playList.played();
            playList.getItems()[playList.getCurrentIndex()].setPlaying(false);
            next();
            PlayerInterface.getInstance().rePrint();
        }
    }
}
