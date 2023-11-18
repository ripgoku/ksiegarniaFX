package com.bookstore.server;

import java.net.*;
import java.io.*;

public class Server {

    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started!");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client connected");

            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            String line = "";

            // reads message from client until "Stop" is sent
            while (!line.equals("Stop"))
            {
                try
                {
                    line = in.readUTF();
                    System.out.println("<Client> " + line);

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();

        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
