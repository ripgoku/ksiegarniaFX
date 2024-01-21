package com.bookstore.client;

import com.bookstore.MessageType;
import com.bookstore.RegistrationData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

import com.bookstore.Message;
import javafx.util.converter.DefaultStringConverter;

import static com.bookstore.MessageType.SERVER_MESSAGE;

public class RegisterController {
    @FXML
    private AnchorPane banner;
    @FXML
    private Button bannerButton;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField street;
    @FXML
    private TextField houseNumber;
    @FXML
    private PasswordField passwordConfirmation;
    @FXML
    private Label registerErrorLabel;

    private ServerConnection serverConnection;
    private final String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();

    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> postalCodeFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]{0,2}|[0-9]{2}-[0-9]{0,3}")) {
                return change;
            }
            return null;
        };

        postalCode.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", postalCodeFilter));
    }

    @FXML
    public void switchToLogin(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setServerConnection(serverConnection);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(css);
        stage.show();
    }

    @FXML
    public void proceedRegister(ActionEvent e) throws IOException, ClassNotFoundException {
        registerErrorLabel.setText("");

        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String email = this.email.getText();
        String login = this.login.getText();
        String password = this.password.getText();
        String city = this.city.getText();
        String postalCode = this.postalCode.getText();
        String street = this.street.getText();
        String houseNumber = this.houseNumber.getText();
        String passwordConfirmation = this.passwordConfirmation.getText();

        RegistrationData registrationData = new RegistrationData(firstName, lastName, email, login,
                 password,  city,  postalCode,
                 street,  houseNumber);

        if (!password.equals(passwordConfirmation)) {
            registerErrorLabel.setText("Hasła nie są identyczne!");
            return;
        } else if (password.contains(" ")) {
            registerErrorLabel.setText("Hasło zawiera spację!");
            return;
        } else if (login.contains(" ")) {
            registerErrorLabel.setText("Login zawiera spację!");
            return;
        } else if (email.contains(" ")) {
            registerErrorLabel.setText("Email zawiera spację!");
            return;
        }

        // Utwórz komunikat rejestracji
        Message registrationMessage = new Message();
        registrationMessage.setType(MessageType.REGISTER);
        registrationMessage.setData(registrationData);

        // Wysyłanie komunikatu do serwera
        serverConnection.sendMessage(registrationMessage);

        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == SERVER_MESSAGE)
        {
            registerErrorLabel.setText((String) serverAnswer.getData());
        }

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setServerConnection(serverConnection);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
}
