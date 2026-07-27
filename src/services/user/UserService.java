package services.user;

import models.user.AdminUser;
import models.user.RegularUser;
import models.user.User;
import utils.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> userCatalog;
    private User currentUser;

    public UserService() {
        this.userCatalog = new HashMap<>();
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        registerUser(new AdminUser("Katy Great Adonai", "katygreatado@gmail.com", "admin123"));
        registerUser(new RegularUser("Aline NZIKWINKUNDA", "nzikaline@gmail.com", "user123"));
    }

    public void registerUser(User user) {
        if (userCatalog.containsKey(user.getUserEmail().toLowerCase()))
            throw new InvalidInputException("User email '" + user.getUserEmail() + "' is already registered.");

        userCatalog.put(user.getUserEmail().toLowerCase(), user);
    }

    public void login(String username, String password) {
        User user = userCatalog.get(username.toLowerCase());
        if (user == null || !user.verifyPassword(password))
            throw new InvalidInputException("Invalid username or password.");

        this.currentUser = user;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("✓ User '" + currentUser.getUserName() + "' logged out successfully.");
            this.currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Map<String, User> getAllUsers() {
        return userCatalog;
    }
}
