package com.bookstore.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Klasa ClientApplication odpowiada za uruchamianie interfejsu użytkownika klienta aplikacji księgarni.
 * Rozszerza klasę Application z JavaFX i jest punktem startowym dla interfejsu użytkownika klienta.
 */
public class ClientApplication extends Application {
    private ServerConnection serverConnection;

    /**
     * Startuje aplikację klienta, łączy się z serwerem i inicjuje interfejs użytkownika.
     * Ładuje główny widok z pliku FXML, ustawia scenę i pokazuje okno aplikacji.
     *
     * @param stage Główna scena/stage dla aplikacji JavaFX.
     * @throws IOException W przypadku problemów z załadowaniem pliku FXML.
     */
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

    /**
     * Zamyka połączenie z serwerem, gdy aplikacja jest zamykana.
     * Jest to nadpisanie metody stop z klasy Application JavaFX.
     *
     * @throws Exception W przypadku problemów z zamknięciem połączenia.
     */
    @Override
    public void stop() throws Exception {
        if (serverConnection != null) {
            serverConnection.close();
        }
        super.stop();
    }
}