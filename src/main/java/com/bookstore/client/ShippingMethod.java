package com.bookstore.client;

public class ShippingMethod {
    String name;
    double price;

    public ShippingMethod(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + "\t" + price + " zł";
    }
}
