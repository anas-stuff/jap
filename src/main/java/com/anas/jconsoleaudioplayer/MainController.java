package com.anas.jconsoleaudioplayer;

import com.anas.jconsoleaudioplayer.players.Extension;
import com.anas.jconsoleaudioplayer.players.Player;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.playlist.PlayListLoader;
import com.anas.jconsoleaudioplayer.userinterface.CLIManager;

import java.util.Scanner;

public class MainController {
    private final CLIManager cliManager;
    private final PlayList playList;
    private final Player player;
    private String resentPath;
    private final Scanner scanner;

    public MainController() {
        this.cliManager = new CLIManager(this);
        this.playList = new PlayList();
        this.player = new Player(getPlayList());
        this.resentPath = "D:\\Music";
        this.scanner = new Scanner(System.in);

        start();
    }

    private void start() {
        playList.addAll(PlayListLoader.load());
        cliManager.showPlayerInterface(getPlayer());
    }

    public void openFileBrowser() {
        cliManager.getFileBrowser().setExtensions(new Extension[]{Extension.WAV});
        playList.addAll(cliManager.openFileBrowser(resentPath));
    }

    public CLIManager getCliManager() {
        return cliManager;
    }

    public Player getPlayer() {
        return player;
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