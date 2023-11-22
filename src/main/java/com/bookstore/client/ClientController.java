package com.bookstore.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();

    private void switchScene(String sceneName) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName + ".fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void loginRequest(ActionEvent e) {
        //TODO
        System.out.println("login");
    }

    public void switchToRegister(ActionEvent e) throws IOException {
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        switchScene("Register");
    }

    public void switchToLogin(ActionEvent e) throws IOException {
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        switchScene("Login");
    }
}