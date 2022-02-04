package com.anas.code;

import com.anas.code.players.Player;
import com.anas.code.playlist.PlayList;
import com.anas.code.userinterface.CLIManager;

import javax.sound.sampled.LineUnavailableException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CLIManager cliManager = new CLIManager(scanner);
        PlayList playList = new PlayList();
        playList.addAll(cliManager.openFileBrowser(null));
        Player player = null;
        try {
            player = new Player(playList);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        cliManager.showPlayerInterface(player);

    }
}
