package it.unisa.KryptoAuth.model;

public class Address {
    private String address;
    private String username;
    private String password;
    private String role;
    private boolean isLoggedIn;

    public Address(){}

    public Address(String address, String username,
                   String password, String role, boolean isLoggedIn) {
        this.address = address;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLoggedIn = isLoggedIn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
