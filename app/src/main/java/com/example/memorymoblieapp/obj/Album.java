package com.example.memorymoblieapp.obj;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Album {
    private String albumName;
    private Boolean block;
    private ArrayList<String> pathImages;

    public Album(String albumName) {
        this.albumName = albumName;
        pathImages = new ArrayList<>();
        block = false;
    }

    public Album(String albumName, ArrayList<String> pathImage) {
        this.albumName = albumName;
        this.pathImages = pathImage;
        block = false;
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

    public void insertNewImageArray(ArrayList<String> pathImages) {
        this.pathImages.addAll(pathImages);
        this.pathImages = removeDuplicates(this.pathImages);
    }

    public void removeImage(String pathImage) {
        this.pathImages.remove(pathImage);
    }

    public void removeImageArray(ArrayList<String> pathImages) {
        this.pathImages.removeAll(pathImages);
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public static ArrayList<String> getAlbumNameArrayList(@NonNull ArrayList<Album> albums) {
        return albums.stream()
                .map(Album::getAlbumName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Set<String> getAlbumNameSet(@NonNull ArrayList<Album> albums) {
        return albums.stream()
                .map(Album::getAlbumName)
                .collect(Collectors.toSet());
    }

    @NonNull
    @Contract("_ -> param1")
    <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
