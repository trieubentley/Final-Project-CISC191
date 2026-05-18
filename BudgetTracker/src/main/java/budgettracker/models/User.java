package budgettracker.models;

// Module 1: OO Refresh — simple encapsulated model with validation in the constructor
public class User {

    private final String username;

    public User(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public String getUsername() { return username; }

    @Override
    public String toString() { return "User(" + username + ")"; }
}
