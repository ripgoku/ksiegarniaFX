package com.bookstore.client;

import java.net.*;
import java.io.*;

public class ClientConnector extends ClientApplication {
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream out;

    public ClientConnector(String address, int port) {
        // we try to establish a connection
        try {
            // creates a socket with the given information
            socket = new Socket(address, port);
            System.out.println("Connected");

            // we 'ready' the input reader
            input = new BufferedReader(new InputStreamReader(System.in));

            // and the output that is connected to the Socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Stop" is input
        while (!line.equals("Stop")) {
            try {
                line = input.readLine(); // reads the line from the keyboard

                out.writeUTF(line); // writes it to the output stream
                // now we just need to collect the data  from the socket on our server
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}
