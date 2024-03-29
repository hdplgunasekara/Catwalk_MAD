package com.example.myapplication.Model;

import java.util.UUID;

public class Inquiry {
    String title;
    Boolean isPreviousSubmitted;
    String about;
    String description;
    String imgPath;
    String id;
    String userNumber;
    String userType;

    public Inquiry(String title, Boolean isPreviousSubmitted, String about, String description, String imgPath, String userNumber, String userType) {
        this.title = title;
        this.isPreviousSubmitted = isPreviousSubmitted;
        this.about = about;
        this.description = description;
        this.imgPath = imgPath;
        this.id = UUID.randomUUID().toString();
        this.userNumber = userNumber;
        this.userType = userType;
    }

    public Inquiry() {

    }

    public Inquiry(String id, String title, Boolean isPreviousSubmitted, String about, String description, String imgPath, String userNumber, String userType) {
        this.id = id;
        this.title = title;
        this.isPreviousSubmitted = isPreviousSubmitted;
        this.about = about;
        this.description = description;
        this.imgPath = imgPath;
        this.id = UUID.randomUUID().toString();
        this.userNumber = userNumber;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getPreviousSubmitted() {
        return isPreviousSubmitted;
    }

    public void setPreviousSubmitted(Boolean previousSubmitted) {
        isPreviousSubmitted = previousSubmitted;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
