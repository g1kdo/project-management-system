package models.user;

public class RegularUser extends User{
    public RegularUser(String userName, String userEmail, String password) {
        super(userName, userEmail, password, Role.REGULAR);
    }
}
