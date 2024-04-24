package org.example.oop2jdbc;

public class CurrentUser {
    private static String currentUser;
    public static Integer id;

    public static void setCurrentUser(String username) {
        currentUser = username;

    }

    public static void clearCurrentUser() {
        currentUser = null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
}