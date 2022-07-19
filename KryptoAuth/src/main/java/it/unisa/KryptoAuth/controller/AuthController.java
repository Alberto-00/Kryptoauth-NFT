package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/kryptoauth", method = GET)
public class AuthController {

    @GetMapping("")
    public String home(){
        return "/page/index";
    }

    @GetMapping("/roadmap")
    public String roadmap(){
        return "/page/roadmap";
    }

    @GetMapping("/goals")
    public String goals(){
        return "/page/goals";
    }

    @GetMapping("/technologies")
    public String technologies(){
        return "/page/technologies";
    }

    @GetMapping("/blog")
    public String blog(){
        return "/page/blog-single";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "/page/register";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "/page/login";
    }
}
