package com.e.jobkwetu.Model;

public class Popular_Model {
    private String title,image;
    public Popular_Model() {

    }
    public Popular_Model(String title, String image) {
        this.title = title;
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
