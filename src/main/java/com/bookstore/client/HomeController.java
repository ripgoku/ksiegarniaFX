package com.bookstore.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private AnchorPane banner;
    @FXML
    private AnchorPane categoryPanel;
    @FXML
    private Button bannerButton;
    @FXML
    private ListView<String> categoryListView;
    String[] category = {"albumy", "biografie", "biznes", "dla dzieci", "dla młodzieży", "encyklopedie i słowniki", "ezoteryka",
            "fantastyka", "historia", "informatyka", "komiksy", "kryminał i sensacja", "kultura i sztuka", "lektury",
            "literatura faktu", "literatura piękna", "literatura popularnonaukowa", "nauka języków",
            "nauki humanistyczne", "nauki ścisłe", "podróże i turystyka", "poezja i dramat", "poradniki",
            "prasa", "prawo", "proza obyczajowa", "religia", "rozrywka i humor", "sport", "young adult"};
    String currentCategory;
    ServerConnection serverConnection;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryListView.getItems().addAll(category);
    }
}
