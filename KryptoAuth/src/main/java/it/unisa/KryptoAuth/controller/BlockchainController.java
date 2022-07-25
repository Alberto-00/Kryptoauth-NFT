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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/kryptoauth", method = POST)
public class BlockchainController {

    @Autowired
    private BlockchainServiceImpl service;

    @ResponseBody
    @PostMapping("/login")
    public AjaxResponse loginPost(@Valid @ModelAttribute("user") User user, Errors errors,
                                  @RequestParam(value = "userAddress") String address,
                                  @RequestParam(value = "privateKey") String privateKey) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();

        if(errors.hasErrors()) {
            ajaxResponse.addMsg("email", errorMsgField(errors, "email"));
            ajaxResponse.addMsg("password", errorMsgField(errors, "password"));
            return ajaxResponse;
        }
        else if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
            ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
            return ajaxResponse;
        }
        else if (!service.isContractLoaded(address)) {
            if (privateKey == null || privateKey.compareTo("undefined") == 0 || privateKey.isEmpty()){
                ajaxResponse.addMsg("contract", "incorrect");
                return ajaxResponse;
            }
            else if (privateKey.length() != 64) {
                ajaxResponse.addMsg("privateKey", "incorrect");
                return ajaxResponse;
            }

            service.loadContract(privateKey);
            if (!service.addressEquals(address)) {
                ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                return ajaxResponse;
            }

            if (service.isAdmin(address)) {
                if (!service.loginAdmin(address, user.getEmail(), user.getPassword()))
                    ajaxResponse.addMsg("loginError", "incorrect");
                else
                    ajaxResponse.addMsg("successAdmin", "ok");
            }
            else if (service.isUser(address)) {
                if (!service.loginUser(address, user.getEmail(), user.getPassword()))
                    ajaxResponse.addMsg("loginError", "incorrect");
                else
                    ajaxResponse.addMsg("successUser", "ok");
            } else
                ajaxResponse.addMsg("credentialsNotVerified", "incorrect");
            return ajaxResponse;
        }
        else if (service.isUser(address)){
            if (service.loginUser(address, user.getEmail(), user.getPassword()))
                ajaxResponse.addMsg("successUser", "ok");
            else ajaxResponse.addMsg("loginError", "incorrect");
        }
        else if (service.isAdmin(address)){
            if (service.loginAdmin(address, user.getEmail(), user.getPassword()))
                ajaxResponse.addMsg("successAdmin", "ok");
            else ajaxResponse.addMsg("loginError", "incorrect");
        }
        else ajaxResponse.addMsg("credentialsNotVerified", "incorrect");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/register")
    public AjaxResponse registerPost(@Valid @ModelAttribute("user") User user, Errors errors,
                                     @RequestParam(value = "userAddress") String address,
                                     @RequestParam(value = "privateKey") String privateKey) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();

        if(errors.hasErrors()) {
            ajaxResponse.addMsg("email", errorMsgField(errors, "email"));
            ajaxResponse.addMsg("password", errorMsgField(errors, "password"));
            ajaxResponse.addMsg("repeatPassword", errorMsgField(errors, "repeatPassword"));
            ajaxResponse.addMsg("role", errorMsgField(errors, "role"));
            return ajaxResponse;
        }
        else if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
            ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
            return ajaxResponse;
        }
        else if (!service.isContractLoaded(address)) {
            if (privateKey == null || privateKey.compareTo("undefined") == 0 || privateKey.isEmpty()){
                ajaxResponse.addMsg("contract", "incorrect");
                return ajaxResponse;
            }
            else if (privateKey.length() != 64) {
                ajaxResponse.addMsg("privateKey", "incorrect");
                return ajaxResponse;
            }

            service.loadContract(privateKey);
            if (!service.addressEquals(address)) {
                ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                return ajaxResponse;
            }

            try {
                if (user.getRole().compareToIgnoreCase("Utente") == 0 && !service.isAdmin(address)) {
                    service.registerUser(address, user.getEmail(), user.getPassword());
                    ajaxResponse.addMsg("success", "ok");
                    writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Non Attivo");
                    return ajaxResponse;
                }
                else if (user.getRole().compareToIgnoreCase("Admin") == 0 && !service.isUser(address)) {
                    service.registerAdmin(address, user.getEmail(), user.getPassword());
                    ajaxResponse.addMsg("success", "ok");
                    writeAddress(address);
                    return ajaxResponse;
                }
            } catch (Exception e){
                ajaxResponse.addMsg("alredyRegistered", "error");
                return ajaxResponse;
            }
            ajaxResponse.addMsg("errorAdmin", "error");
            return ajaxResponse;
        }
        try {
            if (user.getRole().compareToIgnoreCase("Utente") == 0 &&  !service.isAdmin(address) ) {
                service.registerUser(address, user.getEmail(), user.getPassword());
                ajaxResponse.addMsg("success", "ok");
                writeAddress(address);
                return ajaxResponse;
            }
            else if (user.getRole().compareToIgnoreCase("Admin") == 0 && !service.isUser(address)) {
                service.registerAdmin(address, user.getEmail(), user.getPassword());
                ajaxResponse.addMsg("success", "ok");
                writeAddress(address);
                return ajaxResponse;
            }
        } catch (Exception e){
            ajaxResponse.addMsg("alredyRegistered", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("errorAdmin", "error");
        return ajaxResponse;
    }

    private String errorMsgField(Errors errors, String field){
        for (FieldError error: errors.getFieldErrors(field)) {
            if (error != null)
                return error.getDefaultMessage();
        }
        return "";
    }

    private void writeAddress(String address) throws IOException {
        BufferedWriter out = null;
        try {
            FileWriter fstream = new FileWriter("src/main/resources/static/txt/addressRegistered.txt", true);
            out = new BufferedWriter(fstream);
            out.write(address + "\n");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            if(out != null) {
                out.close();
            }
        }
    }
}
