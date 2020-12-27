package com.e.jobkwetu.Model;

public class newly_joined_model {
    private String username,image,category,location,date;

    public newly_joined_model() {
    }

    public newly_joined_model(String username, String image, String category, String location, String date) {
        this.username = username;
        this.image = image;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
