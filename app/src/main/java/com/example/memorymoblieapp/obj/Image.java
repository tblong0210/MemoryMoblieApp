package com.example.memorymoblieapp.obj;

public class Image {
    private String name;
    private String size;
    private String dimensions;
    private String location;
    private int img;

    public Image(String name, String size, String dimensions, String location, int img) {
        this.name = name;
        this.size = size;
        this.dimensions = dimensions;
        this.location = location;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}