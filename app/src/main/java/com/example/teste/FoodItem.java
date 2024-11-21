package com.example.teste;

public class FoodItem {
    private String name;
    private String description;
    private double price;
    private int imageResource;

    public FoodItem(String name, String description, double price, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }
}