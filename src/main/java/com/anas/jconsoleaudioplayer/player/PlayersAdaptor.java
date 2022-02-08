package com.anas.jconsoleaudioplayer.player;

import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.userinterface.player.PlayerInterface;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;

public class PlayersAdaptor implements SuPlayer {
    private final Player[] players;
    private Player currentPlayer;
    private PlayList playList;
    private double soundVolume, soundVolumeBeforeMute;

    public PlayersAdaptor(PlayList playList, Player... players) {
        this.playList = playList;
        this.players = players;
        this.soundVolume = 0.5;
        this.currentPlayer = players[0];
        setAdapterOfAllPlayers();
    }

    private void setAdapterOfAllPlayers() {
        for (Player player : players) {
            player.setPlayersAdaptor(this);
        }
    }

    public void play() {
        if (players.length > 1) { // if there are more than one player
            for (Player player : players) { // Get the supported player for the current file
                if (player.isSupportedFile(playList.getItems()[playList.getCurrentIndex()].getFile())) {
                    currentPlayer = player;
                    break;
                }
            }
        }
        try {
            new Thread(() -> {
                try {
                    currentPlayer.play(playList.getCurrentTrack().getFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
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
        soundVolumeBeforeMute = soundVolume;
        soundVolume = 0;
    }

    @Override
    public double getVolume() {
        return soundVolume;
    }

    @Override
    public void setVolume(double volume) {
        this.soundVolume = volume;
        currentPlayer.setVolume(soundVolume);
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
     *
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


    public void event(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            playList.played();
            playList.getItems()[playList.getCurrentIndex()].setPlaying(false);
            next();
            PlayerInterface.getInstance().rePrint();
        }
    }
}
