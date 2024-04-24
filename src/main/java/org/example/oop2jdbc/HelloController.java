package org.example.oop2jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

import static javafx.fxml.FXMLLoader.load;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private AnchorPane pnLogin, pnLogout;

    @FXML
    private TextField UsernameField;

    @FXML
    private TextField PasswordField;

    @FXML
    public Button btnRegister;

    private final CRUD crud = new CRUD();

    @FXML
    protected void onHelloButtonClick() throws IOException {
        String username = UsernameField.getText();
        String password = PasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please enter both username and password.");
            return;
        }

        if (crud.readData(username, password)) {
            System.out.println("Login successful!");
            setCurrentUserAfterLogin(username);
            loadHomePage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Credentials", "Incorrect username or password.");
        }

    }

    protected void loadHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent scene = loader.load();
        pnLogin.getChildren().clear();
        pnLogin.getChildren().add(scene);
        HomeController hm = loader.getController();
        hm.initialize2();
    }

    protected void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void onLogoutButtonClick() throws IOException {
        Parent scene = load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));

        pnLogout.getChildren().clear();
        pnLogout.getChildren().add(scene);
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        Parent welcomeScene = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        UsernameField.getScene().setRoot(welcomeScene);
    }

    private void setCurrentUserAfterLogin(String username) {
        CurrentUser.setCurrentUser(username);
    }
}
