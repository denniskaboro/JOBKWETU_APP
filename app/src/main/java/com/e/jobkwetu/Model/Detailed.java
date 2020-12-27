package com.e.jobkwetu.Model;

public class Detailed {
    private String user_id,image,name,description,title,date_joined;
    private Integer level;
    private Double ratting;

    public Detailed() {
    }

    public Detailed(String user_id, String image, String name, String description, String title, String date_joined, Integer level, Double ratting) {
        this.user_id = user_id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.title = title;
        this.date_joined = date_joined;
        this.level = level;
        this.ratting = ratting;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getRatting() {
        return ratting;
    }

    public void setRatting(Double ratting) {
        this.ratting = ratting;
    }
}
