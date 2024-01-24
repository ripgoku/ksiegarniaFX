package com.bookstore.client;

import com.bookstore.Message;
import com.bookstore.MessageType;
import com.bookstore.PasswordData;
import com.bookstore.UserData;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static com.bookstore.MessageType.SERVER_MESSAGE_ERROR;
import static com.bookstore.MessageType.SERVER_MESSAGE_SUCCES;

/**
 * Kontroler obsługujący interakcje użytkownika z panelem użytkownika w aplikacji.
 */
public class UserController implements Initializable {
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label streetNameLabel;
    @FXML
    private Label houseNumberLabel;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField streetNameTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private ToggleButton changeDetailsButton;
    @FXML
    private ToggleButton changePasswordButton;
    @FXML
    private AnchorPane slidingPanel;
    @FXML
    private AnchorPane slidingPanel2;
    @FXML
    private Label errorLabel;

    ServerConnection serverConnection;
    private final String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();
    private static final double SLIDING_PANEL_HIDDEN_Y = 600.0;
    private static final double SLIDING_PANEL_SHOWN_Y = 178.0;
    private static final double SLIDING_PANEL2_HIDDEN_Y = 600.0;
    private static final double SLIDING_PANEL2_SHOWN_Y = 300.0;

    /**
     * Ustawia połączenie serwera dla kontrolera.
     *
     * @param serverConnection Połączenie z serwerem.
     */
    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Metoda inicjalizująca kontroler po załadowaniu widoku.
     *
     * @param url            Adres URL widoku.
     * @param resourceBundle Zasoby powiązane z widokiem.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UnaryOperator<TextFormatter.Change> postalCodeFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]{0,2}|[0-9]{2}-[0-9]{0,3}")) {
                return change;
            }
            return null;
        };

        postalCodeTextField.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", postalCodeFilter));
        setDefaultDetails();
    }

    /**
     * Obsługuje aktualizację hasła użytkownika.
     *
     * @throws IOException            Wyjątek, jeśli wystąpi problem z połączeniem.
     * @throws ClassNotFoundException Wyjątek, jeśli wystąpi problem z klasą.
     */
    @FXML
    public void updatePassword() throws IOException, ClassNotFoundException {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Hasła nie są identyczne!");
            return;
        } else if (newPassword.contains(" ")) {
            errorLabel.setText("Hasło zawiera spację!");
            return;
        }

        PasswordData passwordData = new PasswordData(LoggedUser.getInstance().getLogin(), oldPassword, newPassword);

        Message updateMessage = new Message();
        updateMessage.setType(MessageType.UPDATE_PASSWORD);
        updateMessage.setData(passwordData);
        serverConnection.sendMessage(updateMessage);

        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == SERVER_MESSAGE_SUCCES)
        {
            errorLabel.setText((String) serverAnswer.getData());
            changePasswordButton.setSelected(false);
            toggleSlidingPanel2();
            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } else if (serverAnswer.getType() == SERVER_MESSAGE_ERROR)
        {
            errorLabel.setText((String) serverAnswer.getData());
        }
    }

    /**
     * Obsługuje aktualizację danych użytkownika.
     *
     * @throws IOException            Wyjątek, jeśli wystąpi problem z połączeniem.
     * @throws ClassNotFoundException Wyjątek, jeśli wystąpi problem z klasą.
     */
    @FXML
    public void updateDetails() throws IOException, ClassNotFoundException {
        String firstName = this.firstNameTextField.getText();
        String lastName = this.lastNameTextField.getText();
        String email = this.emailTextField.getText();
        String login = this.loginTextField.getText();
        String city = this.cityTextField.getText();
        String postalCode = this.postalCodeTextField.getText();
        String street = this.streetNameTextField.getText();
        String houseNumber = this.houseNumberTextField.getText();

        if (login.contains(" ")) {
            errorLabel.setText("Login zawiera spację!");
            return;
        } else if (email.contains(" ")) {
            errorLabel.setText("Email zawiera spację!");
            return;
        }

        UserData userData = new UserData(LoggedUser.getInstance().getCustomerId(), LoggedUser.getInstance().getAdresId(), firstName, lastName, city, postalCode, street, houseNumber, email, login);

        Message updateMessage = new Message();
        updateMessage.setType(MessageType.UPDATE_USER_DETAILS);
        updateMessage.setData(userData);
        serverConnection.sendMessage(updateMessage);

        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == SERVER_MESSAGE_SUCCES)
        {
            errorLabel.setText((String) serverAnswer.getData());
            LoggedUser.getInstance().setUser(LoggedUser.getInstance().getCustomerId(), LoggedUser.getInstance().getAdresId(), firstNameTextField.getText(), lastNameTextField.getText(), postalCodeTextField.getText(), cityTextField.getText(), streetNameTextField.getText(), houseNumberTextField.getText(), emailTextField.getText(), loginTextField.getText());
            changeDetailsButton.setSelected(false);
            toggleSlidingPanel();
            setDefaultDetails();
        } else if (serverAnswer.getType() == SERVER_MESSAGE_ERROR)
        {
            errorLabel.setText((String) serverAnswer.getData());
        }
    }

    /**
     * Ustawia domyślne dane użytkownika w formularzu.
     */
    public void setDefaultDetails() {
        firstNameLabel.setText("Imię: " + LoggedUser.getInstance().getFirstName());
        lastNameLabel.setText("Nazwisko: " + LoggedUser.getInstance().getLastName());
        emailLabel.setText("Adres email: " + LoggedUser.getInstance().getEmail());
        loginLabel.setText("Login: " + LoggedUser.getInstance().getLogin());
        cityLabel.setText("Miejscowość: " + LoggedUser.getInstance().getCity());
        postalCodeLabel.setText("Kod pocztowy: " + LoggedUser.getInstance().getPostalCode());
        streetNameLabel.setText("Ulica: " + LoggedUser.getInstance().getStreet());
        houseNumberLabel.setText("Numer domu/lokalu: " + LoggedUser.getInstance().getHouseNumber());

        firstNameTextField.setText(LoggedUser.getInstance().getFirstName());
        lastNameTextField.setText(LoggedUser.getInstance().getLastName());
        emailTextField.setText(LoggedUser.getInstance().getEmail());
        loginTextField.setText(LoggedUser.getInstance().getLogin());
        cityTextField.setText(LoggedUser.getInstance().getCity());
        postalCodeTextField.setText(LoggedUser.getInstance().getPostalCode());
        streetNameTextField.setText(LoggedUser.getInstance().getStreet());
        houseNumberTextField.setText(LoggedUser.getInstance().getHouseNumber());
    }

    /**
     * Obsługuje animację przesuwania panelu zmiany hasła.
     */
    @FXML
    public void toggleSlidingPanel2() {
        Timeline timeline = new Timeline();
        KeyValue kv;
        if (changePasswordButton.isSelected()) {
            // Animacja do pozycji pokazania
            kv = new KeyValue(slidingPanel2.layoutYProperty(), SLIDING_PANEL2_SHOWN_Y);
        } else {
            // Animacja do pozycji ukrycia
            kv = new KeyValue(slidingPanel2.layoutYProperty(), SLIDING_PANEL2_HIDDEN_Y);
        }

        KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Obsługuje animację przesuwania panelu zmiany danych użytkownika.
     */
    @FXML
    public void toggleSlidingPanel() {
        Timeline timeline = new Timeline();
        KeyValue kv;
        if (changeDetailsButton.isSelected()) {
            // Animacja do pozycji pokazania
            kv = new KeyValue(slidingPanel.layoutYProperty(), SLIDING_PANEL_SHOWN_Y);
        } else {
            // Animacja do pozycji ukrycia
            kv = new KeyValue(slidingPanel.layoutYProperty(), SLIDING_PANEL_HIDDEN_Y);
        }

        KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Przełącza widok na ekran główny po naciśnięciu przycisku "Powrót".
     *
     * @param e Zdarzenie akcji przycisku.
     * @throws IOException Wyjątek, jeśli wystąpi problem z ładowaniem widoku.
     */
    @FXML
    public void switchToHome(ActionEvent e) throws IOException {
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
