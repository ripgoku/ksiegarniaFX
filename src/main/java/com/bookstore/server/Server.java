package com.bookstore.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server {
    public Server (int port) throws IOException {
        // listening to port
        ServerSocket serverSocket = new ServerSocket(port);

        // infinite loop for requests
        while(true)
        {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: "+clientSocket);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                Thread t = new ClientHandler(clientSocket, dis, dos);

                t.start();
            } catch (Exception e) {
                Objects.requireNonNull(clientSocket).close();
                e.printStackTrace();
            }
        }
    }
}
