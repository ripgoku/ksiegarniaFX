package com.bookstore.client;

import com.bookstore.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.bookstore.MessageType.SERVER_MESSAGE_ERROR;
import static com.bookstore.MessageType.VIEW_BOOKS;

/**
 * Klasa HomeController zarządza głównym interfejsem użytkownika aplikacji klienta księgarni.
 * Jest odpowiedzialna za wyświetlanie książek, zarządzanie koszykiem zakupów oraz interakcje z serwerem.
 * Zawiera metody do przetwarzania zdarzeń interfejsu użytkownika oraz zarządzania danymi wyświetlanymi na ekranie.
 */
public class HomeController implements Initializable {
    // Deklaracje elementów interfejsu użytkownika
    @FXML
    private AnchorPane banner;
    @FXML
    private AnchorPane categoryPanel;
    @FXML
    private Button bannerButton;
    @FXML
    private ListView<String> categoryListView;
    @FXML
    private TilePane book_panel;
    @FXML
    private Button nextPage;
    @FXML
    private Button prevPage;
    @FXML
    private Label curPage;
    @FXML
    private Label categoryNameLabel;
    @FXML
    private ToggleButton koszykButton;
    @FXML
    private AnchorPane slidingPanel;
    @FXML
    private AnchorPane shoppingCartSummaryPane;
    @FXML
    private ScrollPane shoppingCartScrollPane;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Button shoppingCartOrderButton;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<ShippingMethod> shippingMethodChoice;
    @FXML
    private Label homeErrorLabel;

    String[] category = {"Wszystkie", "albumy", "biografie", "biznes", "dla dzieci", "dla młodzieży", "encyklopedie i słowniki", "ezoteryka",
            "fantastyka", "historia", "informatyka", "komiksy", "kryminał i sensacja", "kultura i sztuka", "lektury",
            "literatura faktu", "literatura piękna", "literatura popularnonaukowa", "nauka języków",
            "nauki humanistyczne", "nauki ścisłe", "podróże i turystyka", "poezja i dramat", "poradniki",
            "prasa", "prawo", "proza obyczajowa", "religia", "rozrywka i humor", "sport"};

    ServerConnection serverConnection;
    private final String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();
    private List<Book> books;
    private static final int ITEMS_PER_PAGE = 10;
    private Pagination pagination;
    private int curPageIndex = 0;
    private List<Book> allBooks;
    private static final double SLIDING_PANEL_HIDDEN_Y = 600.0;
    private static final double SLIDING_PANEL_SHOWN_Y = 178.0;

    /**
     * Ustawia połączenie z serwerem dla kontrolera.
     * @param serverConnection Instancja połączenia z serwerem.
     */
    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        postInitialization();
    }

    /**
     * Inicjalizuje kontroler, ustawiając początkowe wartości i konfiguracje interfejsu użytkownika.
     * @param url URL używane do rozwiązania ścieżek względnych dla korzenia obiektu, lub null jeśli nieznane.
     * @param resourceBundle Zasób, który zawiera dane lokalizacji.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bannerButton.setText(LoggedUser.getInstance().getFirstName() + " " + LoggedUser.getInstance().getLastName());
        categoryListView.getItems().addAll(category);
        initializeShippingMethods();

        shippingMethodChoice.getSelectionModel().selectedItemProperty().addListener((observableValue, oldMethod, newMethod) -> {
            if (newMethod != null) {
                updateShoppingCartView();
            }
        });

        categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleCategorySelection(newValue);
            }
        });
        nextPage.setOnAction(e -> {
            if (curPageIndex < pagination.getPageCount() - 1 && pagination.getPageCount() > 0) {
                updatePage(curPageIndex + 1);
            }
        });

        prevPage.setOnAction(e -> {
            if (curPageIndex > 0) {
                updatePage(curPageIndex - 1);
            }
        });
    }

    /**
     * Inicjalizuje metody dostawy dostępne w aplikacji.
     * Dodaje różne metody dostawy do listy wyboru i ustawia domyślną metodę dostawy.
     */
    private void initializeShippingMethods() {
        shippingMethodChoice.getItems().add(new ShippingMethod(1,"Standard", 7.00));
        shippingMethodChoice.getItems().add(new ShippingMethod(2,"Priorytet", 11.90));
        shippingMethodChoice.getItems().add(new ShippingMethod(3,"Ekspres", 14.90));
        shippingMethodChoice.getItems().add(new ShippingMethod(4,"Międzynarodowa", 34.50));

        shippingMethodChoice.setValue(shippingMethodChoice.getItems().getFirst());
    }

    /**
     * Przełącza widok do panelu użytkownika.
     *
     * @param e Zdarzenie, które wywołuje tę metodę.
     * @throws IOException W przypadku błędu podczas ładowania pliku FXML.
     */
    @FXML
    public void switchToUser(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("User.fxml"));
        Parent root = loader.load();
        UserController controller = loader.getController();
        controller.setServerConnection(serverConnection);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(css);
        stage.show();
    }

    /**
     * Realizuje proces składania zamówienia.
     *
     * @throws IOException W przypadku błędu podczas komunikacji z serwerem.
     * @throws ClassNotFoundException W przypadku błędów związanych z deserializacją.
     */
    @FXML
    public void proceedOrder() throws IOException, ClassNotFoundException {
        Order order = new Order(LoggedUser.getInstance().getCustomerId(), LoggedUser.getInstance().getAdresId(),
                ShoppingCart.getInstance().getCartItems(), shippingMethodChoice.getValue());

        Message orderMessage = new Message();
        orderMessage.setType(MessageType.ORDER_PRODUCT);
        orderMessage.setData(order);

        serverConnection.sendMessage(orderMessage);
        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == MessageType.SERVER_MESSAGE_SUCCES) {
            homeErrorLabel.setText((String) serverAnswer.getData());
            ShoppingCart.getInstance().clearCart();
            updateShoppingCartView();
        } else if (serverAnswer.getType() == SERVER_MESSAGE_ERROR) {
            homeErrorLabel.setText((String) serverAnswer.getData());
        }
    }

    /**
     * Przetwarza zapytanie o wyszukanie książek na podstawie wprowadzonego tekstu.
     * Wyszukuje książki z listy wszystkich książek, które pasują do kryteriów wyszukiwania.
     */
    @FXML
    public void performSearch() {
        String searchText = searchField.getText();
        List<Book> searchResults = allBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                        book.getAuthors().stream().anyMatch(author -> author.toLowerCase().contains(searchText.toLowerCase())))
                .collect(Collectors.toList());

        categoryNameLabel.setText("Znaleziono: " + searchResults.size());
        updateBookView(searchResults);
    }

    /**
     * Przełącza panel boczny koszyka, pokazując lub ukrywając go.
     * Używa animacji do płynnego przejścia.
     */
    @FXML
    public void toggleSlidingPanel() {
        homeErrorLabel.setText("");
        Timeline timeline = new Timeline();
        KeyValue kv;
        if (koszykButton.isSelected()) {
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
     * Aktualizuje widok koszyka zakupów.
     * Wyświetla produkty w koszyku wraz z ich cenami, ilościami oraz sumarycznym kosztem.
     */
    private void updateShoppingCartView() {
        VBox cartContent = new VBox(10);
        double totalCost = 0.00d;

        for (Map.Entry<Book, Integer> entry : ShoppingCart.getInstance().getCartItems().entrySet()) {
            Book book = entry.getKey();
            Integer quantity = entry.getValue();


            HBox bookInfo = new HBox(10);
            bookInfo.setAlignment(Pos.CENTER_LEFT);

            ImageView bookImage = new ImageView(new Image(new ByteArrayInputStream(book.getImg())));
            bookImage.setFitHeight(100);
            bookImage.setFitWidth(75);

            VBox bookDetails = new VBox(5);
            bookDetails.setMinWidth(245);
            bookDetails.setMaxWidth(245);
            Label titleLabel = new Label(book.getTitle());
            titleLabel.getStyleClass().add("titleLabel");
            String authorsText = String.join(", ", book.getAuthors());
            Label authorLabel = new Label(authorsText);
            authorLabel.getStyleClass().add("authorLabel");
            bookDetails.getChildren().addAll(titleLabel, authorLabel);

            Label quantityLabel = new Label(quantity + "x");
            Label priceLabel = new Label(String.format("%.2f", book.getPrice() * quantity) + " zł");
            priceLabel.setMinWidth(80);
            priceLabel.setMaxWidth(80);
            Button deleteFromShoppingCart = new Button("Usuń");
            deleteFromShoppingCart.getStyleClass().add("deleteFromShoppingCartButton");
            deleteFromShoppingCart.setOnAction(e -> {
                ShoppingCart.getInstance().removeBook(book);
                updateShoppingCartView();
            });

            HBox priceAndQuantity = new HBox(30, quantityLabel, priceLabel, deleteFromShoppingCart);
            priceAndQuantity.setAlignment(Pos.CENTER_RIGHT);

            bookInfo.getChildren().addAll(bookImage, bookDetails, priceAndQuantity);
            cartContent.getChildren().add(bookInfo);

            totalCost += book.getPrice() * quantity;
        }

        ShippingMethod selectedMethod = shippingMethodChoice.getValue();
        if (selectedMethod != null) {
            totalCost += selectedMethod.getPrice();
        }
        shoppingCartScrollPane.setContent(cartContent);
        totalCostLabel.setText(String.valueOf(String.format("%.2f", totalCost)));
    }

    /**
     * Obsługuje zdarzenie wyboru kategorii książek.
     *
     * @param selectedCategory Wybrana kategoria książek.
     */
    private void handleCategorySelection(String selectedCategory) {
        categoryNameLabel.setText(selectedCategory);
        List<Book> filteredBooks = getBooksByCategory(selectedCategory);
        updateBookView(filteredBooks);
    }

    /**
     * Aktualizuje widok książek na podstawie przefiltrowanej listy.
     *
     * @param filteredBooks Lista książek do wyświetlenia.
     */
    private void updateBookView(List<Book> filteredBooks) {
        book_panel.getChildren().clear();
        filteredBooks.forEach(book -> {
            VBox bookBox = createBookBox(book);
            book_panel.getChildren().add(bookBox);
        });
        books = filteredBooks;

        int pageCount = (int) Math.ceil((double) books.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        updatePage(0);

        curPage.setText("1/" + pageCount);
    }

    /**
     * Filtruje książki według wybranej kategorii.
     *
     * @param category Kategoria, według której mają zostać przefiltrowane książki.
     * @return Lista książek należących do wybranej kategorii.
     */
    private List<Book> getBooksByCategory(String category) {
        if (category.equals("Wszystkie"))
            return allBooks;
        else
            return allBooks.stream()
                .filter(book -> book.getCategories().contains(category))
                .collect(Collectors.toList());
    }

    /**
     * Inicjalizuje widok książek, pobierając je z serwera.
     *
     * @throws IOException W przypadku błędu podczas komunikacji z serwerem.
     * @throws ClassNotFoundException W przypadku błędów związanych z deserializacją.
     */
    public void postInitialization() {
        try {
            initializeBooksView();
            updateShoppingCartView();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje stronę widoku książek.
     *
     * @param newPageIndex Numer nowej strony do wyświetlenia.
     */
    private void updatePage(int newPageIndex) {
        curPageIndex = newPageIndex;
        createPage(curPageIndex);
        curPage.setText((curPageIndex + 1) + "/" + pagination.getPageCount());
    }

    /**
     * Inicjalizuje widok książek w aplikacji.
     * Łączy się z serwerem i pobiera listę dostępnych książek, a następnie aktualizuje widok.
     *
     * @throws IOException W przypadku błędu wejścia-wyjścia.
     * @throws ClassNotFoundException W przypadku braku klasy.
     */
    private void initializeBooksView() throws IOException, ClassNotFoundException {
        if (serverConnection == null) {
            System.out.println("Błąd połączenia!");
            return;
        }
        Message viewBooks = new Message();
        viewBooks.setType(VIEW_BOOKS);
        serverConnection.sendMessage(viewBooks);

        Message serverAnswer = (Message) serverConnection.receiveMessage();
        if (serverAnswer.getType() == SERVER_MESSAGE_ERROR) {
            homeErrorLabel.setText((String) serverAnswer.getData());
        } else if (serverAnswer.getType() == VIEW_BOOKS) {
            allBooks = (List<Book>) serverAnswer.getData();
            books = new ArrayList<>(allBooks);
            for (Book book : books) {
                books = (List<Book>) serverAnswer.getData();
                initializePagination();
                updatePage(0);
            }
        }
    }

    /**
     * Oblicza liczbę stron na podstawie liczby dostępnych książek i liczby książek na stronę.
     *
     * @return Liczba stron z książkami.
     */
    private int getPageCount() {
        return (int) Math.ceil((double) books.size() / ITEMS_PER_PAGE);
    }

    /**
     * Inicjalizuje paginację (podział na strony) widoku książek.
     * Ustawia liczbę stron i fabrykę stron do generowania widoku poszczególnych stron.
     */
    private void initializePagination() {
        int pageCount = getPageCount();
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);
        book_panel.getChildren().add(pagination);
    }

    /**
     * Tworzy widok pojedynczej strony z książkami.
     *
     * @param pageIndex Numer strony do utworzenia.
     * @return Węzeł reprezentujący stronę z książkami.
     */
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, books.size());
        book_panel.getChildren().clear();

        List<VBox> boxes = books.subList(fromIndex, toIndex).stream()
                .map(this::createBookBox)
                .collect(Collectors.toList());
        book_panel.getChildren().addAll(boxes);

        return book_panel;
    }

    /**
     * Tworzy widok pojedynczej książki.
     *
     * @param book Obiekt książki do wyświetlenia.
     * @return Węzeł reprezentujący pojedynczą książkę.
     */
    private VBox createBookBox(Book book) {
        VBox box = new VBox(4);
        Label titleLabel = new Label(book.getTitle());
        String authorsText = String.join(", ", book.getAuthors());
        Label authorLabel = new Label(authorsText);
        Label priceLabel = new Label(String.format("%.2f zł", book.getPrice()));
        priceLabel.setMinWidth(100);
        ImageView imageView = new ImageView();

        if (book.getImg() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(book.getImg());
            Image image = new Image(inputStream);

            imageView.setImage(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(70);
        }
        HBox imageContainer = new HBox(imageView);

        ImageView cartIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/shopping_cart.png"))));
        cartIcon.setFitHeight(24);
        cartIcon.setFitWidth(24);
        Button addToCartButton = new Button("", cartIcon);
        addToCartButton.getStyleClass().add("addToCartButton");
        addToCartButton.setOnAction(e -> {
            ShoppingCart.getInstance().addBook(book);
            updateShoppingCartView();
        });
        HBox priceAndCartBox = new HBox(1);
        priceAndCartBox.getChildren().addAll(priceLabel, addToCartButton);

        box.getStyleClass().add("box");
        titleLabel.getStyleClass().add("titleLabel");
        authorLabel.getStyleClass().add("authorLabel");
        priceLabel.getStyleClass().add("priceLabel");
        imageContainer.getStyleClass().add("imageContainer");

        box.getChildren().addAll(imageContainer, titleLabel, authorLabel, priceAndCartBox);
        return box;
    }
}
