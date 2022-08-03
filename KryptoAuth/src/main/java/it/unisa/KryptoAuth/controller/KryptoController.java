package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Gestisce tutte le chiamate GET per la visualizzazione delle pagine dell'applicazione
 * e la funzione di logout riservata all'utente e all'admin.
 */
@Controller
@RequestMapping(value = "/kryptoauth", method = GET)
public class KryptoController {

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (request.getAttribute("user") != null){
            session.removeAttribute("user");
        } else if (request.getAttribute("admin") != null) {
            session.removeAttribute("admin");
        }
        session.invalidate();
        return "redirect:/kryptoauth";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            model.addAttribute("user", new User());
            return "/page/register";
        } return "/error/500";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            model.addAttribute("user", new User());
            return "/page/login";
        } return "/error/500";
    }

    @GetMapping("/loginAdmin")
    public String loginAdmin(Model model, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") != null) {
            model.addAttribute( "listAddress", readFileAddress());
            return "/page/admin";
        } else if (session.getAttribute("user") != null) {
            return "/error/401";
        } return "/error/500";
    }

    private List<String> readFileAddress(){
        List<String> address = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader("src/main/resources/static/txt/addressRegistered.json"));
            JSONArray root = (JSONArray) obj;

            for (Object o : root) {
                JSONObject element = (JSONObject) o;
                String addressJson = element.get("address").toString();
                String nameJson = element.get("name").toString();
                String roleJson = element.get("role").toString();
                String statusJson = element.get("status").toString();
                address.add(addressJson + "," + nameJson + "," + roleJson + "," + statusJson);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
