package it.unisa.KryptoAuth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String index(){
        return "/page/index";
    }

    @GetMapping("/kryptoauth")
    public String home(){
        return "/page/index";
    }

    @GetMapping("/kryptoauth/roadmap")
    public String roadmap(){
        return "/page/roadmap";
    }

    @GetMapping("/kryptoauth/goals")
    public String goals(){
        return "/page/goals";
    }

    @PostMapping("/kryptoauth/login")
    public String loginPost(){
        return "/page/login";
    }

    @GetMapping("/kryptoauth/register")
    public String register(){
        return "/page/register";
    }

    @GetMapping("/kryptoauth/technologies")
    public String technologies(){
        return "/page/technologies";
    }

    @GetMapping("/kryptoauth/blog")
    public String blog(){
        return "/page/blog-single";
    }

    @PostMapping("/kryptoauth/register")
    public String registerPost(){
        return "/page/register";
    }

    @GetMapping("/kryptoauth/login")
    public String login(){
        return "/page/login";
    }
}
