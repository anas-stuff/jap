package com.anas.code.playlist;

public class PlayList {
    private ListItem[] list;
    private int currentIndex;
    private boolean looping;


    public PlayList() {
        list = new ListItem[0];
    }

    public void add(ListItem item) {
        item.setPlayed(false);
        item.setIndex(list.length - 1);
        if (!contains(item)) {
            ListItem[] newList = new ListItem[list.length + 1];
            System.arraycopy(list, 0, newList, 0, list.length);
            newList[newList.length - 1] = item;
            list = newList;
        }
    }

    private boolean contains(ListItem item) {
        for (ListItem listItem : list) {
            if (listItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public void addAll(ListItem[] items) {
        ListItem[] newList = deleteContains(items);
        newList = setUpItems(newList);
        newList = new ListItem[list.length + newList.length];
        System.arraycopy(list, 0, newList, 0, list.length);
        System.arraycopy(items, 0, newList, list.length, items.length);
        list = newList;
    }

    private ListItem[] setUpItems(ListItem[] newList) {
        for (int i = 0; i < newList.length; i++) {
            newList[i].setIndex(i + list.length); // Set the index of the item
            newList[i].setPlayed(false); // Set the item as not played
        }
        return newList;
    }

    public void remove(int index) {
        // Remove the item at the specified index
        ListItem[] newList = new ListItem[list.length - 1];
        System.arraycopy(list, 0, newList, 0, index);
        System.arraycopy(list, index + 1, newList, index, list.length - index - 1);
        list = newList;
    }

    private ListItem[] deleteContains(ListItem[] newItems) {
        ListItem[] newList = new ListItem[1];
        if (list.length > 0) {
            for (int i = 0; i < newItems.length; i++) {
                ListItem item = newItems[i];
                boolean found = false;
                for (int j = 0; j < list.length; j++) {
                    if (list[j].equals(item)) {
                        found = true;
                        break;
                    }
                }
                if (!found) { // If not found, add to new list
                    ListItem[] newList2 = new ListItem[newList.length + 1];
                    newList2[newList2.length - 1] = item;
                    System.arraycopy(newList, 0, newList2, 0, newList.length);
                }
            }
        } else {
            newList = newItems; // If no items in list, just add the new items
        }
        return newList;
    }

    public ListItem get(int index) {
        return list[index];
    }

    public int size() {
        return list.length;
    }

    public ListItem getNextItem() {
        if (currentIndex == list.length - 1) {
            currentIndex = 0;
        }
        return list[currentIndex++];
    }

    public ListItem getPreviousItem() {
        if (currentIndex == 0) {
            currentIndex = list.length - 1;
        }
        return list[currentIndex--];
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
        for (ListItem item : list) {
            item.setPlayed(false);
        }
    }

    public boolean isEnded() {
        boolean ended = true;
        for (ListItem item : list) {
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
        for (ListItem listItem : list) {
            System.out.println((listItem.getIndex() + 1) + ": " + listItem.getFileName());
        }
    }
}
