package org.example.oop2jdbc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Trail {
    @FXML
    private TextField tastespotField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField paletteField;
    @FXML
    private Button btnback;
    private final CRUDTrail crudtrail = new CRUDTrail();

    private String palette, tastespot, location, description;
    private int id;

    public Trail() {

    }
    public Trail(int id, String palette, String tastespot, String location, String description) {
        this.id= id;
        this.palette=palette;
        this.tastespot=tastespot;
        this.location=location;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPalette() {
        return palette;
    }

    public void setPalette(String palette) {
        this.palette = palette;
    }

    public String getTastespot() {
        return tastespot;
    }

    public void setTastespot(String tastespot) {
        this.tastespot = tastespot;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private void CreateTableTrail() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS trail (" +
                    "idUsersTrail INT  AUTO_INCREMENT PRIMARY KEY," +
                    "idUser INT NOT NULL,"+
                    "tastespot VARCHAR(100) NOT NULL," +
                    "location VARCHAR(100) NOT NULL," +
                    "description VARCHAR(100) NOT NULL," +
                    "palette VARCHAR(100) NOT NULL," +
                    "FOREIGN KEY (idUser) REFERENCES users(id))";


            statement.execute(query);
            System.out.println("Table Trail created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void save(MouseEvent mouseEvent) {
        CreateTableTrail();

        String palette = paletteField.getText().trim();
        String tastespot = tastespotField.getText().trim();
        String location = locationField.getText().trim();
        String description = descriptionField.getText().trim();

        if (palette.isEmpty() || tastespot.isEmpty() || location.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please fill in all fields.");
            return;
        }

        if (crudtrail.readData(palette, tastespot, location, description)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Trail Exists", "This trail already exists. Please enter a new one.");
            return;
        }

        if (crudtrail.insertData(palette, tastespot, location, description)) {
            paletteField.clear();
            tastespotField.clear();
            locationField.clear();
            descriptionField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Trail Added", "Trail added successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Trail", "Failed to add trail. Please try again later.");
        }
    }

    @FXML
    void back (MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            HomeController hm = loader.getController();
            hm.initialize2();
            Stage stage = (Stage) btnback.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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


        

