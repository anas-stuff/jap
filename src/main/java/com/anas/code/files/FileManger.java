package com.anas.code.files;

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
     * @param extension - the extension to search for
     * @return - a list of files with the given extension
     */
    public static File[] getAbsoluteFiles(File directory, String extension) {
        ArrayList<File> files = new ArrayList<>(List.of(Objects.requireNonNull(directory.listFiles())));
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isDirectory()) {
                // Remove the directory from the list if is empty
                if (files.get(i).listFiles() == null) {
                    files.remove(i); // Remove the empty directory from the list
                } else {
                    File dir = files.remove(i);
                    files.addAll(List.of(getAbsoluteFiles(dir, extension)));
                }
                i--; // Decrement i to compensate for the removed element
            } else if (!files.get(i).getName().endsWith(extension)) {
                files.remove(i--); // remove file and decrement i to compensate for the removed element
            }
        }
        return files.toArray(new File[0]);
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
}
