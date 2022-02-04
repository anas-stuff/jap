package com.anas.code;

import com.anas.code.playlist.PlayList;
import com.anas.code.userinterface.CLIManager;

public class Main {
    public static void main(String[] args) {
        CLIManager cliManager = new CLIManager();
        PlayList playList = new PlayList();
        playList.addAll(cliManager.openFileBrowser(null));
        cliManager.printPlayList(playList);
    }
}
