package com.example.teste;

public class FoodItem {
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private int id;
    private int calories;
    private int carbohydrates;
    private int proteins;
    private int fats;

    public FoodItem(int id, String name, String description, double price, int imageResource, int calories, int carbohydrates, int proteins, int fats) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.id = id;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;


    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }
    public int getCalories() { return calories; }
    public int getCarbohydrates() { return carbohydrates; }
    public int getProteins() { return proteins; }
    public int getFats() { return fats; }

}