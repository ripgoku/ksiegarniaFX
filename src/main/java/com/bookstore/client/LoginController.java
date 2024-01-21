package com.bookstore.client;

import com.bookstore.LoginData;
import com.bookstore.Message;
import com.bookstore.MessageType;
import com.bookstore.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.bookstore.MessageType.*;

public class LoginController {
    @FXML
    private AnchorPane banner;
    @FXML
    private AnchorPane loginPanel;
    @FXML
    private Label whiteHeadLabel;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginErrorLabel;

    private final String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();
    ServerConnection serverConnection;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @FXML
    public void loginRequest(ActionEvent e) throws IOException, ClassNotFoundException {
        loginErrorLabel.setText("");
        String login = this.login.getText();
        String password = this.password.getText();

        LoginData loginData = new LoginData(login, password);

        Message loginMessage = new Message();
        loginMessage.setType(MessageType.LOGIN);
        loginMessage.setData(loginData);

        // Wysyłanie komunikatu do serwera
        serverConnection.sendMessage(loginMessage);

        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == SERVER_MESSAGE_ERROR) {
            loginErrorLabel.setText((String) serverAnswer.getData());
        } else if (serverAnswer.getType() == LOGIN) {
            UserData userData = (UserData) serverAnswer.getData();
            LoggedUser.getInstance().setUser(userData.getCustomer_id(), userData.getAdres_id(),
                    userData.getFirst_name(), userData.getLast_name(), userData.getPostalCode(),
                    userData.getCity(), userData.getStreet(), userData.getHouseNumber());

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setServerConnection(serverConnection);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add(css);
            stage.show();
        }
    }

    @FXML
    public void switchToRegister(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = loader.load();
        RegisterController controller = loader.getController();
        controller.setServerConnection(serverConnection);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(css);
        stage.show();
    }
}
