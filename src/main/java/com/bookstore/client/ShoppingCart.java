package com.bookstore.client;

import com.bookstore.Book;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa reprezentująca koszyk zakupów w aplikacji sklepu internetowego.
 */
public class ShoppingCart {
    private static ShoppingCart instance = null;
    private final Map<Book, Integer> cartItems;

    /**
     * Konstruktor prywatny klasy ShoppingCart. Jest wywoływany tylko raz przy tworzeniu instancji koszyka zakupów (Singleton).
     */
    private ShoppingCart() {
        cartItems = new HashMap<>();
    }

    /**
     * Metoda dostarcza instancję koszyka zakupów (Singleton).
     *
     * @return Instancja koszyka zakupów.
     */
    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    /**
     * Dodaje książkę do koszyka zakupów.
     *
     * @param book Książka, która ma zostać dodana do koszyka.
     */
    public void addBook(Book book) {
        cartItems.put(book, cartItems.getOrDefault(book, 0) + 1);
    }

    /**
     * Usuwa książkę z koszyka zakupów.
     *
     * @param book Książka, która ma zostać usunięta z koszyka.
     */
    public void removeBook(Book book) {
        if (cartItems.containsKey(book)) {
            int count = cartItems.get(book);
            if (count > 1) {
                cartItems.put(book, count - 1);
            } else {
                cartItems.remove(book);
            }
        }
    }

    /**
     * Pobiera zawartość koszyka zakupów.
     *
     * @return Mapa zawierająca książki i ich ilości w koszyku.
     */
    public Map<Book, Integer> getCartItems() {
        return cartItems;
    }

    /**
     * Czyści koszyk zakupów, usuwając wszystkie pozycje.
     */
    public void clearCart() {
        cartItems.clear();
    }
}