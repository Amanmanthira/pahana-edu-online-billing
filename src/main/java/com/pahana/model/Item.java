package com.pahana.model;
public class Item {
    private int id;
    private String name;
    private double price;
    public Item() {}
    public Item(int i, String n, double p) { id = i; name = n; price = p; }
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}