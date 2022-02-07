package com.anas.jconsoleaudioplayer.userinterface.player;

import com.anas.jconsoleaudioplayer.players.Action;
import com.anas.jconsoleaudioplayer.players.Player;
import com.anas.jconsoleaudioplayer.userinterface.Screen;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class PlayerInterface extends Screen {
    // Singleton pattern
    private static PlayerInterface instance = null;
    private Player player;

    private PlayerInterface() {
    }

    public static PlayerInterface getInstance() {
        if (instance == null) {
            instance = new PlayerInterface();
        }
        return instance;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void start(Player player) {
        setPlayer(player);
        print();
    }

    private void print() {
        rePrintPayer(true);
    }

    private void tackAction(Action takeInput, boolean rePrint) {
        try {
            switch (takeInput) {
                case PLAY -> player.play();
                case PAUSE -> player.pause();
                case RESUME -> player.resume();
                case STOP -> player.stop();
                case NEXT -> player.next();
                case PREVIOUS -> player.previous();
                case LOOP_ON_ONE_CLIP -> player.loop();
                case LOOP_ON_PLAY_LIST -> player.loopOfPlayList();
                case SHUFFLE -> player.shuffle();
                case MUTE -> player.mute();
                case SHOW_VOLUME_LEVEL -> showVolumeLevel((float) player.getVolume());
                case SET_VOLUME -> player.setVolume(takeNewVolume());
                case VOLUME_UP -> player.setVolume(player.getVolume() + 0.1);
                case VOLUME_DOWN -> player.setVolume(player.getVolume() - 0.1);
                case OPEN_FILE_BROWSER -> super.getMainController().openFileBrowser();
                case EXIT -> {
                    player.exit();
                    System.exit(0);
                }
                default -> System.out.println("Invalid input");
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        if (rePrint)
            rePrintPayer(true);
    }

    private double takeNewVolume() {
        double volume = -1;
        do {
            System.out.println("Enter the new volume level: ");
            volume = super.getScanner().nextDouble();
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
        int result = player.getPlayList().search(substring);
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
            case "pu" -> Action.PAUSE;
            case "re" -> Action.RESUME;
            case "s" -> Action.STOP;
            case "n" -> Action.NEXT;
            case "pr" -> Action.PREVIOUS;
            case "l" -> Action.LOOP_ON_ONE_CLIP;
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

    private void printTheOptions() {
        System.out.println("(p)lay, (pu)se, (re)sume, (s)top, (n)ext, (pr)ivos, (l)oop, (lp)loop play list, (sh)uffle\n" +
                "(m)ute, (vl) show volume level,(v:) set volume, (v+) volume up(+10), (v-)volume down(-10)" +
                ", (open) Open file browser, (:) Search, (q)uit");
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