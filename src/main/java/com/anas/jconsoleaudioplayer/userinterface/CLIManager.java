package com.anas.jconsoleaudioplayer.userinterface;


import com.anas.jconsoleaudioplayer.MainController;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.playlist.Track;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.userinterface.fileBrowser.FileBrowser;
import com.anas.jconsoleaudioplayer.userinterface.player.PlayerInterface;

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

    public void showPlayerInterface(PlayersAdaptor playersAdaptor) {
        playerInterface.start(playersAdaptor);
    }

    public FileBrowser getFileBrowser() {
        return fileBrowser;
    }
}
