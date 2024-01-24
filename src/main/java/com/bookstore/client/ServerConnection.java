package com.bookstore.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Klasa reprezentująca połączenie klienta z serwerem.
 */
public class ServerConnection {
    private final String address;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Konstruktor klasy ServerConnection.
     *
     * @param address Adres serwera, z którym będzie nawiązywane połączenie.
     * @param port    Port serwera, na którym działa usługa.
     */
    public ServerConnection(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Metoda nawiązująca połączenie z serwerem.
     *
     * @throws IOException Wyjątek, jeśli nie można nawiązać połączenia.
     */
    public void connect() throws IOException {
        socket = new Socket(address, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Metoda wysyłająca wiadomość do serwera.
     *
     * @param message Obiekt wiadomości do wysłania.
     * @throws IOException Wyjątek, jeśli nie można wysłać wiadomości.
     */
    public void sendMessage(Object message) throws IOException {
        output.writeObject(message);
        output.flush();
    }

    /**
     * Metoda odbierająca wiadomość od serwera.
     *
     * @return Odebrany obiekt wiadomości.
     * @throws IOException            Wyjątek, jeśli nie można odebrać wiadomości.
     * @throws ClassNotFoundException Wyjątek, jeśli klasa obiektu nie jest znana.
     */
    public Object receiveMessage() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    /**
     * Metoda zamykająca połączenie z serwerem oraz strumienie wejścia i wyjścia.
     *
     * @throws IOException Wyjątek, jeśli nie można zamknąć połączenia lub strumieni.
     */
    public void close() throws IOException {
        if (input != null) input.close();
        if (output != null) output.close();
        if (socket != null) socket.close();
    }
}
