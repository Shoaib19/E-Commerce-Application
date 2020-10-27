package com.shoaib.ecommerce.Model;

public class BuyerAllOrders {
    private String pname;
    private String price;
    private String quantity;
    private String discount;
    private String sid;
    private String image;
    private String date;
    private String state;
    private String address;
    private String time;
    private String pincode;
    private String name;
    private String city;

    public BuyerAllOrders(String pname, String price, String quantity,
                          String discount, String sid, String image, String date, String state, String address,
                          String time, String pincode, String name, String city) {
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.sid = sid;
        this.image = image;
        this.date = date;
        this.state = state;
        this.address = address;
        this.time = time;
        this.pincode = pincode;
        this.name = name;
        this.city = city;
    }



    public BuyerAllOrders() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}