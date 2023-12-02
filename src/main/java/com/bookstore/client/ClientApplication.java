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
    private ServerConnection serverConnection;

    @Override
    public void start(Stage stage) throws IOException {
        serverConnection = new ServerConnection("127.0.0.1", 6666);
        serverConnection.connect();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setServerConnection(serverConnection);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm());

        stage.setTitle("Czytaj Z Pasją");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/icon.png")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (serverConnection != null) {
            serverConnection.close();
        }
        super.stop();
    }
}