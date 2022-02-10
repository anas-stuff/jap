package com.anas.jconsoleaudioplayer.userinterface.playlistsmanger;

import com.anas.jconsoleaudioplayer.userinterface.Screen;

public class PlaylistsMangerInterface extends Screen {

    // Singleton
    private static PlaylistsMangerInterface instance;

    private PlaylistsMangerInterface() {
        super();
    }

    public static PlaylistsMangerInterface getInstance() {
        if (instance == null) {
            instance = new PlaylistsMangerInterface();
        }
        return instance;
    }

    public void show() {
        do {
            printPlaylists();
        }
    }

    private void printPlaylists() {

    }
}
