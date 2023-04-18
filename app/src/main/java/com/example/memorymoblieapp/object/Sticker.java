package com.example.memorymoblieapp.object;

import android.graphics.Bitmap;

public class Sticker {
    private String name;
    private Bitmap imageResource;

    public Sticker(String name, Bitmap imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }

    public void setImageResource(Bitmap imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "name='" + name + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }
}
