package com.anas.jap.userinterface;

import com.anas.jap.MainController;

import java.util.Scanner;

public abstract class Screen {
    private static MainController mainController;

    public void show(Object ... args) {
        String userInput;
        setArgs(args);
        do {
            printInterface();
            printTheOptionsMenu();
            userInput = takeUserInput();
            if (!takeActions(Utility.parseInput(userInput))) {
                switch (userInput) {
                    case "q" -> quit();
                    case "exit" -> mainController.exit();
                    default -> System.out.println("Invalid input");
                }
            }
        } while (!userInput.equalsIgnoreCase("q"));
    }

    protected String takeUserInput() {
        return getMainController().getCliManager().getInput("> ");
    }

    protected abstract void setArgs(Object ... args);
    protected abstract void printInterface();
    protected abstract void printTheOptionsMenu();
    protected abstract boolean takeActions(String[] parseInput);

    protected abstract void quit();
    public Scanner getScanner() {
        return mainController.getScanner();
    }

    public MainController getMainController() {
        return mainController;
    }

    public static void setMainController(MainController mainController) {
        Screen.mainController = mainController;
    }
}
