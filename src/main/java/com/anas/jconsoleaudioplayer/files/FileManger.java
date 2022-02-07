package com.anas.jconsoleaudioplayer.files;

import com.anas.jconsoleaudioplayer.players.Extension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManger {
    /**
     * Get the root files of the system
     * @return - an array of root files
     */
    public static File[] getRootFiles() {
        return File.listRoots();
    }

    /**
     * Get the files of the system
     * @return - an array of files
     */
    public static File[] getFiles(File directory) {
        return directory.listFiles();
    }

    /**
     * Get all files in the directory and subdirectories with the given extension
     * @param directory - the directory to search
     * @param extensions - the extensions to search for
     * @return - a list of files with the given extension
     */
    public static File[] getAbsoluteFiles(File directory, Extension[] extensions) {
        ArrayList<File> files = new ArrayList<>(List.of(Objects.requireNonNull(directory.listFiles())));
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isDirectory()) {
                // Remove the directory from the list if is empty
                if (files.get(i).listFiles() == null) {
                    files.remove(i); // Remove the empty directory from the list
                } else {
                    File dir = files.remove(i);
                    files.addAll(List.of(getAbsoluteFiles(dir, extensions)));
                }
                i--; // Decrement i to compensate for the removed element
            } else if (!isSupportedFile(files.get(i), extensions)) {
                files.remove(i--); // remove file and decrement i to compensate for the removed element
            }
        }
        return files.toArray(new File[0]);
    }

    public static boolean isSupportedFile(File file, Extension[] extensions) {
        for (Extension extension : extensions) {
            if (file.getName().toUpperCase().endsWith(extension.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the parent directory of the given file if the file is not the root directory
     * return the root list if the file is the root directory
     * @param path - the path of the file
     * @return - the parent directory of the given file
     */
    public static File[] back(String path) {
        File f = null;
        if (path != null) {
            f = new File(path).getParentFile();
        }
        if (f == null) {
            return getRootFiles();
        }
        return f.listFiles();
    }

    /**
     *  Used to leran is root directory
     * @param file - the file to check
     * @return - true if the file is the root directory
     */
    public static boolean isRootDir(File file) {
        return file.getParentFile() == null; // if the parent file is null, then it is the root directory
    }

    public static File[] filterFiles(File[] files, Extension[] extensions) {
        ArrayList<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                if (isSupportedFile(file, extensions)) {
                    filteredFiles.add(file);
                }
            } else {
                filteredFiles.add(file);
            }
        }
        return filteredFiles.toArray(new File[0]);
    }
}
