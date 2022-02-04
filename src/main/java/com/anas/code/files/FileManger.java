package com.anas.code.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManger {
    public static File[] getRootFiles() {
        return File.listRoots();
    }

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

    public static void main(String[] args) {
        var file = new File("D:\\Music\\FLAC");
        System.out.println("file.isDirectory() = " + file.isDirectory());
        var files = getAbsoluteFiles(file, ".wav");
        for (var filea : files) {
            System.out.println(filea.getName());
        }
        // print total number of files
        System.out.println("Total number of files: " + files.length);
    }

    public static File[] back(String path) {
        var f = new File(path).getParentFile();
        if (f == null) {
            return getRootFiles();
        }
        return f.listFiles();
    }
}
