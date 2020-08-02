package com.example.sih.Atmanirbhar.Atmanirbhar;


public class ProductCardView {
    private String ProductName;
    private String Description;
    private String Phone;
    private String Price;

    public ProductCardView() {
    }

    public ProductCardView(String productname, String description, String price, String phone) {
        ProductName = productname;
        Description = description;
        Phone = phone;
        Price = price;
    }

    public String getProductName() {
        return ProductName;
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