package com.vigilant.vigilant_shop.model;

// 7. The static modifier and Nested Classes
public class User {
    private String username;
    private String password;
    private String role;

    // Static Nested Class for Roles
    public static class Role {
        public static final String ADMIN = "ADMIN";
        public static final String CUSTOMER = "CUSTOMER";
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
