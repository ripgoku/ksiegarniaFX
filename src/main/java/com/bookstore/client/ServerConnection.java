package com.bookstore.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {
    private final String address;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ServerConnection(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(address, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Object message) throws IOException {
        output.writeObject(message);
        output.flush();
    }

    public Object receiveMessage() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public void close() throws IOException {
        if (input != null) input.close();
        if (output != null) output.close();
        if (socket != null) socket.close();
    }
}
