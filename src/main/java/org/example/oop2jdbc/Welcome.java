package org.example.oop2jdbc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Welcome {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final CRUD crud = new CRUD();

    @FXML
    void redirectToLogin(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent loginPage = fxmlLoader.load();

        Scene scene = new Scene(loginPage);

        Stage stage = (Stage) usernameField.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void register(MouseEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please enter both username and password.");
            return;
        }

        if (crud.readData(username, password)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username Exists", "Username already exists. Please choose another one.");
            return;
        }

        if (crud.insertData(username, password)) {
            usernameField.clear();
            passwordField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful", "You have successfully registered.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration Failed", "Registration failed. Please try again later.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}