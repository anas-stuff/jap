package com.anas.code.userinterface;


import com.anas.code.playlist.ListItem;
import com.anas.code.playlist.PlayList;

public class CLIManager {
    private final FileBrowser fileBrowser;

    public CLIManager() {
        this.fileBrowser = FileBrowser.getInstance();
    }

    public ListItem[] openFileBrowser(String startingDirectory) {
        return this.fileBrowser.openBrowser(startingDirectory);
    }

    public void printPlayList(PlayList playList) {
        playList.print();
    }
}
