package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.User;
import it.unisa.KryptoAuth.service.BlockchainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/kryptoauth", method = GET)
public class KryptoController {

    @Autowired
    private BlockchainServiceImpl service;

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

    @GetMapping("/loginAdmin")
    public String loginAdmin(Model model) throws Exception{
        model.addAttribute( "listAddress", readFileAddress());
        return "/page/admin";
    }

    private List<String> readFileAddress(){
        List<String> address = new ArrayList<>();

        try {
            File file = new File("src/main/resources/static/txt/addressRegistered.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isEmpty())
                    address.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return address;
    }
}
