package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

import javax.persistence.Table;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/user")
public class UserFrajkorController {

    @Autowired
    private UserService userService;

    private User loggedUser;
    private String notification;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        try {
            if (userService.isPasswordVerified(username, password)) {
                loggedUser = userService.login(username);
            } else {
                notification = "Username is not registered or password is wrong.";
            }
        } catch (UserException e) {
            notification = e.getMessage();
        } finally {
            return "redirect:/";
        }
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        loggedUser = null;
        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String username, String password) {
        try {
            userService.register(username, password);
            notification = "Sucessfully registered new user. Please log in";
        } catch (UserException e) {
            notification = e.getMessage();
        } finally {
            return "redirect:/";
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

}
