package org.example.oop2jdbc;

import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUDTrail {


    public boolean insertData(String palette, String tastespot, String location, String description){
        boolean inserted = false;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO trail (palette,tastespot,location, description,idUser) VALUES (?, ?, ?, ?, ?)"
            )){
            statement.setString(1, palette);
            statement.setString(2, tastespot);
            statement.setString(3, location);
            statement.setString(4, description);
            statement.setInt(5,CurrentUser.id);
            int num = statement.executeUpdate();
            System.out.println("Trail created successfully!");
            if(num != 0) inserted = true;
        }catch (SQLException e){
            System.out.println("Exception in insertDataTrail");
            e.printStackTrace();
        }
        return inserted;
    }

    public boolean readData(String palette, String tastespot, String location, String description){
        boolean check = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM trail WHERE palette = ? AND tastespot = ? AND location = ? AND description = ?"
             )) {
            statement.setString(1, palette);
            statement.setString(2, tastespot);
            statement.setString(3, location);
            statement.setString(4, description);
            ResultSet present = statement.executeQuery();
            if(present.next()){
                check = true;
            }
        } catch (SQLException e) {
            System.out.println("Exception in readData");
            e.printStackTrace();
        }
        return check;
    }

    public boolean updateData(String oldPalette, String newPalette, String tastespot, String location, String description, String newDescription) {
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


    public boolean deleteData(String palette, String tastespot, String location, String description){
        boolean deleted = false;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM trails WHERE palette = ? AND tastespot = ? AND location = ? AND description = ?"
            )){
            statement.setString(1, palette);
            statement.setString(2, tastespot);
            statement.setString(3, location);
            statement.setString(4, description);
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
            if(rowsDeleted != 0) deleted = true;
        }catch (SQLException e){
            System.out.println("Exception in deleteData");
            e.printStackTrace();
        }
        return deleted;
    }


}
