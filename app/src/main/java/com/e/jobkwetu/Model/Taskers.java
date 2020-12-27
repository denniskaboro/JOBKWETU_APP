package com.e.jobkwetu.Model;

public class Taskers {
    String username,skills,description,date_joined,thumbnailUrl,location;
    int jobber_id;
    float rating;



    public Taskers() {
    }

    public Taskers(String username,int jobber_id, String skills, String description, String date_joined,String thumbnailUrl,String location, float rating) {
        this.username = username;
        this.jobber_id =jobber_id;
        this.skills = skills;
        this.description = description;
        this.date_joined = date_joined;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.rating = rating;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public int getJobber_id() {
        return jobber_id;
    }

    public void setJobber_id(int jobber_id) {
        this.jobber_id = jobber_id;
    }
}
