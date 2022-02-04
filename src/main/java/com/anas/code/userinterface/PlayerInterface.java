package com.anas.code.userinterface;

import com.anas.code.players.Actions;
import com.anas.code.players.Player;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Scanner;

public class PlayerInterface {
    // Singleton pattern
    private static PlayerInterface instance = null;
    private Scanner scanner;
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
        printPlayList(this.player.getPlayList().getCurrentIndex());
        printTheOptions();
        tackAction(takeInput());
    }

    private void tackAction(Actions takeInput) {
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
                case SHOW_VOLUME_LEVEL -> showVolumeLevel(player.getVolume());
                case VOLUME_UP -> player.setVolume(player.getVolume() + 1.01f);
                case VOLUME_DOWN -> player.setVolume(player.getVolume() - 1.01f);
                case EXIT -> player.exit();
                default -> System.out.println("Invalid input");
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        rePrintPayer();
    }

    private void showVolumeLevel(float volume) {
        System.out.println("Volume level is " + volume);
        rePrintPayer();
    }

    private Actions takeInput() {
        String input = "";
        input = scanner.nextLine();
        Actions action = Actions.UNKNOWN;
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
            printPlayList(result);
        } else {
            System.out.println("No result found");
            rePrintPayer();
        }

    }

    private void rePrintPayer() {
        try {
            printPlayList(player.getPlayList().getCurrentIndex());
            printPlayingTrack(player.getPlayList().getCurrentIndex());
        } catch (IndexOutOfBoundsException ignored) {}
        printTheOptions();
        tackAction(takeInput());
    }

    private Actions getTheStaticAction(String input) {
        Actions action;
        switch (input) {
            case "p" -> action = Actions.PLAY;
            case "pu" -> action = Actions.PAUSE;
            case "re" -> action = Actions.RESUME;
            case "s" -> action = Actions.STOP;
            case "n" -> action = Actions.NEXT;
            case "pr" -> action = Actions.PREVIOUS;
            case "l" -> action = Actions.LOOP_ON_ONE_CLIP;
            case "lp" -> action = Actions.LOOP_ON_PLAY_LIST;
            case "sh" -> action = Actions.SHUFFLE;
            case "m" -> action = Actions.MUTE;
            case "vl" -> action = Actions.SHOW_VOLUME_LEVEL;
            case "vu" -> action = Actions.VOLUME_UP;
            case "vd" -> action = Actions.VOLUME_DOWN;
            case "q" -> action = Actions.EXIT;
            default -> action = Actions.UNKNOWN;
        }
        return action;
    }

    private void printTheOptions() {
        System.out.println("(p)lay, (pu)se, (re)sume, (s)top, (n)ext, (pr)ivos, (l)oop, (lp)loop play list, (sh)uffle\n" +
                "(m)ute, (vl) show volume level, (vu) volume up(+10), (vd)volume down(-10)" +
                ", (:) Search, (q)uit");
        System.out.print("> ");
    }

    private void printPlayList(int currentIndex) {
        // Print the first 10 elements of the playlist from the current index
        for (int i = currentIndex; i < currentIndex + 10; i++) {
            if (i < player.getPlayList().getItems().length) {
                System.out.println(i + ": " + player.getPlayList().getItems()[i].getFileName());
            }
        }
    }

    private void printPlayingTrack(int currentIndex) {
        System.out.println("------------------------------------------------------");
        System.out.println("Playing: " + player.getPlayList().getItems()[currentIndex].toString());
        System.out.println("------------------------------------------------------");
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
