module org.example.oop2jdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens org.example.oop2jdbc to javafx.fxml;
    exports org.example.oop2jdbc;
}