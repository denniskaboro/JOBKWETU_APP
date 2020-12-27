package com.e.jobkwetu.Model;

public class Inspired_model {
    String image,descroption;
    Boolean favourite;
    Integer cost,votes;
    Double ratting;

    public Inspired_model() {
    }

    public Inspired_model(String image, String descroption, Boolean favourite, Integer cost, Integer votes, Double ratting) {
        this.image = image;
        this.descroption = descroption;
        this.favourite = favourite;
        this.cost = cost;
        this.votes = votes;
        this.ratting = ratting;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescroption() {
        return descroption;
    }

    public void setDescroption(String descroption) {
        this.descroption = descroption;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Double getRatting() {
        return ratting;
    }

    public void setRatting(Double ratting) {
        this.ratting = ratting;
    }
}
