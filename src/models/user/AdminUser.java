package models.user;

public class AdminUser extends User{
    public AdminUser(String userName, String userEmail) {
        super(userName, userEmail, Role.ADMIN);
    }
}
