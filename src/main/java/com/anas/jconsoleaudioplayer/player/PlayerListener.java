package com.anas.jconsoleaudioplayer.player;

public interface PlayerListener {
    /**
     * Called when the player has any change, such as the player state(playing, paused, stopped)
     * @param event the event
     */
    void onPlayerEvent(PlayerEvent event);
}
