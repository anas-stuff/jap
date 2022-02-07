package com.anas.jconsoleaudioplayer.userinterface;

import com.anas.jconsoleaudioplayer.MainController;

import java.util.Scanner;

public abstract class Screen {
    private MainController mainController;
    public Scanner getScanner() {
        return mainController.getScanner();
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
