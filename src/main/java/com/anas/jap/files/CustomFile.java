package com.anas.jap.files;

import java.io.File;

public class CustomFile extends File {
    private int index;
    public CustomFile(String pathname, int index) {
        super(pathname);
        setIndex(index);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
