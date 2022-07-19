package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/kryptoauth", method = POST)
public class BlockchainController {

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute("user") User user, Errors errors){
        if(errors.hasErrors())
            return "page/login";

        return "redirect:/kryptoauth";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") User user, Errors errors){
        if(errors.hasErrors())
            return "page/register";

        return "/page/index";
    }
}
