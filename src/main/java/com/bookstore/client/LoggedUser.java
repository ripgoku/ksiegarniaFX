package com.bookstore.client;

/**
 * Klasa LoggedUser to singleton przechowujący informacje o zalogowanym użytkowniku.
 * Zawiera informacje takie jak ID klienta, imię, nazwisko oraz adres.
 * Umożliwia globalny dostęp do danych zalogowanego użytkownika w aplikacji klienta.
 */
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

    /**
     * Zwraca instancję klasy LoggedUser.
     * Jeśli instancja nie istnieje, tworzy nową.
     *
     * @return Instancja LoggedUser.
     */
    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    /**
     * Zwraca identyfikator klienta.
     *
     * @return ID klienta.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Zwraca imię użytkownika.
     *
     * @return Imię użytkownika.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Zwraca nazwisko użytkownika.
     *
     * @return Nazwisko użytkownika.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Zwraca kod pocztowy użytkownika.
     *
     * @return Kod pocztowy.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Zwraca numer domu/mieszkania użytkownika.
     *
     * @return Numer domu/mieszkania.
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Zwraca ulicę użytkownika.
     *
     * @return Ulica.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Zwraca miasto użytkownika.
     *
     * @return Miasto.
     */
    public String getCity() {
        return city;
    }

    /**
     * Zwraca identyfikator adresu użytkownika.
     *
     * @return ID adresu.
     */
    public int getAdresId() {
        return adresId;
    }

    /**
     * Zwraca adres email użytkownika.
     *
     * @return Adres email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Zwraca login użytkownika.
     *
     * @return Login użytkownika.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Ustawia adres email użytkownika.
     *
     * @param email Nowy adres email użytkownika.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Ustawia kod pocztowy użytkownika.
     *
     * @param postalCode Nowy kod pocztowy użytkownika.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Ustawia numer domu/mieszkania użytkownika.
     *
     * @param houseNumber Nowy numer domu/mieszkania użytkownika.
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * Ustawia login użytkownika.
     *
     * @param login Nowy login użytkownika.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Ustawia ulicę użytkownika.
     *
     * @param street Nowa ulica użytkownika.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Ustawia miasto użytkownika.
     *
     * @param city Nowe miasto użytkownika.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Ustawia identyfikator adresu użytkownika.
     *
     * @param adresId Nowy ID adresu użytkownika.
     */
    public void setAdresId(int adresId) {
        this.adresId = adresId;
    }

    /**
     * Ustawia identyfikator klienta.
     *
     * @param customerId Nowy ID klienta użytkownika.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Ustawia imię użytkownika.
     *
     * @param firstName Nowe imię użytkownika.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Ustawia nazwisko użytkownika.
     *
     * @param lastName Nowe nazwisko użytkownika.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Ustawia wszystkie dane zalogowanego użytkownika.
     *
     * @param customerId ID klienta.
     * @param adresId ID adresu.
     * @param firstName Imię użytkownika.
     * @param lastName Nazwisko użytkownika.
     * @param postalCode Kod pocztowy.
     * @param city Miasto.
     * @param street Ulica.
     * @param houseNumber Numer domu/mieszkania.
     * @param email Adres email użytkownika.
     * @param login Login użytkownika.
     */
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
