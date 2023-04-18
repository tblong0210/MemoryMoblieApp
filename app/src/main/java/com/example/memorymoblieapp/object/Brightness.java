package com.example.memorymoblieapp.object;

public class Brightness {
    private String name;
    private int brightnessResource;

    public Brightness(String name, int brightnessResource) {
        this.name = name;
        this.brightnessResource = brightnessResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightnessResource() {
        return brightnessResource;
    }

    public void setBrightnessResource(int brightnessResource) {
        this.brightnessResource = brightnessResource;
    }
}
