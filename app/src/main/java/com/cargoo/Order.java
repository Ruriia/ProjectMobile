package com.cargoo;

import java.util.Date;

public class Order {
    private String orderID;
    private String userID;
    private String orderDate;
    private String orderStatus;
    private String namaPengirim;
    private String emailPengirim;
    private String telpPengirim;

    private String namaPenerima;
    private String emailPenerima;
    private String telpPenerima;

    private String deliveryDate;

    private float distance;
    private int deliveryPrice;
    private int itemPrice;

    private float totalWeight;
    private float totalVolume;
    private int totalPrice;


    public Order(){

    }

    public Order(String orderID, String userID, String orderDate, String orderStatus, String namaPengirim, String emailPengirim, String telpPengirim, String namaPenerima, String emailPenerima, String telpPenerima, String deliveryDate, float distance, int deliveryPrice, float totalWeight, float totalVolume, int itemPrice, int totalPrice) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.namaPengirim = namaPengirim;
        this.emailPengirim = emailPengirim;
        this.telpPengirim = telpPengirim;
        this.namaPenerima = namaPenerima;
        this.emailPenerima = emailPenerima;
        this.telpPenerima = telpPenerima;
        this.deliveryDate = deliveryDate;
        this.distance = distance;
        this.deliveryPrice = deliveryPrice;
        this.totalWeight = totalWeight;
        this.totalVolume = totalVolume;
        this.itemPrice = itemPrice;
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public void setNamaPengirim(String namaPengirim) {
        this.namaPengirim = namaPengirim;
    }

    public String getEmailPengirim() {
        return emailPengirim;
    }

    public void setEmailPengirim(String emailPengirim) {
        this.emailPengirim = emailPengirim;
    }

    public String getTelpPengirim() {
        return telpPengirim;
    }

    public void setTelpPengirim(String telpPengirim) {
        this.telpPengirim = telpPengirim;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getEmailPenerima() {
        return emailPenerima;
    }

    public void setEmailPenerima(String emailPenerima) {
        this.emailPenerima = emailPenerima;
    }

    public String getTelpPenerima() {
        return telpPenerima;
    }

    public void setTelpPenerima(String telpPenerima) {
        this.telpPenerima = telpPenerima;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public float getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(float totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}