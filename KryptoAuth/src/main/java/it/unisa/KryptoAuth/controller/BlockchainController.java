package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.AjaxResponse;
import it.unisa.KryptoAuth.model.User;
import it.unisa.KryptoAuth.service.BlockchainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/kryptoauth", method = POST)
public class BlockchainController {

    @Autowired
    private BlockchainServiceImpl service;

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute("user") User user, Errors errors){
        if(errors.hasErrors())
            return "page/login";

        return "redirect:/kryptoauth";
    }

    @ResponseBody
    @PostMapping("/register")
    public AjaxResponse registerPost(@Valid @ModelAttribute("user") User user, Errors errors,
                                     @RequestParam(value = "userAddress") String address){

        AjaxResponse ajaxResponse = new AjaxResponse();

        if(errors.hasErrors()) {
            ajaxResponse.addMsg("email", errorMsgField(errors, "email"));
            ajaxResponse.addMsg("password", errorMsgField(errors, "password"));
            ajaxResponse.addMsg("repeatPassword", errorMsgField(errors, "repeatPassword"));
            return ajaxResponse;
        }
        else if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
            ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
            return ajaxResponse;
        }

        try {
            service.deploy(address);
            System.out.println(service.isAdmin(address));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String errorMsgField(Errors errors, String field){
        for (FieldError error: errors.getFieldErrors(field)) {
            if (error != null)
                return error.getDefaultMessage();
        }
        return "";
    }
}
