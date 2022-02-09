package com.anas.jconsoleaudioplayer;

import com.anas.jconsoleaudioplayer.cache.CacheManger;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.playlist.PlayListLoader;
import com.anas.jconsoleaudioplayer.userinterface.CLIManager;

import java.util.Scanner;

public class MainController {
    private CLIManager cliManager;
    private CacheManger cacheManger;
    private PlayList playList;
    private PlayersAdaptor playersAdaptor;
    private String resentPath;
    private Scanner scanner;

    public MainController() {
        init();
        start();
    }

    private void init() {
        this.cliManager = new CLIManager(this);
        this.cacheManger = new CacheManger(".");
        this.playList = cacheManger.getResentPlayList();
        this.playersAdaptor = PlayersAdaptor.getInstance();
        this.resentPath = cacheManger.getResentPath();
        this.scanner = new Scanner(System.in);
    }

    private void start() {
        // set volume
        playersAdaptor.setVolume(cacheManger.getResentVolumeLevel());
        playersAdaptor.setLoopOnTrack(cacheManger.getResentLoopOnTrack());
        playersAdaptor.setPlayList(playList);
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

    public void exit() {
        save();
        close();
        System.exit(0);
    }

    private void close() {
        playersAdaptor.exit();
        scanner.close();
    }

    private void save() {
        cacheManger.saveResentPath(resentPath);
        cacheManger.savePlayList(playList);
        cacheManger.saveCurrentVolumeLevel(playersAdaptor.getVolume());
        cacheManger.saveLoopOnTrack(playersAdaptor.getLoopOnTrack());
        cacheManger.saveCache();
    }
}
