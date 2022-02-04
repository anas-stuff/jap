package com.anas.code.userinterface;


import com.anas.code.players.Player;
import com.anas.code.playlist.ListItem;
import com.anas.code.playlist.PlayList;

import java.util.Scanner;

public class CLIManager {
    private final FileBrowser fileBrowser;
    private final PlayerInterface playerInterface;
    private final Scanner scanner;

    public CLIManager(Scanner scanner) {
        this.fileBrowser = FileBrowser.getInstance();
        fileBrowser.setScanner(scanner);
        this.playerInterface = PlayerInterface.getInstance();
        playerInterface.setScanner(scanner);
        this.scanner = scanner;
    }

    public ListItem[] openFileBrowser(String startingDirectory) {
        return this.fileBrowser.openBrowser(startingDirectory);
    }

    public void printPlayList(PlayList playList) {
        playList.print();
    }

    public void showPlayerInterface(Player player) {
        playerInterface.start(player);
    }
}
