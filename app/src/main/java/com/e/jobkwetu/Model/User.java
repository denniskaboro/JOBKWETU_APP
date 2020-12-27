package com.e.jobkwetu.Model;

import java.io.Serializable;

public class User implements Serializable {
    String id, phone, name,token,profile,email;

    public User() {
    }

    public User(String id, String phone, String name,String token,String profile,String email) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.token = token;
        this.profile = profile;
        this.email = email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
