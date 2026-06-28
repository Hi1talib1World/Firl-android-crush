package com.Denzo.firl.Model;

public class ActiveUser {
    private String name;
    private int imageResource;
    private String imageUrl;

    public ActiveUser(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public ActiveUser(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageResource = 0;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
