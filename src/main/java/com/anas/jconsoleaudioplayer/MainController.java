package com.anas.jconsoleaudioplayer;

import com.anas.jconsoleaudioplayer.cache.CacheManger;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.playlist.PlayListHelper;
import com.anas.jconsoleaudioplayer.playlist.PlayListsManger;
import com.anas.jconsoleaudioplayer.userinterface.CLIManager;

import java.util.Scanner;
import java.util.logging.LogManager;

public class MainController {
    private CLIManager cliManager;
    private CacheManger cacheManger;
    private PlayersAdaptor playersAdaptor;
    private String resentPath;
    private Scanner scanner;

    public MainController() {
        LogManager.getLogManager().reset(); // to remove the default console handler
        init();
        start();
    }

    private void init() {
        this.cliManager = new CLIManager(this);
        this.cacheManger = new CacheManger("./.cache");
        this.playersAdaptor = PlayersAdaptor.getInstance();
        this.resentPath = cacheManger.getResentPath();
        this.scanner = new Scanner(System.in);
    }

    private void start() {
        // set volume
        playersAdaptor.setVolume(cacheManger.getRecentVolumeLevel());
        playersAdaptor.setLoopOnTrack(cacheManger.getRecentLoopOnTrack());
        cliManager.showPlayerInterface(getPlayersAdaptor());
    }

    public void openFileBrowser() {
        PlayListHelper.addAllToPlayList(
                PlayListsManger.getInstance().getCurrentPlayList(),
                cliManager.openFileBrowser(playersAdaptor.getSupportedExtensions(), resentPath)
        );
    }

    public CLIManager getCliManager() {
        return cliManager;
    }

    public PlayersAdaptor getPlayersAdaptor() {
        return playersAdaptor;
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
        cacheManger.saveCurrentVolumeLevel(playersAdaptor.getVolume());
        cacheManger.saveLoopOnTrack(playersAdaptor.getLoopOnTrack());
        cacheManger.saveCache();
    }
}
