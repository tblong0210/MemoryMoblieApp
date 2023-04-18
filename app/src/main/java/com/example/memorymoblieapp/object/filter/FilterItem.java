package com.example.memorymoblieapp.object.filter;

public class FilterItem {
    private String name;
    private float[] colorMatrix;

    private Boolean isActivated;

    public FilterItem() {
        isActivated = false;
    }

    public FilterItem(String name, float[] colorMatrix, Boolean isActived) {
        this.name = name;
        this.colorMatrix = colorMatrix;
        this.isActivated = isActived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getColorMatrix() {
        return colorMatrix;
    }

    public void setColorMatrix(float[] colorMatrix) {
        this.colorMatrix = colorMatrix;
    }

    public Boolean getActived() {
        return isActivated;
    }

    public void setActivated(Boolean actived) {
        isActivated = actived;
    }
}
