package com.anas.code.userinterface;

import com.anas.code.files.FileManger;
import com.anas.code.playlist.ListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileBrowser {
    private Scanner scanner;

    // Singleton pattern
    private static FileBrowser instance;

    private FileBrowser() {
    }

    protected static FileBrowser getInstance() {
        if (instance == null) {
            instance = new FileBrowser();
        }
        return instance;
    }
    public ListItem[] openBrowser(String path) {
        if (path == null) {
            path = System.getProperty("user.home");
        }
        File[] files = new File(path).listFiles();
        List<ListItem> list = new ArrayList<>();
        String userInput = "";
        do {
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    System.out.println((i + 1) + ": " + (FileManger.isRootDir(files[i]) ? files[i].getPath() : files[i].getName()));
                }
            } else {
                System.out.println("No files found");
            }
            System.out.println("0: Back, +: Add to list, -: Remove from list, Q: Quit from file browser");
            userInput = getUserInput();
            String[] userInputArray = new String[0];
            if (userInput.length() > 1) {
                userInputArray = Utility.parseInput(userInput);
                userInput = userInputArray[0]; // first element is the command
            }
            files = tackeAction(path, files, list, userInput, userInputArray);
            path = files[0].getParent();
        } while (!userInput.equalsIgnoreCase("q"));
        return list.toArray(new ListItem[0]);
    }

    private File[] tackeAction(String path, File[] files, List<ListItem> list, String userInput, String[] userInputArray) {
        switch (userInput) {
            case "0" -> files = FileManger.back(path);
            case "+" -> add(path, files, list, userInputArray);
            case "-" -> remove(list, userInputArray);
            case ">" -> {
                if (userInputArray.length == 2) {
                    // Go to directory
                    files = FileManger.getFiles(new File(files[Integer.parseInt(userInputArray[1]) - 1].getPath()));
                }
            }
            case "Q" -> {

            }
        }
        return files;
    }

    private void remove(List<ListItem> list, String[] userInputArray) {
        if (userInputArray.length > 1) {
            for (int i = 1; i < userInputArray.length; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getIndex() == Integer.parseInt(userInputArray[i]) - 1) {
                        list.remove(j);
                    }
                }
            }
        }
    }

    private void add(String path, File[] files, List<ListItem> list, String[] userInputArray) {
        if (userInputArray.length > 1) {
            for (int i = 1; i < userInputArray.length; i++) {
                File[] filesToAdd = FileManger.getAbsoluteFiles(
                        new File(files[Integer.parseInt(userInputArray[i]) - 1].getPath()),
                        ".wav");
                for (File file : filesToAdd) {
                    list.add(new ListItem(Integer.parseInt(userInputArray[i]) - 1, file));
                }
            }
        }
    }

    private String getUserInput() {
        String userInput;
        System.out.print("> ");
        userInput = scanner.nextLine();
        userInput = userInput.toUpperCase();
        return userInput;
    }

    // Test
    public void main(String[] args) {
        ListItem[] li = openBrowser(null);
        for (ListItem listItem : li) {
            System.out.println(listItem.getIndex() + ": " + listItem.getFile().getName());
        }
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
