package com.bookstore.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm());

        stage.setTitle("Czytaj Z Pasją");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/icon.png")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}