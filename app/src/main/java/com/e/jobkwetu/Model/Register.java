package com.e.jobkwetu.Model;

import java.io.Serializable;

public class Register implements Serializable {
    String id;
    String phone;
    String email;
    String name;
    String token;

    public Register(String id, String phone, String email, String name ,String token) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.token = token;
    }



    public Register() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
