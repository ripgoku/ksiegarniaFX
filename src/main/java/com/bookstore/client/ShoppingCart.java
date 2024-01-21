package com.bookstore.client;

import com.bookstore.Book;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance = null;
    private Map<Book, Integer> cartItems;

    private ShoppingCart() {
        cartItems = new HashMap<>();
    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addBook(Book book) {
        cartItems.put(book, cartItems.getOrDefault(book, 0) + 1);
    }

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

    public Map<Book, Integer> getCartItems() {
        return cartItems;
    }
}