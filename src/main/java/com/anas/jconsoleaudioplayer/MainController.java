package com.anas.jconsoleaudioplayer;

import com.anas.jconsoleaudioplayer.player.Extension;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.player.players.WAVPlayer;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.playlist.PlayListLoader;
import com.anas.jconsoleaudioplayer.userinterface.CLIManager;

import java.util.Scanner;

public class MainController {
    private final CLIManager cliManager;
    private final PlayList playList;
    private final PlayersAdaptor playersAdaptor;
    private String resentPath;
    private final Scanner scanner;

    public MainController() {
        this.cliManager = new CLIManager(this);
        this.playList = new PlayList();
        this.playersAdaptor = new PlayersAdaptor(getPlayList(),  new WAVPlayer());
        this.resentPath = null; // TODO: Get from cache
        this.scanner = new Scanner(System.in);

        start();
    }

    private void start() {
        playList.addAll(PlayListLoader.load());
        cliManager.showPlayerInterface(getPlayersAdaptor());
    }

    public void openFileBrowser() {
        cliManager.getFileBrowser().setExtensions(new Extension[]{Extension.WAV});
        playList.addAll(cliManager.openFileBrowser(resentPath));
    }

    public CLIManager getCliManager() {
        return cliManager;
    }

    public PlayersAdaptor getPlayersAdaptor() {
        return playersAdaptor;
    }

    public PlayList getPlayList() {
        return playList;
    }

    public String getResentPath() {
        return resentPath;
    }

    public Scanner getScanner() {
        scanner.reset();
        return scanner;
    }

    public void setResentPath(String resentPath) {
        this.resentPath = resentPath;
    }
}
