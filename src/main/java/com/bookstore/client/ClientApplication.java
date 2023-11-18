package com.bookstore.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(ClientApplication.class.getResource("Login.fxml"));
        String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();

        Scene scene = new Scene(root.load(), 1024, 600);
        scene.getStylesheets().add(css);

        stage.setTitle("Czytaj Z Pasją");
        stage.setScene(scene);
        stage.show();
    }
}