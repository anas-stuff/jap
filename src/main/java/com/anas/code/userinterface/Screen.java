package com.anas.code.userinterface;

import com.anas.code.MainController;

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
