package com.cargoo;

public class Items {
    private String itemID;
    private String orderID;
    private String itemName;
    private int quantity;
    private String unit;
    private float width;
    private float length;
    private float height;
    private float weight;
    private float volume;
    private int itemPrice;
    private boolean fragileStatus;

    public Items(String itemID, String orderID, String itemName, int quantity, String unit, float width, float length, float height
            , float weight, float volume, int itemPrice, boolean fragileStatus) {
        this.itemID = itemID;
        this.orderID = orderID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.width = width;
        this.length = length;
        this.height = height;
        this.weight = weight;
        this.volume = volume;
        this.itemPrice = itemPrice;
        this.fragileStatus = fragileStatus;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isFragileStatus() {
        return fragileStatus;
    }

    public void setFragileStatus(boolean fragileStatus) {
        this.fragileStatus = fragileStatus;
    }

    public Items(){

    }
}
