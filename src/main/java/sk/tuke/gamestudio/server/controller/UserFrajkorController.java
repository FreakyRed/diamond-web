package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.user.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/user")
public class UserFrajkorController {

    @Autowired
    private UserService userService;

    private User loggedUser;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(User login, Model model){
        //loggedUser = userService.login();
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser(){
        return loggedUser;
    }

    public boolean isLogged(){
        return loggedUser != null;
    }
}
