package models.user;

public class AdminUser extends User{
    public AdminUser(String userName, String userEmail, String password) {
        super(userName, userEmail, password, Role.ADMIN);
    }
}
