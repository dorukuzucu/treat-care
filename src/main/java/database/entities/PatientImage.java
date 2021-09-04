package main.java.database.entities;

import java.io.InputStream;

public class PatientImage {
    Double id;
    Double userId;
    InputStream image;
    int imageFileLength;

    public PatientImage(Double id, Double userId, InputStream image, int imageFileLength){
        this.id = id;
        this.userId = userId;
        this.image = image;
        this.imageFileLength = imageFileLength;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getId() {
        return this.id;
    }

    public void setUserId(Double userId) {
        this.userId = userId;
    }

    public Double getUserId() {
        return this.userId;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public InputStream getImage() {
        return this.image;
    }

    public void setLength(int imageFileLength) {
        this.imageFileLength = imageFileLength;
    }

    public int getLength() {
        return imageFileLength;
    }

    public String toString(){
        return String.format("Image of -> id: %.0f, userid: %.0f",id,userId);
    }
}
