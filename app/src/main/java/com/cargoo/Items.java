package com.cargoo;

public class Items {
    private String itemID;
    private String orderID;
    private String itemName;
    private int quantity;
    private int unit;
    private int width;
    private int length;
    private int height;
    private int weight;
    private int volume;
    private boolean fragileStatus;

    public Items(String itemID, String orderID, String itemName, int quantity, int unit, int width, int length, int height, int weight, int volume, boolean fragileStatus) {
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

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isFragileStatus() {
        return fragileStatus;
    }

    public void setFragileStatus(boolean fragileStatus) {
        this.fragileStatus = fragileStatus;
    }
}
