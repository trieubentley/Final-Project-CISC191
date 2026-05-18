// looks fine to me for now, but we can come back

public class User {
    private String username;

    public User(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty.");
        }

        this.username = username;
    }
}