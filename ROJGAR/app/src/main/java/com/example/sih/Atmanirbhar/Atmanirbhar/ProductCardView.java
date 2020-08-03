package com.example.sih.Atmanirbhar.Atmanirbhar;


public class ProductCardView {
    private String ProductName;
    private String Description;
    private String Phone;
    private String Location;
    private String Price;

    public ProductCardView() {
    }

    public ProductCardView(String productname, String description, String price, String phone, String location) {
        ProductName = productname;
        Description = description;
        Location = location;
        Phone = phone;
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getLocation() {
        return Location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhone() {
        return Phone;
    }

    public String getPrice() {
        return Price;
    }

}