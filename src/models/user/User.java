package models.user;

import utils.RegexValidator;
import utils.exceptions.InvalidInputException;

import java.util.Objects;

public abstract class User {

    private static int idCounter;
    private final String userID;
    private String userName;
    private String userEmail;
    private String password;
    private Role role;

    public User(String userName, String userEmail, String password, Role role) {
        RegexValidator.validateUserEmail(userEmail);
        if (userName == null || userName.strip().isEmpty())
            throw new InvalidInputException("User name cannot be empty.");
        if (userEmail == null || userEmail.strip().isEmpty())
            throw new InvalidInputException("User email cannot be empty.");

        this.userID = String.format("USR%03d", idCounter++);
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
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

    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    //tell Java how to compare two User objects for equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; //same memory reference? Equal.
        if (obj == null || getClass() != obj.getClass()) return false; // null or different class. Not Equal.
        User user = (User) obj;
        return Objects.equals(userID, user.userID);
    }

    //generate a hash calculation based entirely on the unique ID
    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
