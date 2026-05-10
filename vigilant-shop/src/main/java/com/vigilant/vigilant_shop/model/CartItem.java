package com.vigilant.vigilant_shop.model;

public class CartItem {
    private ClothItem clothItem;
    private int quantity;

    public CartItem(ClothItem clothItem, int quantity) {
        this.clothItem = clothItem;
        this.quantity = quantity;
    }

    // 8. Polymorphism: Delegating calculation to ClothItem
    public double getTotalPrice() {
        return clothItem.calculatePrice(quantity);
    }

    public ClothItem getClothItem() { return clothItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
