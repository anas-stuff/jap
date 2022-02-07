package com.anas.jconsoleaudioplayer.playlist;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class PlayList {
    private Track[] list;
    private int currentIndex;
    private boolean looping, shuffling;
    private int longFileNameLength;


    public PlayList() {
        list = new Track[0];
        currentIndex = 0;
        looping = false;
        shuffling = false;
        setLongFileNameLength(0);
    }

    public void add(Track item) {
        item.setPlayed(false);
        item.setIndex(list.length - 1);
        if (!contains(item)) {
            Track[] newList = new Track[list.length + 1];
            System.arraycopy(list, 0, newList, 0, list.length);
            newList[newList.length - 1] = item;
            list = newList;
        }
    }

    private boolean contains(Track item) {
        for (Track track : list) {
            if (track.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public void addAll(Track[] items) {
        if (items != null && items.length > 0) {
            Track[] newList = deleteContains(items);
            setUpItems(newList);
            newList = new Track[list.length + newList.length];
            System.arraycopy(list, 0, newList, 0, list.length);
            System.arraycopy(items, 0, newList, list.length, items.length);
            list = newList;
            setLongFileNameLength(updateLongFileNameLength(newList));
        }
    }

    private int updateLongFileNameLength(Track[] newList) {
        int longLength = longFileNameLength;
        for (Track item : newList) {
            if (item.getFileName().length() > longLength) {
                longLength = item.getFileName().length();
            }
        }
        return longLength;
    }

    private void setUpItems(Track[] newList) {
        for (int i = 0; i < newList.length; i++) {
            newList[i].setIndex(i + list.length); // Set the index of the item
            newList[i].setPlayed(false); // Set the item as not played
        }
    }

    public void remove(int index) {
        // Remove the item at the specified index
        Track[] newList = new Track[list.length - 1];
        System.arraycopy(list, 0, newList, 0, index);
        System.arraycopy(list, index + 1, newList, index, list.length - index - 1);
        list = newList;
    }

    private Track[] deleteContains(Track[] newItems) {
        Track[] newList = new Track[1];
        if (list.length > 0) {
            for (Track item : newItems) {
                boolean found = false;
                for (Track track : list) {
                    if (track.equals(item)) {
                        found = true;
                        break;
                    }
                }
                if (!found) { // If not found, add to current play list
                    Track[] temp = Arrays.copyOf(newList, newList.length + 1);
                    temp[temp.length - 1] = item;
                    newList = temp;
                }
            }
        } else {
            newList = newItems; // If no items in list, just add the new items
        }
        return newList;
    }

    public Track get(int index) {
        return list[index];
    }

    public int size() {
        return list.length;
    }

    public void next() {
        if (currentIndex == list.length - 1 && looping && !shuffling) {
            currentIndex = 0;
        } else if (shuffling) {
            int index = currentIndex;
            if (list[currentIndex].getNextTrack() == null) {
                shuffle(); // Shuffle if the next track is null
                list[index].setNextTrack(list[currentIndex]); // Set the previous track as the next track
                list[currentIndex].setPreviousTrack(list[index]); // Set the current track as the previous track
            } else {
                currentIndex = list[currentIndex].getNextTrack().getIndex();
            }
        } else {
            currentIndex++;
            list[currentIndex - 1].setNextTrack(list[currentIndex]); // Set the previous track as the next track
            list[currentIndex].setPreviousTrack(list[currentIndex - 1]); // Set the next track as the previous track
        }
    }

    public void previous() {
        if (currentIndex == 0 && looping && !shuffling) {
            currentIndex = list.length - 1;
        } else if (shuffling) {
            int index = currentIndex;
            if (list[currentIndex].getPreviousTrack() == null) {
                shuffle(); // Shuffle if the previous track is null
                list[index].setPreviousTrack(list[currentIndex]); // Set the next track as the previous track
                list[currentIndex].setNextTrack(list[index]); // Set the current track as the next track
            } else {
                currentIndex = list[currentIndex].getPreviousTrack().getIndex();
            }
        } else {
            currentIndex--;
            list[currentIndex + 1].setPreviousTrack(list[currentIndex]); // Set the next track as the previous track
            list[currentIndex].setNextTrack(list[currentIndex + 1]); // Set the previous track as the next track
        }
    }

    public PlayList shuffle() {
        int randomIndex = currentIndex;
        while (randomIndex == currentIndex) {
            randomIndex = (int) (Math.random() * list.length);
        }
        // Check if the random index is played before
        boolean end = false;
        if (list[randomIndex].isPlayed() && !(end = isEnded())) {
            return shuffle();
        } else if (end && looping) {
            reset();
            return shuffle();
        }
        currentIndex = randomIndex;
        return this;
    }

    public void played() {
        list[currentIndex].setPlayed(true);
    }

    public void reset() {
        for (Track item : list) {
            item.setPlayed(false);
            item.setNextTrack(null);
            item.setPreviousTrack(null);
        }
    }

    public boolean isEnded() {
        boolean ended = true;
        for (Track item : list) {
            if (!item.isPlayed()) {
                ended = false;
                break;
            }
        }
        return ended;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void print() {
        print(-1);
    }

    public void print(int from) {
        if (from == -1) // Print the play list from current index
            from = currentIndex;
        if (list.length > 0) {
            printPlayList(from);
        } else {
            System.out.println("Empty play list");
        }
    }

    private void printPlayList(int from) {
        String lineSplat = null;
        if (getItems().length > from + 10) {
            // Print the first 10 elements of the playlist from the current index
            for (int i = from; i < from + 10; i++) {
                lineSplat = printItem(lineSplat, i);
            }
        } else {
            printLastItems();
        }
    }

    private void printLastItems() {
        String lineSplat = null;
        int len = 0;
        if (getItems().length >= 10) {
            len = getItems().length - 10;
        }
        for (int i = len; i < list.length; i++) {
            lineSplat = printItem(lineSplat, i);
        }
    }

    private String printItem(String lineSplat, int i) {
        Track item = getItems()[i];
        Formatter formatter = new Formatter();
        formatter.format("| %2s%-3d | %" + -(longFileNameLength + "[Played] ".length()) + "s |\n",
                (item.isPlaying() ? "> " : ""), i + 1, (item.isPlayed() ? "[Played] " : "") + item.getFileName());
        if (lineSplat == null)
            lineSplat = createLineSplat(formatter);
        System.out.print(formatter + lineSplat);
        return lineSplat;
    }

    private String createLineSplat(Formatter formatter) {
        return "+" + "-".repeat(formatter.toString().length() - 3) + "+" + "\n";
    }

    public AudioInputStream getAudioInputStream() throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(play());
    }

    private File play() {
        return list[currentIndex].getFile();
    }

    public boolean isShuffling() {
        return shuffling;
    }

    public void setShuffling(boolean shuffling) {
        this.shuffling = shuffling;
    }

    public int search(String name) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].getFileName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentIndex() {
        if (list.length > 0)
            return currentIndex;
        return -1;
    }

    public Track[] getItems() {
        return list;
    }

    public int getLongFileNameLength() {
        return longFileNameLength;
    }

    private void setLongFileNameLength(int longFileNameLength) {
        this.longFileNameLength = longFileNameLength;
    }

    public Track getCurrentTrack() {
        return list[currentIndex];
    }
}
