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

//    private String alamatPengirim;
//    private String provinsiPengirim;
//    private String kotaPengirim;
//    private String kecamatanPengirim;
//    private int kodePosPengirim;

    private String namaPenerima;
    private String emailPenerima;
    private String telpPenerima;

//    private String alamatPenerima;
//    private String provinsiPenerima;
//    private String kotaPenerima;
//    private String kecamatanPenerima;
//    private int kodePosPenerima;

    private String deliveryDate;

    public Order(){

    }

    public Order(String orderID, String userID, String orderDate, String orderStatus, String namaPengirim, String emailPengirim, String telpPengirim, String namaPenerima, String emailPenerima, String telpPenerima, String deliveryDate) {
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
}