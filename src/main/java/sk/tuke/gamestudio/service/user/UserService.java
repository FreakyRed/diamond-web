package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    User register(String username, String password);
    User login(String username, String password);
}
