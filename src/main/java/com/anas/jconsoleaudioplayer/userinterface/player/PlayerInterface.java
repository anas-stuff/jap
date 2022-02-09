package com.anas.jconsoleaudioplayer.userinterface.player;

import com.anas.jconsoleaudioplayer.player.Action;
import com.anas.jconsoleaudioplayer.player.Loop;
import com.anas.jconsoleaudioplayer.player.PlayersAdaptor;
import com.anas.jconsoleaudioplayer.userinterface.Screen;

public class PlayerInterface extends Screen {
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
        print();
    }

    private void print() {
        rePrintPayer(true);
    }

    private void tackAction(Action takeInput, boolean rePrint) {
        try {
            switch (takeInput) {
                case PLAY -> playersAdaptor.play();
                case PAUSE -> playersAdaptor.pause();
                case RESUME -> playersAdaptor.resume();
                case STOP -> playersAdaptor.stop();
                case NEXT -> playersAdaptor.next();
                case PREVIOUS -> playersAdaptor.previous();
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
                case EXIT ->  super.getMainController().exit();
                default -> System.out.println("Invalid input");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rePrint)
            rePrintPayer(true);
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
        String input = "";
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
        int result = playersAdaptor.getPlayList().search(substring);
        if (result != -1) {
            // Print the playlist from the result
            super.getMainController().getPlayList().print(result);
        } else {
            System.out.println("No result found");
            rePrintPayer(true);
        }

    }

    public void rePrint() {
        rePrintPayer(false);
    }

    private void rePrintPayer(boolean rePrintAffterAction) {
        System.out.println();
        try {
            super.getMainController().getPlayList().print();
            printPlayingTrack(super.getMainController().getPlayList().getCurrentIndex());
        } catch (IndexOutOfBoundsException ignored) {
        }
        printTheOptions();
        tackAction(takeInput(), rePrintAffterAction);
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
            case "exit" -> Action.EXIT;
            default -> Action.UNKNOWN;
        };
    }

    // TODO: Refactor this method to be more readable
    private void printTheOptions() {
        System.out.println("(p)lay, (pa)use, (re)sume, (s)top, (n)ext, (pr)evious, (loop) loop on current track, (loop1) loop on current track one time, (lp)loop play list, (sh)uffle\n" +
                "(m)ute, (vl) show volume level,(v:) set volume, (v+) volume up(+10), (v-)volume down(-10)" +
                ", (open) Open file browser, (:) Search, (exit) Exit from program");
        System.out.print("> ");
    }

    private void printPlayingTrack(int currentIndex) {
        String p = "Playing: " +
                (currentIndex != -1 ? super.getMainController().getPlayList().getItems()[currentIndex].toString() :
                        "null");
        String s = "-".repeat(p.length());
        System.out.println(s);
        System.out.println(p);
        System.out.println(s);
    }
}
