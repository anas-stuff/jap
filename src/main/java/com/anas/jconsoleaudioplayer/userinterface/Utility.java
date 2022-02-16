package com.anas.jconsoleaudioplayer.userinterface;

public class Utility {
    public static String[] parseInput(String input) {
        String prefix = input.substring(0, input.indexOf(" "));
        input = input.substring(input.indexOf(" ") + 1); // remove prefix
        String[] args = input.split(",");
        String[] result = new String[args.length + 1]; // +1 for prefix
        result[0] = prefix;
        for (int i = 0; i < args.length; i++) {
            // Process the arguments to remove the spaces
            args[i] = args[i].trim();
            // Add the arguments to the result array
            result[i + 1] = args[i];
        }
        return result;
    }
}
