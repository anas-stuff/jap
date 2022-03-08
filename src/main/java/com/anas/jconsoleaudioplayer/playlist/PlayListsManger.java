package com.anas.jconsoleaudioplayer.playlist;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class PlayListsManger implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<PlayList> playLists;
    private PlayList currentPlayList;
    private static PlayListsManger instance;

    private PlayListsManger() {
        init();
    }

    public static PlayListsManger getInstance() {
        if (instance == null) {
            instance = new PlayListsManger();
        }
        return instance;
    }

    private void init() {
        playLists = new ArrayList<>();
        currentPlayList = new PlayList("Default", 0);
    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

    public void addPlayList(PlayList playList) {
        playLists.add(playList);
    }

    public void removePlayList(PlayList playList) {
        playLists.remove(playList);
    }

    public PlayList getPlayList(String name) {
        for (PlayList playList : playLists) {
            if (playList.getName().equals(name)) {
                return playList;
            }
        }
        return null;
    }

    public PlayList getPlayList(int index) {
        return playLists.get(index);
    }

    public PlayList getCurrentPlayList() {
        return currentPlayList;
    }

    public void setCurrentPlayList(String name) {
        PlayList playList = getPlayList(name);
        if (playList != null) {
            currentPlayList = playList;
        }
    }

    public void setCurrentPlayList(PlayList playList) {
        if (playList == null)
            return;
        playLists.add(playList);
        currentPlayList = playList;
    }

    public void removePlayList(int index) {
        playLists.remove(index);
    }

    public void setPlayListName(int index, String name) {
        int preFix = 0;
        for (PlayList playList : playLists) {
            if (playList.getName().equals(name + (preFix == 0 ? "" : "_" + preFix))) {
                preFix++;
            }
        }
        playLists.get(index).setNameAndPrefix(name, preFix);
    }
}
