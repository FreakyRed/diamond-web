package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    void register(String username, String password);
    User login(String username);
    boolean isPasswordVerified(String username, String password);
    User getUser();
}
