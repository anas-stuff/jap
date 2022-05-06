package com.anas.jconsoleaudioplayer.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayList implements Serializable {
    private String name;
    private int namePrefix;
    private int currentIndex;
    private boolean looping, shuffling;
    private int longFileNameLength;
    private int length;
    private final ArrayList<Track> notPlayedTracks,
                                   playedTracks;

    private PlayList() {
        this("", 0);
    }

    protected PlayList(String name, int namePrefix) {
        this.name = name;
        this.namePrefix = namePrefix;
        currentIndex = 0;
        looping = false;
        shuffling = false;
        length = 0;
        notPlayedTracks = new ArrayList<>();
        playedTracks = new ArrayList<>();
        setLongFileNameLength(0);
    }

    public void add(Track item) {
        if (!this.contains(item)) {
            notPlayedTracks.add(item);
            length++;
        }
    }

    private boolean contains(Track item) {
        return notPlayedTracks.contains(item) || playedTracks.contains(item);
    }

    public void addAll(Track[] items) {
        if (items != null && items.length > 0) {
            Track[] newList = deleteContains(items);
            if (newList != null && newList.length > 0) {
                setUpItems(newList);
                notPlayedTracks.addAll(Arrays.asList(newList));
                setLongFileNameLength(updateLongFileNameLength(newList));
                updateLength();
            }
        }
    }

    private void updateLength() {
        length = notPlayedTracks.size() + playedTracks.size();
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
            newList[i].setIndex(i + this.length); // Set the index of the item
        }
    }

    public void remove(int index) {
        if (index < this.length) {
            // Remove the item at the specified index
            getCorrectList(index).remove(index);
            length--;
        }
    }

    private ArrayList<Track> getCorrectList(int index) {
        if (index > playedTracks.size() - 1) {
            return notPlayedTracks;
        } else {
            return playedTracks;
        }
    }

    private Track[] deleteContains(Track[] newItems) {
        Track[] newList = new Track[0];
        if (this.length > 0) {
            for (Track item : newItems) {
                boolean found = false;
                for (Track track : this.getItems()) {
                    if (track.getFilePath().equals(item.getFilePath())) {
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
        if (getCorrectList(index).equals(notPlayedTracks)) {
            return notPlayedTracks.get(index - playedTracks.size());
        } else {
            return playedTracks.get(index);
        }
    }

    public int size() {
        return this.length;
    }

    public void next() throws EndPlayListException {
        if (currentIndex == this.length - 1 && looping && !shuffling) {
            currentIndex = 0;
            reset(); // Rest the play list
        } else if (shuffling) {
            int index = currentIndex; // Save the current index before changing it (with shuffle)
            if (get(index).getNextTrackIndex() == -1) {
                shuffle(); // Shuffle if the next track index is -1
                get(index).setNextTrackIndex(currentIndex); // Set the previous track as the next track
                get(index).setPreviousTrackIndex(index); // Set the current track as the previous track
            } else {
                currentIndex = get(currentIndex).getNextTrackIndex();
            }
        } else if (currentIndex == this.length - 1 && !looping) {
            throw new EndPlayListException();
        } else {
            if (currentIndex < this.length - 1) {
                currentIndex++;
            }
            if (currentIndex - 1 > this.length - 1) {
                get(currentIndex - 1).setNextTrackIndex(currentIndex); // Set the previous track as the next track
                get(currentIndex).setPreviousTrackIndex(currentIndex - 1); // Set the next track as the previous track
            }
        }
    }

    public void previous() throws EndPlayListException {
        if (currentIndex == 0 && looping && !shuffling) {
            currentIndex = this.length - 1;
            reset(); // Reset the play list
        } else if (shuffling) {
            int index = currentIndex;
            if (get(currentIndex).getPreviousTrackIndex() == -1) {
                shuffle(); // Shuffle if the previous track is null
                get(index).setPreviousTrackIndex(currentIndex); // Set the next track as the previous track
                get(currentIndex).setNextTrackIndex(index); // Set the current track as the next track
            } else {
                currentIndex = get(currentIndex).getPreviousTrackIndex();
            }
        } else if (currentIndex <= 0 && !looping) {
            throw new EndPlayListException();
        } else {
            if (currentIndex > 0) {
                currentIndex--;
            }
            if (currentIndex + 1 < this.length - 1) {
                get(currentIndex + 1).setPreviousTrackIndex(currentIndex); // Set the next track as the previous track
                get(currentIndex).setNextTrackIndex(currentIndex + 1); // Set the previous track as the next track
            }
        }
    }

    public PlayList shuffle() throws EndPlayListException {
        // Check if the play list is ended
        if (ifEnd()) return shuffle();
        // Generate a random index
        int randomIndex = currentIndex;
        while (randomIndex == currentIndex &&
                notPlayedTracks.size() > 0) { // Shuffle until the current index is different
            randomIndex = (int) (Math.random() * notPlayedTracks.size());
        }
        currentIndex = randomIndex;
        return this;
    }

    private boolean ifEnd() throws EndPlayListException {
        // Check if playlist is finished
        boolean end;
        if ((end = isEnded()) && looping) {
            reset();
            return true;
        } else if (end) {
            throw new EndPlayListException();
        }
        return false;
    }

    public void reset() {
        for (int i = 0; i < playedTracks.size(); i++) {
            playedTracks.get(i).setPlaying(false);
            playedTracks.get(i).setPreviousTrackIndex(-1);
            playedTracks.get(i).setNextTrackIndex(-1);
            notPlayedTracks.add(playedTracks.remove(i));
        }
        currentIndex = 0;
    }

    public boolean isEnded() {
        return notPlayedTracks.isEmpty();
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
        if (this.length > 0) {
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
        for (int i = len; i < this.length; i++) {
            lineSplat = printItem(lineSplat, i);
        }
    }

    private String printItem(String lineSplat, int i) {
        Track item = getItems()[i];
        Formatter formatter = new Formatter();
        formatter.format("| %2s%-3d | %" + -(longFileNameLength + "[Played] ".length()) + "s |\n",
                (item.isPlaying() ? "> " : ""), i + 1,
                (getCorrectList(i).equals(playedTracks)? "[Played] " : "") + item.getFile().getName());
        if (lineSplat == null)
            lineSplat = createLineSplat(formatter);
        System.out.print(formatter + lineSplat);
        return lineSplat;
    }

    private String createLineSplat(Formatter formatter) {
        return "+" + "-".repeat(formatter.toString().length() - 3) + "+" + "\n";
    }

    public File playCurrentTrack() {
        getCurrentTrack().setPlaying(true);
        return getCurrentTrack().getFile();
    }

    public boolean isShuffling() {
        return shuffling;
    }

    public void setShuffling(boolean shuffling) {
        this.shuffling = shuffling;
    }

    public int search(String name) {
        for (int i = 0; i < this.length; i++) {
            if (get(i).getFile().getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentIndex() {
        if (this.length > 0)
            return currentIndex;
        return -1;
    }

    public Track[] getItems() {
        ArrayList<Track> items = new ArrayList<>();
        items.addAll(playedTracks);
        items.addAll(notPlayedTracks);
        return items.toArray(new Track[0]);
    }

    public int getLongFileNameLength() {
        return longFileNameLength;
    }

    private void setLongFileNameLength(int longFileNameLength) {
        this.longFileNameLength = longFileNameLength;
    }

    public Track getCurrentTrack() {
        return get(currentIndex);
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

    public void played() {
        playedTracks.add(getCurrentTrack());
        notPlayedTracks.remove(getCurrentTrack());
    }
}
