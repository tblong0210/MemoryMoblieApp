package com.example.memorymoblieapp.local_data_storage;

import java.util.ArrayList;

public class Album {
    private String albumName;
    private ArrayList<String> pathImages;

    public Album(String albumName) {
        this.albumName = albumName;
        pathImages = new ArrayList<>();
    }

    public Album(String albumName, ArrayList<String> pathImage) {
        this.albumName = albumName;
        this.pathImages = pathImage;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<String> getPathImages() {
        return pathImages;
    }

    public void setPathImages(ArrayList<String> pathImage) {
        this.pathImages = pathImage;
    }

    public boolean insertNewImage(String pathImage) {
        if (!pathImages.contains(pathImage)) {
            this.pathImages.add(pathImage);
            return true;
        }
        return false;
    }

    public boolean deleteImage(String pathImage) {
        return pathImages.remove(pathImage);
    }
}
