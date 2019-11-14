package com.cargoo;

public class Users {
    private String userID;
    private String name;
    private String phone;
    private String email;
    private String pass;


    private String address;
    private String province;
    private String city;
    private String district;
    private int zipcode;

    public Users(){

    }

    public Users(String userID, String name, String phone, String mail, String pass) {
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.email = mail;
        this.pass = pass;
    }

    public Users(String address, String province, String city, String district, int zip) {
        this.address = address;
        this.province = province;
        this.city = city;
        this.district = district;
        this.zipcode = zip;
    }

    public Users(String address, String province, String city, String district) {
        this.address = address;
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public int getZipcode() {
        return zipcode;
    }
}
