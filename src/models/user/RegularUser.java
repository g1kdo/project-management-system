package models.user;

public class RegularUser extends User{
    public RegularUser(String userName, String userEmail) {
        super(userName, userEmail, Role.REGULAR);
    }
}
