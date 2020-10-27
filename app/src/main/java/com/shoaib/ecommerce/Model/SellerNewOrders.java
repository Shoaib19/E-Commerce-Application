package com.shoaib.ecommerce.Model;

public class SellerNewOrders {
    private String name, phone, address, date, pname,quantity,price,image,city,pincode,state;

    public SellerNewOrders(){

    }

    public SellerNewOrders(String name, String phone, String address, String date, String time, String pname, String quantity, String price, String city, String pincode, String state) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.pname = pname;
        this.quantity = quantity;
        this.price = price;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
