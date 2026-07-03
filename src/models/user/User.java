package models.user;

public abstract class User {

    private static int idCounter;
    private final String userID;
    private String userName;
    private String userEmail;
    private Role role;

    public User(String userName, String userEmail, Role role) {
        this.userID = String.format("USR%03d", idCounter++);
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Role getRole() {
        return role;
    }
}
