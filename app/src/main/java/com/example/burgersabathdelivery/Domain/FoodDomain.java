package com.example.burgersabathdelivery.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {

    private String title;
    private String description;
    private String picUrl;
    private Double price;
    private int numCart;

    public FoodDomain(String title, String description,String picUrl, Double price) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumCart() {
        return numCart;
    }

    public void setNumCart(int numCart) {
        this.numCart = numCart;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
