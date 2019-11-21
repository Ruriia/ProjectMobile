package com.cargoo;

public class Users {
    private String userID;
    private String name;
    private String phone;
    private String email;
    private String pass;
    private String authID;


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

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }
}
