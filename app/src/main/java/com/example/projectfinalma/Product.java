package com.example.projectfinalma;

import android.net.Uri;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class Product {
    private int id, stock;
    private double price;
    private String name, description;
    private String image;

    public Product(String name, String description, double price, int stock, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }


    public int getId() {
        return id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImageUri(String imageUri) {
        this.image = imageUri;
    }
}
