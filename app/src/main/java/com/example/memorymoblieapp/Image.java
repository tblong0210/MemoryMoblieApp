package com.example.memorymoblieapp;

public class Image {
    private String name;


    private int imageResource;

    public Image(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }
}
