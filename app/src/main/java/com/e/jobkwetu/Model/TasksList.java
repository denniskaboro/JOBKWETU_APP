package com.e.jobkwetu.Model;

public class TasksList {
private  String title,description, skills,start_date,subcounty;
    public TasksList() {
    }


    public TasksList(String title, String description, String skills, String start_date, String subcounty) {
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.start_date = start_date;
        this.subcounty = subcounty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }
}
