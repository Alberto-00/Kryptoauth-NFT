package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.AjaxResponse;
import it.unisa.KryptoAuth.model.User;
import it.unisa.KryptoAuth.service.BlockchainServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.*;

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
                                  @RequestParam(value = "privateKey") String privateKey,
                                  HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null && session.getAttribute("admin") == null){
            if(errors.hasFieldErrors("email") || errors.hasFieldErrors("password")) {
                ajaxResponse.addMsg("email", errorMsgField(errors, "email"));
                ajaxResponse.addMsg("password", errorMsgField(errors, "password"));
                return ajaxResponse;
            }
            else if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
                ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
                return ajaxResponse;
            }
            else if (!service.isContractLoaded(address)) {
                if (checkPrivateKeyAndAddress(address, privateKey, ajaxResponse))
                    return ajaxResponse;

                if (service.isAdmin(address)) {
                    if (!service.loginAdmin(address, user.getEmail(), user.getPassword()))
                        ajaxResponse.addMsg("loginError", "incorrect");
                    else {
                        ajaxResponse.addMsg("successAdmin", "ok");
                        session.setAttribute("admin", address);
                    }
                }
                else if (service.isUser(address)) {
                    if (!service.loginUser(address, user.getEmail(), user.getPassword()))
                        ajaxResponse.addMsg("loginError", "incorrect");
                    else {
                        ajaxResponse.addMsg("successUser", "ok");
                        session.setAttribute("user", address);
                    }
                } else
                    ajaxResponse.addMsg("credentialsNotVerified", "incorrect");
                return ajaxResponse;
            }
            else if (service.isUser(address)){
                if (service.loginUser(address, user.getEmail(), user.getPassword())) {
                    ajaxResponse.addMsg("successUser", "ok");
                    session.setAttribute("user", address);
                }
                else ajaxResponse.addMsg("loginError", "incorrect");
            }
            else if (service.isAdmin(address)){
                if (service.loginAdmin(address, user.getEmail(), user.getPassword())) {
                    ajaxResponse.addMsg("successAdmin", "ok");
                    session.setAttribute("admin", address);
                } 
                else ajaxResponse.addMsg("loginError", "incorrect");
            }
            else 
                ajaxResponse.addMsg("credentialsNotVerified", "incorrect");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/register")
    public AjaxResponse registerPost(@Valid @ModelAttribute("user") User user, Errors errors,
                                     @RequestParam(value = "userAddress") String address,
                                     @RequestParam(value = "privateKey") String privateKey,
                                     HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null && session.getAttribute("admin") == null){
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
                if (checkPrivateKeyAndAddress(address, privateKey, ajaxResponse))
                    return ajaxResponse;
                return registerForRoles(user, address, ajaxResponse, session);
            }
            return registerForRoles(user, address, ajaxResponse, session);
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/activeAddress")
    public AjaxResponse activeAddress(@RequestParam(value = "address") String address,
                                      @RequestParam(value = "role") String role,
                                      @RequestParam(value = "status") String status,
                                      @RequestParam(value = "privateKey") String privateKey,
                                      HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null){
            if (address != null && !address.isEmpty() &&
                    role != null && !role.isEmpty() &&
                    status != null && !status.isEmpty()){

                if (!service.isContractLoaded(session.getAttribute("admin").toString())){
                    if (checkPrivateKeyAndLoadContract(privateKey, ajaxResponse))
                        return ajaxResponse;
                    if (!service.addressEquals(session.getAttribute("admin").toString())){
                        ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                        return ajaxResponse;
                    }
                    if (role.compareToIgnoreCase("user") == 0){
                        if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) == 0){
                            ajaxResponse.addMsg("revokeAdmin", "ok");
                            return ajaxResponse;
                        } else if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
                            ajaxResponse.addMsg("notRevoke", "error");
                            return ajaxResponse;
                        }
                        service.addUser(address);
                        if (status.compareToIgnoreCase("non attivo") == 0)
                            status = "Attivo";
                        updateAddress(address, status, role);
                        ajaxResponse.addMsg("success", status + "," + role);
                        return ajaxResponse;
                    }
                    else if (role.compareToIgnoreCase("admin") == 0) {
                        service.addAdmin(address);
                        if (status.compareToIgnoreCase("non attivo") == 0)
                            status = "Attivo";
                        updateAddress(address, status, role);
                        ajaxResponse.addMsg("success", status + "," + role);
                        return ajaxResponse;
                    }
                }
                if (!service.addressEquals(session.getAttribute("admin").toString())){
                    ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                    return ajaxResponse;
                }
                if (role.compareToIgnoreCase("user") == 0){
                    if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) == 0){
                        ajaxResponse.addMsg("revokeAdmin", "ok");
                        return ajaxResponse;
                    } else if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
                        ajaxResponse.addMsg("notRevoke", "error");
                        return ajaxResponse;
                    }
                    service.addUser(address);
                    if (status.compareToIgnoreCase("non attivo") == 0)
                        status = "Attivo";
                    updateAddress(address, status, role);
                    ajaxResponse.addMsg("success", status + "," + role);
                    return ajaxResponse;
                }
                else if (role.compareToIgnoreCase("admin") == 0) {
                    service.addAdmin(address);
                    if (status.compareToIgnoreCase("non attivo") == 0)
                        status = "Attivo";
                    updateAddress(address, status, role);
                    ajaxResponse.addMsg("success", status + "," + role);
                    return ajaxResponse;
                }
            } else
                ajaxResponse.addMsg("errorPage", "error");
        } else
            ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/disactiveAddress")
    public AjaxResponse disactiveAddress(@RequestParam(value = "address") String address,
                                      @RequestParam(value = "role") String role,
                                      @RequestParam(value = "status") String status,
                                      @RequestParam(value = "privateKey") String privateKey,
                                      HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null){
            if (address != null && !address.isEmpty() &&
                    role != null && !role.isEmpty() &&
                    status != null && !status.isEmpty()){

                if (!service.isContractLoaded(session.getAttribute("admin").toString())){
                    if (checkPrivateKeyAndLoadContract(privateKey, ajaxResponse))
                        return ajaxResponse;
                    return checkRevokeRole(address, role, status, ajaxResponse, session);
                }
                return checkRevokeRole(address, role, status, ajaxResponse, session);
            } else
                ajaxResponse.addMsg("errorPage", "error");
        } else
            ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/revokeAdmin")
    public AjaxResponse renounceAdmin(@RequestParam(value = "address") String address,
                                      HttpServletRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        try {
            if (session != null && session.getAttribute("admin").toString().compareToIgnoreCase(address) == 0){
                service.addUser(address);
                service.removeAdmin(address);
                ajaxResponse.addMsg("redirect", "ok");
                updateAddress(address, "Attivo", "User");
            } else  ajaxResponse.addMsg("error", "error");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.addMsg("error", "error");
        }
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/revokeRoles")
    public AjaxResponse renounceRoles(@RequestParam(value = "address") String address,
                                      HttpServletRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        try {
            if (session != null && session.getAttribute("admin").toString().compareToIgnoreCase(address) == 0){
                service.removeUser(address);
                service.removeAdmin(address);
                ajaxResponse.addMsg("redirect", "ok");
                updateAddress(address, "Non Attivo", "User");
            } else  ajaxResponse.addMsg("error", "error");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.addMsg("error", "error");
        }
        return ajaxResponse;
    }

    private boolean checkPrivateKeyAndLoadContract(@RequestParam("privateKey") String privateKey,
                                                   AjaxResponse ajaxResponse) {
        return checkPrivateKey(privateKey, ajaxResponse);
    }

    private boolean checkPrivateKey(@RequestParam("privateKey") String privateKey, AjaxResponse ajaxResponse) {
        if (privateKey == null || privateKey.compareTo("undefined") == 0 || privateKey.isEmpty()){
            ajaxResponse.addMsg("contract", "incorrect");
            return true;
        }
        else if (privateKey.length() != 64) {
            ajaxResponse.addMsg("privateKey", "incorrect");
            return true;
        }
        service.loadContract(privateKey);
        return false;
    }

    private boolean checkPrivateKeyAndAddress(@RequestParam("userAddress") String address,
                                              @RequestParam("privateKey") String privateKey,
                                              AjaxResponse ajaxResponse) throws Exception {
        if (checkPrivateKey(privateKey, ajaxResponse))
            return true;
        if (!service.addressEquals(address)) {
            ajaxResponse.addMsg("notEqualsAddress", "incorrect");
            return true;
        }
        return false;
    }

    @NotNull
    private AjaxResponse registerForRoles(@ModelAttribute("user") @Valid User user,
                                          @RequestParam("userAddress") String address,
                                          AjaxResponse ajaxResponse, HttpSession session) {
        try {
            if (user.getRole().compareToIgnoreCase("User") == 0 && !service.isAdmin(address)) {
                service.registerUser(address, user.getEmail(), user.getPassword());
                ajaxResponse.addMsg("success", "user");
                writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Non Attivo,");
                return ajaxResponse;
            }
            else if (user.getRole().compareToIgnoreCase("Admin") == 0 && !service.isUser(address)) {
                service.registerUser(address, user.getEmail(), user.getPassword());

                if (service.isAdmin(address)) {
                    writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Attivo,");
                    session.setAttribute("admin", address);
                    ajaxResponse.addMsg("adminActive", "admin");
                } else {
                    writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Non Attivo,");
                    ajaxResponse.addMsg("success", "admin");
                }
                return ajaxResponse;
            } else if (user.getRole().compareToIgnoreCase("Admin") == 0 && service.isUser(address)){
                ajaxResponse.addMsg("errorUser", "error");
                return ajaxResponse;
            }
        } catch (Exception e){
            ajaxResponse.addMsg("alredyRegistered", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("errorAdmin", "error");
        return ajaxResponse;
    }

    @NotNull
    private AjaxResponse checkRevokeRole(@RequestParam("address") String address,
                                         @RequestParam("role") String role,
                                         @RequestParam("status") String status,
                                         AjaxResponse ajaxResponse,
                                         HttpSession session) throws Exception {
        if (!service.addressEquals(session.getAttribute("admin").toString())){
            ajaxResponse.addMsg("notEqualsAddress", "incorrect");
            return ajaxResponse;
        }
        if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) == 0){
            ajaxResponse.addMsg("revoke", "ok");
            return ajaxResponse;
        }
        else if (service.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
            ajaxResponse.addMsg("notRevoke", "error");
            return ajaxResponse;
        }
        service.removeUser(address);
        if (status.compareToIgnoreCase("Attivo") == 0)
            status = "Non Attivo";
        updateAddress(address, status, role);
        ajaxResponse.addMsg("success", status + "," + role);
        return ajaxResponse;
    }

    private String errorMsgField(Errors errors, String field){
        for (FieldError error: errors.getFieldErrors(field)) {
            if (error != null)
                return error.getDefaultMessage();
        }
        return "";
    }

    private void writeAddress(String address) {
        String[] data = address.split(",");
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader("src/main/resources/static/txt/addressRegistered.json"));
            JSONArray root = (JSONArray) obj;

            JSONObject userInfo = new JSONObject();
            userInfo.put("address", data[0]);
            userInfo.put("name", data[1]);
            userInfo.put("role", data[2]);
            userInfo.put("status", data[3]);

            root.add(userInfo);

            FileWriter file = new FileWriter("src/main/resources/static/txt/addressRegistered.json");
            file.write(root.toJSONString());
            file.flush();
            file.close();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAddress(String address, String status, String role){
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader("src/main/resources/static/txt/addressRegistered.json"));
            JSONArray root = (JSONArray) obj;

            for (Object o : root) {
                JSONObject element = (JSONObject) o;

                if (element.get("address").toString().compareTo(address) == 0){
                    element.put("status", status);
                    element.put("role", role);
                }
            }

            FileWriter file = new FileWriter("src/main/resources/static/txt/addressRegistered.json");
            file.write(root.toJSONString());
            file.flush();
            file.close();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
