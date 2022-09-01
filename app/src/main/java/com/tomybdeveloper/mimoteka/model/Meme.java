package com.tomybdeveloper.mimoteka.model;

import com.google.firebase.Timestamp;

public class Meme {

    private String title;
    private String userId;
    private String description;
    private Timestamp timeAdded;
    private String imageUrl;
    private String userName;

    public Meme() {
    }

    public Meme(String title, String userId, String description, Timestamp timeAdded, String imageUrl, String userName) {
        this.title = title;
        this.userId = userId;
        this.description = description;
        this.timeAdded = timeAdded;
        this.imageUrl = imageUrl;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
