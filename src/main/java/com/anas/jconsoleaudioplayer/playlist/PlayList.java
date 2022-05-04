package com.anas.jconsoleaudioplayer.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Formatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayList implements Serializable {
    private String name;
    private int namePrefix;
    private int currentIndex;
    private boolean looping, shuffling;
    private int longFileNameLength;
    private Track[] items;

    private PlayList() {
        this("", 0);
    }

    protected PlayList(String name, int namePrefix) {
        this.name = name;
        this.namePrefix = namePrefix;
        items = new Track[0];
        currentIndex = 0;
        looping = false;
        shuffling = false;
        setLongFileNameLength(0);
    }

    public void add(Track item) {
        item.setPlayed(false);
        item.setIndex(items.length - 1);
        if (!contains(item)) {
            Track[] newList = new Track[items.length + 1];
            System.arraycopy(items, 0, newList, 0, items.length);
            newList[newList.length - 1] = item;
            items = newList;
        }
    }

    private boolean contains(Track item) {
        for (Track track : items) {
            if (track.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public void addAll(Track[] items) {
        if (items != null && items.length > 0) {
            Track[] newList = deleteContains(items);
            if (newList != null && newList.length > 0) {
                setUpItems(newList);
                newList = new Track[this.items.length + newList.length];
                System.arraycopy(this.items, 0, newList, 0, this.items.length);
                System.arraycopy(items, 0, newList, this.items.length, items.length);
                this.items = newList;
                setLongFileNameLength(updateLongFileNameLength(newList));
            }
        }
    }

    private int updateLongFileNameLength(Track[] newList) {
        int longLength = longFileNameLength;
        for (Track item : newList) {
            File file = item.getFile();
            if (file.length() > longLength) {
                longLength = file.getName().length();
            }
        }
        return longLength;
    }

    private void setUpItems(Track[] newList) {
        for (int i = 0; i < newList.length; i++) {
            newList[i].setIndex(i + items.length); // Set the index of the item
            newList[i].setPlayed(false); // Set the item as not played
        }
    }

    public void remove(int index) {
        // Remove the item at the specified index
        Track[] newList = new Track[items.length - 1];
        System.arraycopy(items, 0, newList, 0, index);
        System.arraycopy(items, index + 1, newList, index, items.length - index - 1);
        items = newList;
    }

    private Track[] deleteContains(Track[] newItems) {
        Track[] newList = new Track[0];
        if (items.length > 0) {
            for (Track item : newItems) {
                boolean found = false;
                for (Track track : items) {
                    if (track.getFile().equals(item.getFile())) {
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
        return items[index];
    }

    public int size() {
        return items.length;
    }

    public void next() throws EndPlayListException {
        if (currentIndex == items.length - 1 && looping && !shuffling) {
            currentIndex = 0;
            reset(); // Rest the play list
        } else if (shuffling) {
            int index = currentIndex;
            if (items[currentIndex].getNextTrackIndex() == -1) {
                shuffle(); // Shuffle if the next track index is -1
                items[index].setNextTrackIndex(currentIndex); // Set the previous track as the next track
                items[currentIndex].setPreviousTrackIndex(index); // Set the current track as the previous track
            } else {
                currentIndex = items[currentIndex].getNextTrackIndex();
            }
        } else if (currentIndex == items.length - 1 && !looping) {
            throw new EndPlayListException();
        } else {
            if (currentIndex < items.length - 1) {
                currentIndex++;
            }
            if (currentIndex - 1 > items.length - 1) {
                items[currentIndex - 1].setNextTrackIndex(currentIndex); // Set the previous track as the next track
                items[currentIndex].setPreviousTrackIndex(currentIndex - 1); // Set the next track as the previous track
            }
        }
    }

    public void previous() throws EndPlayListException {
        if (currentIndex == 0 && looping && !shuffling) {
            currentIndex = items.length - 1;
            reset(); // Reset the play list
        } else if (shuffling) {
            int index = currentIndex;
            if (items[currentIndex].getPreviousTrackIndex() == -1) {
                shuffle(); // Shuffle if the previous track is null
                items[index].setPreviousTrackIndex(currentIndex); // Set the next track as the previous track
                items[currentIndex].setNextTrackIndex(index); // Set the current track as the next track
            } else {
                currentIndex = items[currentIndex].getPreviousTrackIndex();
            }
        } else if (currentIndex <= 0 && !looping) {
            throw new EndPlayListException();
        } else {
            if (currentIndex > 0) {
                currentIndex--;
            }
            if (currentIndex + 1 < items.length - 1) {
                items[currentIndex + 1].setPreviousTrackIndex(currentIndex); // Set the next track as the previous track
                items[currentIndex].setNextTrackIndex(currentIndex + 1); // Set the previous track as the next track
            }
        }
    }

    // TODO: Re Implement shuffle
    public PlayList shuffle() throws EndPlayListException {
        int randomIndex = currentIndex;
        while (randomIndex == currentIndex) {
            randomIndex = (int) (Math.random() * items.length);
        }
        // Check if the random index is played before
        boolean end = false;
        if (items[randomIndex].isPlayed() && !(end = isEnded())) {
            return shuffle();
        } else if (end && looping) {
            reset();
            return shuffle();
        } else if (end) {
            throw new EndPlayListException();
        }
        currentIndex = randomIndex;
        return this;
    }

    public void played() {
        items[currentIndex].setPlayed(true);
        items[currentIndex].setPlaying(false);
    }

    public void reset() {
        for (Track item : items) {
            item.setPlayed(false);
            item.setNextTrackIndex(-1);
            item.setPreviousTrackIndex(-1);
        }
        currentIndex = 0;
    }

    public boolean isEnded() {
        boolean ended = true;
        for (Track item : items) {
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
        if (items.length > 0) {
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
        for (int i = len; i < items.length; i++) {
            lineSplat = printItem(lineSplat, i);
        }
    }

    private String printItem(String lineSplat, int i) {
        Track item = getItems()[i];
        Formatter formatter = new Formatter();
        formatter.format("| %2s%-3d | %" + -(longFileNameLength + "[Played] ".length()) + "s |\n",
                (item.isPlaying() ? "> " : ""), i + 1, (item.isPlayed() ? "[Played] " : "") + item.getFile().getName());
        if (lineSplat == null)
            lineSplat = createLineSplat(formatter);
        System.out.print(formatter + lineSplat);
        return lineSplat;
    }

    private String createLineSplat(Formatter formatter) {
        return "+" + "-".repeat(formatter.toString().length() - 3) + "+" + "\n";
    }

    public File playCurrentTrack() {
        items[currentIndex].setPlaying(true);
        return items[currentIndex].getFile();
    }

    public boolean isShuffling() {
        return shuffling;
    }

    public void setShuffling(boolean shuffling) {
        this.shuffling = shuffling;
    }

    public int search(String name) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getFile().getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentIndex() {
        if (items.length > 0)
            return currentIndex;
        return -1;
    }

    public Track[] getItems() {
        return items;
    }

    public int getLongFileNameLength() {
        return longFileNameLength;
    }

    private void setLongFileNameLength(int longFileNameLength) {
        this.longFileNameLength = longFileNameLength;
    }

    public Track getCurrentTrack() {
        return items[currentIndex];
    }

    public String getName() {
        return name + (namePrefix == 0 ? "" : "_" + namePrefix);
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(int namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setNameAndPrefix(String name, int namePrefix) {
        setName(name);
        setNamePrefix(namePrefix);
    }

    public String getPlayListInfo() {
        Formatter formatter = new Formatter();
        formatter.format("%s %d track", getName(), getItems().length);
        return formatter.toString();
    }

    @Override
    public String toString() {
        return getPlayListInfo();
    }
}
