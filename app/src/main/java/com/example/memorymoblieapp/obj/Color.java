package com.example.memorymoblieapp.obj;

public class Color {
    private String name;
    private int color_type;

    public Color(String name, int color_type) {
        this.name = name;
        this.color_type = color_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorType() {
        return color_type;
    }

    public void setColorType(int color_type) {
        this.color_type = color_type;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", color_type=" + color_type +
                '}';
    }
}
