package com.pahana.model;
public class Customer {
    private String accountNo, name, address, phone;
    private int units;
    public Customer() {}
    public Customer(String acc, String name, String address, String phone, int units) {
        this.accountNo = acc; this.name = name; this.address = address;
        this.phone = phone; this.units = units;
    }
    public String getAccountNo() { return accountNo; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public int getUnits() { return units; }
}
