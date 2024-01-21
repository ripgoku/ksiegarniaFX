package com.bookstore.client;

public class LoggedUser {
    private static LoggedUser instance;

    private int customerId;
    private String firstName;
    private String lastName;
    private int adresId;
    private String city;
    private String postalCode;
    private String street;
    private String houseNumber;

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    public int getCustomerId() {
        return customerId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getHouseNumber() {
        return houseNumber;
    }
    public String getStreet() {
        return street;
    }
    public String getCity() {
        return city;
    }
    public int getAdresId() {
        return adresId;
    }

    public void setUser(int customerId, int adresId, String firstName, String lastName, String postalCode,
                        String city, String street, String houseNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adresId = adresId;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
