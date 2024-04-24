package org.example.oop2jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class HomeController {

    @FXML
    private ToggleButton darkModeToggle;

    @FXML
    private AnchorPane pnLogout;

    @FXML
    private Button Logoutbtn;

    @FXML
    private Label currentUsernameLabel;

    @FXML
    private TextField currentUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;


    @FXML
    void initialize() {
        // Initialize the current username label with the current user's username
        String currentUsername = CurrentUser.getCurrentUser();
        if (currentUsername != null) {
            currentUsernameLabel.setText("Welcome, " + currentUsername + "!");
        }
    }

    @FXML
    void handleLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("You have been logged out successfully!");
        System.out.println("Logout successful!");

        alert.setOnCloseRequest(dialogEvent -> redirectToLoginPage());

        alert.showAndWait();
    }



    @FXML
    void saveChanges(MouseEvent event) {
        String newUsername = currentUsernameField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password Mismatch", "New password and confirm password do not match.");
            return;
        }
        if (updateAccount(newUsername, newPassword)) {
            System.out.println("Account update successful!");
            editAccountForm.setVisible(false);
            currentUsernameField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account Updated", "Your account has been successfully updated.");
            currentUsernameLabel.setText("Welcome, " + newUsername + "!");

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Failed to update account. Please try again.");
        }
    }

    private boolean updateAccount(String newUsername, String newPassword) {
        try (Connection connection = MySQLConnection.getConnection()) {
            String username = CurrentUser.getCurrentUser();
            if (username == null) {
                System.err.println("No current user set.");
                return false;
            }

            String updateQuery = "UPDATE users SET username = ?, password = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, username);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    CurrentUser.setCurrentUser(newUsername);
                    return true;
                } else {
                    System.err.println("No rows affected. User not found or update failed.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void handleDeleteAccount(MouseEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Account");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete your account? This action cannot be undone.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String username = CurrentUser.getCurrentUser();
                if (username == null) {

                    System.err.println("No current user set.");
                    return;
                }

                boolean success = deleteAccountFromDatabase(username);
                if (success) {
                    System.out.println("Account deletion successful!");
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Account Deleted");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Your account has been successfully deleted.");
                    successAlert.showAndWait();

                    handleLogout(event);
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete account. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private boolean deleteAccountFromDatabase(String username) {
        try (Connection connection = MySQLConnection.getConnection()) {
            String deleteAccountQuery = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement deleteAccountStatement = connection.prepareStatement(deleteAccountQuery)) {
                deleteAccountStatement.setString(1, username);
                int rowsAffected = deleteAccountStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return true;
                } else {
                    System.err.println("No rows affected. User not found or deletion failed.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void toggleDarkMode(MouseEvent event) {
        if (darkModeToggle.isSelected()) {
            String css = Objects.requireNonNull(getClass().getResource("homedarkmode.css")).toExternalForm();
            pnLogout.getScene().getStylesheets().add(css);
        } else {
            String css = Objects.requireNonNull(getClass().getResource("homedarkmode.css")).toExternalForm();
            pnLogout.getScene().getStylesheets().remove(css);
        }
    }

    @FXML
    private VBox editAccountForm;

    @FXML
    void toggleEditAccount() {
        editAccountForm.setVisible(!editAccountForm.isVisible());
    }

    private void redirectToLoginPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent loginScene = fxmlLoader.load();
            pnLogout.getScene().setRoot(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




     @FXML
     void AddTrail(MouseEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("trail.fxml"));
            Parent loginScene = fxmlLoader.load();
            pnLogout.getScene().setRoot(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteTrail(MouseEvent event){

    }

// DELETEEEEEEEEEEEEEEE TRAIL
private boolean deleteTrailFromDatabase(int idUsersTrail) {
    try (Connection connection = MySQLConnection.getConnection()) {
        String deleteTrailQuery = "DELETE FROM trail WHERE idUsersTrail = ?";
        try (PreparedStatement deleteTrailStatement = connection.prepareStatement(deleteTrailQuery)) {
            deleteTrailStatement.setInt(1, idUsersTrail);
            int rowsAffected = deleteTrailStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                System.err.println("No rows affected. Trail not found or deletion failed.");
                return false;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    @FXML
    void handleDeleteTrail(MouseEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Trail");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete trail? This action cannot be undone.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                TableView.TableViewSelectionModel<Trail> selectionModel = trailTable.getSelectionModel();
                if (!selectionModel.isEmpty()) {
                    Trail selectedTrail = selectionModel.getSelectedItem();
                    boolean success = deleteTrailFromDatabase(selectedTrail.getId());
                    if (success) {
                        System.out.println("Trail deletion successful!");
                        trailTable.getItems().remove(selectedTrail);
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Failed to delete trail. Please try again.");
                        errorAlert.showAndWait();
                    }
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Please select a trail to delete.");
                    errorAlert.showAndWait();
                }
            }
        });
    }



// UPDATE TRAIILLLLLLLLLLL
public boolean updateTrail(String oldPalette, String newPalette, String tastespot, String location, String description, String newDescription) {
    boolean updated = false;
    try (Connection c = MySQLConnection.getConnection();
         PreparedStatement statement = c.prepareStatement(
                 "UPDATE trails SET palette = ?, description = ? WHERE palette = ? AND tastespot = ? AND location = ? AND description = ?"
         )) {
        statement.setString(1, newPalette);
        statement.setString(2, newDescription);
        statement.setString(3, oldPalette);
        statement.setString(4, tastespot);
        statement.setString(5, location);
        statement.setString(6, description);
        int rowsUpdated = statement.executeUpdate();
        System.out.println("Rows updated: " + rowsUpdated);
        if(rowsUpdated != 0) updated = true;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return updated;
}





    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    //TABLE CONTROLLERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR

    public TableView<Trail> trailTable;

    public AnchorPane display;
    @FXML
    public TableColumn<Trail,String> paletteCol;
    @FXML
    public TableColumn<Trail,String> trailCol;
    @FXML
    public TableColumn<Trail,String> locCol;
    @FXML
    public TableColumn<Trail,String> descCol;

    private ObservableList<Trail> getTrailData() {
        ObservableList<Trail> trailData = FXCollections.observableArrayList();

        try (Connection connection = MySQLConnection.getConnection()) {
            String query = "SELECT * FROM trail";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("idUsersTrail");
                    String tasteSpot = resultSet.getString("tastespot");
                    String location = resultSet.getString("location");
                    String description = resultSet.getString("description");
                    String palette = resultSet.getString("palette");

                    Trail trail = new Trail(id, tasteSpot, location, description, palette);
                    trailData.add(trail);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Trail Data Size: " + trailData.size());
        return trailData;
    }
    @FXML
    void initialize2() {
        // Initialize the current username label with the current user's username
        String currentUsername = CurrentUser.getCurrentUser();
        if (currentUsername != null) {
            currentUsernameLabel.setText("Embark on this delicious trail, " + currentUsername + "!");
        }

        // Set up the table columns
        paletteCol.setCellValueFactory(new PropertyValueFactory<>("palette"));
        trailCol.setCellValueFactory(new PropertyValueFactory<>("tastespot"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));


        // Populate the table
        trailTable.setItems(getTrailData());
    }















}