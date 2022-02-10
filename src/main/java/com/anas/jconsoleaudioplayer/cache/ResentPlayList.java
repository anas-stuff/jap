package com.anas.jconsoleaudioplayer.cache;

import com.anas.jconsoleaudioplayer.playlist.PlayList;

public class ResentPlayList {
    private PlayList recentPlayList;

    public ResentPlayList() {
        this.recentPlayList = null;
    }

    public PlayList getRecentPlayList() {
        return recentPlayList;
    }

    public void setRecentPlayList(PlayList recentPlayList) {
        this.recentPlayList = recentPlayList;
    }
}
