package com.anas.jconsoleaudioplayer.userinterface.playerinterface;

import com.anas.jconsoleaudioplayer.player.*;
import com.anas.jconsoleaudioplayer.playlist.EndPlayListException;
import com.anas.jconsoleaudioplayer.playlist.PlayListsManger;
import com.anas.jconsoleaudioplayer.userinterface.Screen;
import com.anas.jconsoleaudioplayer.userinterface.playlistsmanger.PlaylistsMangerInterface;

public class PlayerInterface extends Screen implements PlayerListener {
    // Singleton pattern
    private static PlayerInterface instance = null;
    private PlayersAdaptor playersAdaptor;

    private PlayerInterface() {
    }

    public static PlayerInterface getInstance() {
        if (instance == null) {
            instance = new PlayerInterface();
        }
        return instance;
    }

    public void setPlayersAdaptor(PlayersAdaptor playersAdaptor) {
        this.playersAdaptor = playersAdaptor;
    }

    public void start(PlayersAdaptor playersAdaptor) {
        setPlayersAdaptor(playersAdaptor);
        playersAdaptor.addPlayerListener(this); // add the listener to the player
        print();
    }

    private void print() {
        rePrintPayer(true);
    }

    private void tackAction(Action userAction, boolean rePrint) {
        try {
            switch (userAction) {
                case PLAY -> playersAdaptor.play();
                case PAUSE -> playersAdaptor.pause();
                case RESUME -> playersAdaptor.resume();
                case STOP -> playersAdaptor.stop();
                case NEXT -> nextAndPrevious(Action.NEXT);
                case PREVIOUS -> nextAndPrevious(Action.PREVIOUS);
                case LOOP_ON_ONE_CLIP_ONE_TIME -> playersAdaptor.setLoopOnTrack(Loop.LOOP_ONE_TIME);
                case LOOP_ON_ONE_CLIP -> playersAdaptor.setLoopOnTrack(Loop.LOOP);
                case LOOP_ON_PLAY_LIST -> playersAdaptor.loopOfPlayList();
                case SHUFFLE -> playersAdaptor.shuffle();
                case MUTE -> playersAdaptor.mute();
                case SHOW_VOLUME_LEVEL -> showVolumeLevel((float) playersAdaptor.getVolume());
                case SET_VOLUME -> playersAdaptor.setVolume(takeNewVolume());
                case VOLUME_UP -> playersAdaptor.setVolume(playersAdaptor.getVolume() + 0.1);
                case VOLUME_DOWN -> playersAdaptor.setVolume(playersAdaptor.getVolume() - 0.1);
                case OPEN_FILE_BROWSER -> super.getMainController().openFileBrowser();
                case OPEN_PLAYLIST_MANAGER -> PlaylistsMangerInterface.getInstance().show();
                case EXIT ->  super.getMainController().exit();
                default -> System.out.println("Invalid input");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rePrint)
            rePrintPayer(true);
    }

    private void nextAndPrevious(Action action) {
        try {
            switch (action) {
                case NEXT -> playersAdaptor.next();
                case PREVIOUS -> playersAdaptor.previous();
            }
        } catch (EndPlayListException e) {
            System.out.println(e.getMessage());
            if (askForRestartPlayList()) {
                PlayListsManger.getInstance().getCurrentPlayList().reset();
                playersAdaptor.play();
            }
        }
    }

    private double takeNewVolume() {
        double volume = -1;
        do {
            System.out.println("Enter the new volume level: ");
            try {
                 volume = Double.parseDouble(super.getScanner().nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number");
            }
        } while (volume < 0 || volume > 100);
        return volume / 100.0;
    }

    private void showVolumeLevel(float volume) {
        System.out.println("Volume level is " + (volume * 100) + "%");
    }

    private Action takeInput() {
        String input;
        Action action = Action.UNKNOWN;

        input = super.getScanner().nextLine();
        // Remove the starting and ending spaces
        input = input.trim();
        if (!input.startsWith(":")) {
            input = input.toLowerCase();
            action = getTheStaticAction(input);
        } else {
            // Search for the input
            search(input.substring(1));
        }
        return action;
    }

    private void search(String substring) {
        // Search for the substring in the playlist
        int result = PlayListsManger.getInstance().getCurrentPlayList().search(substring);
        if (result != -1) {
            // Print the playlist from the result
            PlayListsManger.getInstance().getCurrentPlayList().print(result);
        } else {
            System.out.println("No result found");
            rePrintPayer(true);
        }

    }

    public void rePrint() {
        rePrintPayer(false);
    }

    private void rePrintPayer(boolean rePrintAfterAction) {
        System.out.println();
        try {
            PlayListsManger.getInstance().getCurrentPlayList().print();
            printPlayingTrack(PlayListsManger.getInstance().getCurrentPlayList().getCurrentIndex());
        } catch (IndexOutOfBoundsException ignored) {
        }
        printTheOptions();
        tackAction(takeInput(), rePrintAfterAction);
    }

    private String getModes() {
        return ("| " + (PlayListsManger.getInstance().getCurrentPlayList().isShuffling() ? "S " : "") +
                (PlayListsManger.getInstance().getCurrentPlayList().isLooping() ? "lp " : "") +
                super.getMainController().getPlayersAdaptor().getLoopOnTrack().name().toLowerCase());
    }

    private Action getTheStaticAction(String input) {
        return switch (input) {
            case "p" -> Action.PLAY;
            case "pa" -> Action.PAUSE;
            case "re" -> Action.RESUME;
            case "s" -> Action.STOP;
            case "n" -> Action.NEXT;
            case "pr" -> Action.PREVIOUS;
            case "loop1" -> Action.LOOP_ON_ONE_CLIP_ONE_TIME;
            case "loop" -> Action.LOOP_ON_ONE_CLIP;
            case "lp" -> Action.LOOP_ON_PLAY_LIST;
            case "sh" -> Action.SHUFFLE;
            case "m" -> Action.MUTE;
            case "vl" -> Action.SHOW_VOLUME_LEVEL;
            case "v:" -> Action.SET_VOLUME;
            case "v+" -> Action.VOLUME_UP;
            case "v-" -> Action.VOLUME_DOWN;
            case "open" -> Action.OPEN_FILE_BROWSER;
            case "playlists" -> Action.OPEN_PLAYLIST_MANAGER;
            case "exit" -> Action.EXIT;
            default -> Action.UNKNOWN;
        };
    }

    @Override
    protected void quit() {

    }

    @Override
    protected boolean takeActions(String[] parseInput) {
        return false;
    }

    @Override
    protected void printTheOptionsMenu() {

    }

    // TODO: Refactor this method to be more readable
    private void printTheOptions() {
        System.out.println("(p)lay, (pa)use, (re)sume, (s)top, (n)ext, (pr)evious, (loop) loop on current track, (loop1) loop on current track one time, (lp)loop play list, (sh)uffle\n" +
                "(m)ute, (vl) show volume level,(v:) set volume, (v+) volume up(+10), (v-)volume down(-10)" +
                ", (open) Open file browser,(playlists) open the playlists manger, (:) Search, (exit) Exit from program");
        System.out.print("> ");
    }

    @Override
    protected void setArgs(Object... args) {

    }

    @Override
    protected void printInterface() {

    }

    private void printPlayingTrack(int currentIndex) {
        String p = "Playing: " +
                (currentIndex != -1 ? PlayListsManger.getInstance().getCurrentPlayList().getItems()[currentIndex].toString() :
                        "null") + " " + getModes();
        String s = "-".repeat(p.length());
        System.out.println(s);
        System.out.println(p);
        System.out.println(s);
    }

    public boolean askForRestartPlayList() {
        System.out.println("Do you want to restart the play list? (y/n)");
        return super.getScanner().nextLine().equalsIgnoreCase("y");
    }


    @Override
    public void onPlayerEvent(PlayerEvent event) {
        // Just re print the interface for now
        rePrint();
    }
}
