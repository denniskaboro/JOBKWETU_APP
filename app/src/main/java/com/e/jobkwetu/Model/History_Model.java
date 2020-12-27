package com.e.jobkwetu.Model;

public class History_Model {
    private String name, description ,cost,date,thumbnailUrl;
    private float ratting;

    public History_Model() {
    }

    public History_Model(String name, String description, String cost, String date, String thumbnailUrl,float ratting) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.ratting = ratting;
    }

    public float getRatting() {
        return ratting;
    }

    public void setRatting(float ratting) {
        this.ratting = ratting;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
