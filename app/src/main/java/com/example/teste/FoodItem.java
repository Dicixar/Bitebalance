package com.example.teste;

public class FoodItem {
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private int id;

    public FoodItem(int id, String name, String description, double price, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }
}