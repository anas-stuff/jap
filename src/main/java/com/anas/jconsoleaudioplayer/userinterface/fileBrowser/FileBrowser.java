package com.anas.jconsoleaudioplayer.userinterface.fileBrowser;

import com.anas.jconsoleaudioplayer.files.FileManger;
import com.anas.jconsoleaudioplayer.player.Extension;
import com.anas.jconsoleaudioplayer.playlist.Track;
import com.anas.jconsoleaudioplayer.userinterface.Screen;
import com.anas.jconsoleaudioplayer.userinterface.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowser extends Screen {
    // Singleton pattern
    private static FileBrowser instance;
    private Extension[] extensions;

    private FileBrowser() {
    }

    public static FileBrowser getInstance() {
        if (instance == null) {
            instance = new FileBrowser();
        }
        return instance;
    }
    public Track[] openBrowser(String path) {
        if (path == null) {
            path = System.getProperty("user.home");
        }
        File[] files = new File(path).listFiles();
        List<Track> list = new ArrayList<>();
        String userInput = "";
        do {
            files = printFilesList(files);
            System.out.println(">: Go to, <: Back, +: Add to list, -: Remove from list, Q: Quit from file browser");
            userInput = getUserInput();
            String[] userInputArray = new String[0];
            if (userInput.length() > 1) {
                try {
                    userInputArray = Utility.parseInput(userInput);
                    userInput = userInputArray[0]; // first element is the command
                } catch (Exception e) {
                    continue;
                }

            }
            files = tackeAction(path, files, list, userInput, userInputArray);
            path = files[0].getParent();
        } while (!userInput.equalsIgnoreCase("q"));
        return list.toArray(new Track[0]);
    }

    private File[] printFilesList(File[] files) {
        if (files != null) {
            files = FileManger.filterFiles(files, extensions);
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ": " + (FileManger.isRootDir(files[i]) ? files[i].getPath() : files[i].getName()));
            }
        } else {
            System.out.println("No files found");
        }
        return files;
    }

    private File[] tackeAction(String path, File[] files, List<Track> list, String userInput, String[] userInputArray) {
        switch (userInput) {
            case "<" -> files = FileManger.back(path);
            case "+" -> add(files, list, userInputArray);
            case "-" -> remove(list, userInputArray);
            case ">" -> {
                if (userInputArray.length == 2) {
                    // Go to directory
                    files = FileManger.getFiles(new File(files[Integer.parseInt(userInputArray[1]) - 1].getPath()));
                }
            }
            case "Q" -> super.getMainController().setResentPath(path);
            case "exit" -> super.getMainController().exit();
        }
        return files;
    }

    private void remove(List<Track> list, String[] userInputArray) {
        if (userInputArray.length > 1) {
            for (int i = 1; i < userInputArray.length; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getIndex() == Integer.parseInt(userInputArray[i]) - 1) {
                        list.remove(j--);
                    }
                }
            }
        }
    }

    private void add(File[] files, List<Track> list, String[] userInputArray) {
        try {
            if (userInputArray.length > 1) {
                for (int i = 1; i < userInputArray.length; i++) {
                    File[] filesToAdd = FileManger.getAbsoluteFiles(
                            new File(files[Integer.parseInt(userInputArray[i]) - 1].getPath()),
                            extensions);
                    for (File file : filesToAdd) {
                        list.add(new Track(Integer.parseInt(userInputArray[i]) - 1, file));
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.err.println("File not found");
        }
    }

    private String getUserInput() {
        String userInput;
        System.out.print("> ");
        userInput = super.getScanner().nextLine();
        userInput = userInput.toUpperCase();
        return userInput;
    }

    public void setExtensions(Extension[] extensions) {
        this.extensions = extensions;
    }
}
