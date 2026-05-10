package com.vigilant.vigilant_shop.model;

// 2. Classes, Objects, and Methods
// 3. Inheritance (Implements Product Interface)
public class ClothItem {
    private int id;
    private String name;
    private double price;
    private String category;
    private String imageUrl;
    private String availableSizes;

    // 6. Parameters and Overloading (Constructors)
    public ClothItem() {}

    public ClothItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ClothItem(int id, String name, double price, String category, String imageUrl, String availableSizes) {
        this(id, name, price); // Reusing constructor
        this.category = category;
        this.imageUrl = imageUrl;
        this.availableSizes = availableSizes;
    }

    // 8. Polymorphism: Specific logic for price calculation
    public double calculatePrice(int quantity) {
        return this.price * quantity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAvailableSizes() { return availableSizes; }
    public void setAvailableSizes(String availableSizes) { this.availableSizes = availableSizes; }
}
