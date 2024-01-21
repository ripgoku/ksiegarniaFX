package com.bookstore.client;

import com.bookstore.Book;
import com.bookstore.Message;
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

public class HomeController implements Initializable {
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
    private Label booksErrorLabel;
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
    private ChoiceBox shippingMethodChoice;

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

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        postInitialization();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bannerButton.setText(LoggedUser.getInstance().getFirstName() + " " + LoggedUser.getInstance().getLastName());
        categoryListView.getItems().addAll(category);
        initializeShippingMethods();
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

    private void initializeShippingMethods() {
        shippingMethodChoice.getItems().add(new ShippingMethod("Standard", 7.00));
        shippingMethodChoice.getItems().add(new ShippingMethod("Priorytet", 11.90));
        shippingMethodChoice.getItems().add(new ShippingMethod("Ekspres", 14.90));
        shippingMethodChoice.getItems().add(new ShippingMethod("Międzynarodowa", 34.50));

        // Ustaw domyślną metodę dostawy
        shippingMethodChoice.setValue(shippingMethodChoice.getItems().get(0));
    }

    @FXML
    public void proceedOrder(ActionEvent e) throws IOException {

    }

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

    @FXML
    public void toggleSlidingPanel() {
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
        shoppingCartScrollPane.setContent(cartContent); // Ustawia zawartość ScrollPane
        totalCostLabel.setText(String.valueOf(String.format("%.2f", totalCost)));
    }

    private void handleCategorySelection(String selectedCategory) {
        categoryNameLabel.setText(selectedCategory);
        List<Book> filteredBooks = getBooksByCategory(selectedCategory);
        updateBookView(filteredBooks);
    }

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

    private List<Book> getBooksByCategory(String category) {
        if (category.equals("Wszystkie"))
            return allBooks;
        else
            return allBooks.stream()
                .filter(book -> book.getCategories().contains(category))
                .collect(Collectors.toList());
    }

    public void postInitialization() {
        try {
            initializeBooksView();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updatePage(int newPageIndex) {
        curPageIndex = newPageIndex;
        createPage(curPageIndex);
        curPage.setText((curPageIndex + 1) + "/" + pagination.getPageCount());
    }

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
            booksErrorLabel.setText((String) serverAnswer.getData());
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

    private int getPageCount() {
        return (int) Math.ceil((double) books.size() / ITEMS_PER_PAGE);
    }

    private void initializePagination() {
        int pageCount = getPageCount();
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);
        book_panel.getChildren().add(pagination);
    }

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


    private VBox createBookBox(Book book) {
        VBox box = new VBox(4);
        Label titleLabel = new Label(book.getTitle());
        String authorsText = String.join(", ", book.getAuthors());
        Label authorLabel = new Label(authorsText);
        Label priceLabel = new Label(String.valueOf(book.getPrice()) + " zł");
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
