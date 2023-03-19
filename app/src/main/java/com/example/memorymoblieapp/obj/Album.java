package com.example.memorymoblieapp.obj;

import java.util.ArrayList;

public class Album {
    private String name;
    private ArrayList<Image> imgList;
    private int img;

    public Album(String name, ArrayList<Image> imgList, int img) {
        this.name = name;
        this.imgList = imgList;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Image> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<Image> imgList) {
        this.imgList = imgList;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
