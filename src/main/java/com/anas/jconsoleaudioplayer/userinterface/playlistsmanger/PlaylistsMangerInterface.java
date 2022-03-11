package com.anas.jconsoleaudioplayer.userinterface.playlistsmanger;

import com.anas.jconsoleaudioplayer.Extension;
import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.playlist.PlayListLoader;
import com.anas.jconsoleaudioplayer.playlist.PlayListsManger;
import com.anas.jconsoleaudioplayer.userinterface.Screen;

import java.io.File;

public class PlaylistsMangerInterface extends Screen {
    private final PlayListsManger playlistsManger;

    // Singleton
    private static PlaylistsMangerInterface instance = null;

    private PlaylistsMangerInterface() {
        super();
        playlistsManger = PlayListsManger.getInstance();
    }

    public static PlaylistsMangerInterface getInstance() {
        if (instance == null) {
            instance = new PlaylistsMangerInterface();
        }
        return instance;
    }


    @Override
    protected boolean takeActions(String[] userInput) {
        switch (userInput[0]) {
            case ">" -> editPlayList(userInput[1]);
            case "*" -> select(userInput);
            case "new" -> newPlayList(userInput);
            case "del" -> deletePlayList(userInput);
            case "load" -> loadPlayList();
            default -> {
                return false;
            }
        }
        return true;
    }

    private void loadPlayList() {
        File[] files = super.getMainController().getCliManager().
                openFileBrowser(new Extension[]{Extension.JSON},
                        super.getMainController().getResentPath());
        if (files != null) {
            for (File file : files) {
                playlistsManger.addPlayList(PlayListLoader.getInstance().load(file));
            }
        }
    }

    private void select(String[] userInput) {
        try {
            playlistsManger.setCurrentPlayList(
                    playlistsManger.getPlayList(Integer.parseInt(userInput[1]) - 1)
            );
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    @Override
    protected void quit() {

    }

    private void deletePlayList(String[] userInput) {
        try {
            PlayList playlist = null;
            if (userInput.length > 1) {
                try {
                    playlist = playlistsManger.getPlayList(Integer.parseInt(userInput[1]) - 1);
                } catch (NumberFormatException e) {
                    playlist = playlistsManger.getPlayList(userInput[1]);
                }
            }
            if (!playlistsManger.removePlayList(playlist))
                throw new NullPointerException();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Playlist not found");
        }
    }

    private void newPlayList(String[] userInput) {
        String name = "New Playlist";
        if (userInput.length > 1) {
            name = "";
            for (String s : userInput) {
                name = name.concat(s + " ");
            }
            name = name.substring(0, name.length() - 1); // remove last space
        }
        super.getMainController().getCliManager().showPlayListEditor(name);
    }

    private void editPlayList(String playListIndex) {
        try {
            super.getMainController().getCliManager().showPlayListEditor(
                    playlistsManger.getPlayList(Integer.parseInt(playListIndex) - 1));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Playlist not found");
        }
    }

    @Override
    protected String takeUserInput() {
        System.out.print("> ");
        return super.getScanner().nextLine();
    }

    @Override
    protected void setArgs(Object... args) {

    }

    @Override
    protected void printTheOptionsMenu() {
        System.out.println("(>) - Enter to playlist editor, (*) - Select PlayList, (load) - Load PlayList");
        System.out.println("[new] - create new playlist, [del] - delete playlist, [q] Quit to the player, [exit] - exit");
    }

    @Override
    protected void printInterface() {
        for (int i = 0; i < playlistsManger.getPlayLists().size(); i++) {
            System.out.println((i + 1) + ": " + playlistsManger.getPlayLists().get(i).toString());
        }
    }
}
