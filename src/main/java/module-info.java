module com.bookstore.ksiegarniafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires dataTransfer;
    //requires mysql.connector.j;


    opens com.bookstore.client to javafx.fxml;
    exports com.bookstore.client;
}