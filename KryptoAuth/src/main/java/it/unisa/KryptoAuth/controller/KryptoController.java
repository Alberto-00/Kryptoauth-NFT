package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.User;
import it.unisa.KryptoAuth.service.BlockchainServiceImpl;
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
import java.math.BigInteger;
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
    public String home(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            model.addAttribute("token", currentService.getTokensUser());
        }
        return "/page/index";
    }

    @GetMapping("/roadmap")
    public String roadmap(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            model.addAttribute("token", currentService.getTokensUser());
        }
        return "/page/roadmap";
    }

    @GetMapping("/goals")
    public String goals(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            model.addAttribute("token", currentService.getTokensUser());
        }
        return "/page/goals";
    }

    @GetMapping("/technologies")
    public String technologies(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            model.addAttribute("token", currentService.getTokensUser());
        }
        return "/page/technologies";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam("name") String value,
                       Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            model.addAttribute("token", currentService.getTokensUser());
        }
        return "/page/blog/" + value;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        invalidateSession(request.getSession());
        return "redirect:/kryptoauth";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            model.addAttribute("user", new User());
            return "/page/register";
        }
        invalidateSession(session);
        return "/error/500";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            model.addAttribute("user", new User());
            return "/page/login";
        }
        invalidateSession(session);
        return "/error/500";
    }

    @GetMapping("/shop")
    public String marketplace(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

        if ((session.getAttribute("admin") != null && currentService.isAdmin(session.getAttribute("admin").toString())
                || (session.getAttribute("user") != null && currentService.isUser(session.getAttribute("user").toString())))) {

            if (session.getAttribute("user") != null)
                model.addAttribute("token", currentService.getTokensUser());
            return "/page/NFT-marketplace/marketplace";
        }
        invalidateSession(session);
        return "/error/500";
    }

    @GetMapping("/marketplace/info-nft")
    public String infoNft(@RequestParam("id") String id,
                          Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

        if ((session.getAttribute("admin") != null && currentService.isAdmin(session.getAttribute("admin").toString())
                || (session.getAttribute("user") != null && currentService.isUser(session.getAttribute("user").toString())))) {
            try {
                String jsonString = currentService.getNftById(new BigInteger(id));
                if (jsonString.compareTo("[]") == 0){
                    invalidateSession(session);
                    return "/error/500";
                }
                List<String> nft = new ArrayList<>();
                JSONObject obj = (JSONObject) new JSONParser().parse(jsonString);
                nft.add(obj.get("name").toString());
                nft.add(obj.get("description").toString());
                nft.add(obj.get("url").toString());
                nft.add(obj.get("category").toString());
                nft.add(obj.get("price").toString());
                nft.add(obj.get("validUntil").toString());
                nft.add(obj.get("sale").toString());
                nft.add(obj.get("tokenId").toString());
                nft.add(obj.get("owner").toString());
                nft.add(obj.get("sold").toString());

                if (session.getAttribute("user") != null)
                    model.addAttribute("token", currentService.getTokensUser());

                model.addAttribute("nft", nft);
            } catch (Exception e) {
                invalidateSession(session);
                return "/error/500";
            }
            return "/page/NFT-marketplace/info-nft";
        }
        invalidateSession(session);
        return "/error/500";
    }


    /*=============================================== Admin Page =====================================================*/
    @GetMapping("/amministrazione")
    public String administration(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

        if (session.getAttribute("admin") != null && currentService != null &&
                currentService.isAdmin(session.getAttribute("admin").toString())) {

            model.addAttribute( "listAddress", readFileAddress());
            return "/page/admin";
        } else if (session.getAttribute("user") != null) {
            return "/error/401";
        }
        invalidateSession(session);
        return "/error/500";
    }

    @GetMapping("/marketplace/create-nft")
    public String createNft(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

        if (session.getAttribute("admin") != null && currentService != null &&
                currentService.isAdmin(session.getAttribute("admin").toString())) {
            return "/page/NFT-marketplace/admin/create-nft";
        } else if (session.getAttribute("user") != null) {
            return "/error/401";
        }
        invalidateSession(session);
        return "/error/500";
    }


    /*================================================ User Page =====================================================*/
    @GetMapping("/profile")
    public String userProfile(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

        if (session.getAttribute("user") != null && currentService != null &&
                currentService.isUser(session.getAttribute("user").toString())) {
            model.addAttribute("token", currentService.getTokensUser());
            model.addAttribute("address", session.getAttribute("user").toString());
            model.addAttribute("name", currentService.getName(session.getAttribute("user").toString()));

            int[] categories = new int[4];
            categories[0] = getCategoryDiscount(session.getAttribute("user").toString(), "Accessori");
            categories[1] = getCategoryDiscount(session.getAttribute("user").toString(), "Corsi");
            categories[2] = getCategoryDiscount(session.getAttribute("user").toString(), "Documentazione");
            categories[3] = getCategoryDiscount(session.getAttribute("user").toString(), "Incontri");
            model.addAttribute("category", categories);
            return "/page/NFT-marketplace/user/user-profile";
        } else if (session.getAttribute("admin") != null) {
            return "/error/401";
        }
        invalidateSession(session);
        return "/error/500";
    }

    private void invalidateSession(HttpSession session){
        if (session.getAttribute("user") != null)
            session.removeAttribute("user");

        if (session.getAttribute("admin") != null)
            session.removeAttribute("admin");

        if (session.getAttribute("service") != null)
            session.removeAttribute("service");

        session.invalidate();
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

    private int getCategoryDiscount(String address, String category){
        try {
            Object obj =  new JSONParser().parse(new FileReader("src/main/resources/static/txt/categoryDiscount.json"));
            JSONArray root = (JSONArray) obj;

            for (Object o : root) {
                JSONObject element = (JSONObject) o;

                if (element.get("address").toString().compareToIgnoreCase(address) == 0){
                    switch (category) {
                        case "Accessori" -> {
                            JSONObject sale = (JSONObject) element.get("categories");
                            return Integer.parseInt(sale.get("Accessories").toString());
                        }
                        case "Corsi" -> {
                            JSONObject sale = (JSONObject) element.get("categories");
                            return Integer.parseInt(sale.get("Courses").toString());
                        }
                        case "Documentazione" -> {
                            JSONObject sale = (JSONObject) element.get("categories");
                            return Integer.parseInt(sale.get("Documentation").toString());
                        }
                        case "Incontri" -> {
                            JSONObject sale = (JSONObject) element.get("categories");
                            return Integer.parseInt(sale.get("Exhibition").toString());
                        }
                    }
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
