package com.anas.jconsoleaudioplayer.userinterface;


import com.anas.jconsoleaudioplayer.MainController;
import com.anas.jconsoleaudioplayer.Extension;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.playlist.Track;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.userinterface.filebrowser.FileBrowser;
import com.anas.jconsoleaudioplayer.userinterface.playerinterface.PlayerInterface;
import com.anas.jconsoleaudioplayer.userinterface.playlistsmanger.PlayListEditor;
import com.anas.jconsoleaudioplayer.userinterface.playlistsmanger.PlaylistsMangerInterface;

import java.io.File;

public class CLIManager {
    private FileBrowser fileBrowser;
    private PlayerInterface playerInterface;
    private PlaylistsMangerInterface playlistsMangerInterface;
    private PlayListEditor playListEditor;

    public CLIManager(MainController mainController) {
        init();
        Screen.setMainController(mainController);
    }

    private void init() {
        this.fileBrowser = FileBrowser.getInstance();
        this.playerInterface = PlayerInterface.getInstance();
        this.playlistsMangerInterface = PlaylistsMangerInterface.getInstance();
        playListEditor = new PlayListEditor();
    }

    public File[] openFileBrowser(Extension[] extensions, String startingDirectory) {
        this.fileBrowser.setExtensions(extensions);
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

    public void showPlayListEditor(Object object) {
        playListEditor.show(object);
    }

    public String getInput(String message) {
        System.out.print(message);
        return playerInterface.getMainController().getScanner().nextLine();
    }
}
