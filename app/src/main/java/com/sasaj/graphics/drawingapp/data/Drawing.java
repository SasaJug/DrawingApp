package com.sasaj.graphics.drawingapp.data;

/**
 * Created by DS on 4/21/2017.
 */

public class Drawing implements Comparable<Drawing>{

    private String imagePath;
    private long lastModified;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int compareTo(Drawing compareDrawing) {

        long compareQuantity = compareDrawing.getLastModified();

        return (int) (compareQuantity -this.getLastModified());

    }
}
