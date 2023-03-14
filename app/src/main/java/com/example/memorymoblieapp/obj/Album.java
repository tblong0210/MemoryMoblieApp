package com.example.memorymoblieapp.obj;

public class Album {
    private String name;
    private int quantity;
    private int img;

    public Album(String name, int quantity, int img) {
        this.name = name;
        this.quantity = quantity;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
