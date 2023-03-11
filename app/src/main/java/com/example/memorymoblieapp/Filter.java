package com.example.memorymoblieapp;

public class Filter {
    private String name;


    private int filterResource;

    public Filter(String name, int filterResource) {
        this.name = name;
        this.filterResource = filterResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFilterResource() {
        return filterResource;
    }

    public void setFilterResource(int filterResource) {
        this.filterResource = filterResource;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", imageResource=" + filterResource +
                '}';
    }
}
