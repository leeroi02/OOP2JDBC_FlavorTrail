package org.example.oop2jdbc;


import java.sql.*;

public class CRUD {
    public boolean insertData(String username, String password){
        boolean inserted = false;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
            )){
            statement.setString(1, username);
            statement.setString(2, password);
            int num = statement.executeUpdate();
            System.out.println("Account created successfully!");
            if(num != 0) inserted = true;
        }catch (SQLException e){
            System.out.println("Exception in insertData");
            e.printStackTrace();
        }
        return inserted;
    }

    public boolean readData(String username, String password) {
        boolean check = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?"
             )) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet present = statement.executeQuery();
            if(present.next()){
                check = true;
                CurrentUser.id = present.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Exception in readData");
            e.printStackTrace();
        }
        return check;
    }

    public boolean readData(String username) {
        boolean check = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM users WHERE username = ?"
             )) {
            statement.setString(1, username);
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

    public boolean updateData(String username, String newPassword) {
        boolean updated = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE users SET password = ? WHERE username = ? "
             )) {
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            if(rowsUpdated != 0) updated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean deleteData(String username, String password){
        boolean deleted = false;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users where username = ? and password = ?"
            )){
            statement.setString(1, username);
            statement.setString(2, password);
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
