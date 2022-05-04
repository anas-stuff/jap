package com.anas.jconsoleaudioplayer.userinterface.playlistsmanger;

import com.anas.jconsoleaudioplayer.playlist.PlayList;
import com.anas.jconsoleaudioplayer.playlist.PlayListHelper;
import com.anas.jconsoleaudioplayer.playlist.PlayListsManger;
import com.anas.jconsoleaudioplayer.userinterface.Screen;

public class PlayListEditor extends Screen {
    private final PlayListsManger playListsManger;
    private Object[] args;
    private PlayList playList;

    public PlayListEditor() {
        playListsManger = PlayListsManger.getInstance();
    }

    @Override
    protected void setArgs(Object... args) {
        this.args = args;
    }

    @Override
    protected void printInterface() {
        PlayList playlist = null;
        if (args != null && args.length > 0) {
            if (args[0] instanceof String) {
                playListsManger.newPlayList((String) args[0]);
                playlist = playListsManger.getCurrentPlayList();
            } else if (args[0] instanceof PlayList) {
                playlist = (PlayList) args[0];
            }
            if (playlist != null) {
                this.playList = playlist;
                printPlayListInfo(playlist);
                printPlayList(playlist);
            } else {
                this.playList = playListsManger.getCurrentPlayList();
            }
        }
    }

    private void printPlayListInfo(PlayList playlist) {
        System.out.println(playlist);
    }

    private void printPlayList(PlayList playlist) {
        playlist.print(0);
    }

    @Override
    protected void printTheOptionsMenu() {
        System.out.println("(+) Add track/s to playlist, (-) Remove track/s from playlist, (rename) Rename the playlist, (q) Quit");
    }

    @Override
    protected boolean takeActions(String[] parseInput) {
        switch (parseInput[0]) {
            case "+" -> add();
            case "-" -> remove(parseInput);
            case "rename" -> rename(parseInput);
            default -> {return false;}
        }
        return true;
    }

    private void rename(String[] parseInput) {
        String newName = "";
        if (parseInput.length > 1) {
            for (String s : parseInput) {
                newName = newName.concat(s + " ");
            }
            newName = newName.substring(0, newName.length() - 1); // remove the last space
        } else {
            newName = super.getMainController().getCliManager().getInput("Enter new name: ");
        }
        playListsManger.setPlayListName(playListsManger.getPlayLists().indexOf(playList), newName);
    }

    private void remove(String[] parseInput) {
        try {
            if (parseInput.length <= 1)
                throw new Exception("No track/s to remove");
            for (int i = 1; i < parseInput.length; i++) {
                playList.remove(Integer.parseInt(parseInput[i]) - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void add() {
        PlayListHelper.addAllToPlayList(playList,
                super.getMainController().getCliManager().
                        openFileBrowser(super.getMainController().getPlayersAdaptor().getSupportedExtensions(),
                        super.getMainController().getResentPath()));
    }

    @Override
    protected void quit() {
        playListsManger.updatePlayList(playList);
    }
}
