package com.anas.jconsoleaudioplayer;

import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
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
        this.playersAdaptor = PlayersAdaptor.getInstance();
        this.resentPath = null; // TODO: Get from cache
        this.scanner = new Scanner(System.in);

        start();
    }

    private void start() {
        playersAdaptor.setPlayList(playList);
        playList.addAll(PlayListLoader.load());
        cliManager.showPlayerInterface(getPlayersAdaptor());
    }

    public void openFileBrowser() {
        cliManager.getFileBrowser().setExtensions(playersAdaptor.getSupportedExtensions());
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
