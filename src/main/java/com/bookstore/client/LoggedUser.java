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
    private String email;
    private String login;

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
    public String getEmail() {
        return email;
    }
    public String getLogin() {
        return login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAdresId(int adresId) {
        this.adresId = adresId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUser(int customerId, int adresId, String firstName, String lastName, String postalCode,
                        String city, String street, String houseNumber, String email, String login) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adresId = adresId;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.login = login;
        this.email = email;
    }
}
