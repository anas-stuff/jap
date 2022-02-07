package com.anas.code.userinterface;


import com.anas.code.MainController;
import com.anas.code.players.Player;
import com.anas.code.playlist.Track;
import com.anas.code.playlist.PlayList;
import com.anas.code.userinterface.fileBrowser.FileBrowser;
import com.anas.code.userinterface.player.PlayerInterface;

public class CLIManager {
    private FileBrowser fileBrowser;
    private PlayerInterface playerInterface;

    public CLIManager(MainController mainController) {
        init();
        fileBrowser.setMainController(mainController);
        playerInterface.setMainController(mainController);
    }

    private void init() {
        this.fileBrowser = FileBrowser.getInstance();
        this.playerInterface = PlayerInterface.getInstance();
    }

    public Track[] openFileBrowser(String startingDirectory) {
        return this.fileBrowser.openBrowser(startingDirectory);
    }

    public void printPlayList(PlayList playList) {
        playList.print();
    }

    public void showPlayerInterface(Player player) {
        playerInterface.start(player);
    }

    public FileBrowser getFileBrowser() {
        return fileBrowser;
    }
}
