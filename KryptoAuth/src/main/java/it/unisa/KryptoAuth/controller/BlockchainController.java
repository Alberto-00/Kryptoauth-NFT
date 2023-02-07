package it.unisa.KryptoAuth.controller;

import it.unisa.KryptoAuth.model.AjaxResponse;
import it.unisa.KryptoAuth.model.User;
import it.unisa.KryptoAuth.service.BlockchainServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Gestisce tutte le chiamate POST per effettuare le operazioni di:
 * <ul>
 *     <li>registrazione;</li>
 *     <li>login;</li>
 *     <li>attivazione account;</li>
 *     <li>disattivazione account;</li>
 *     <li>cambio privilegi ad un account.</li>
 * </ul>
 */
@Controller
@RequestMapping(value = "/kryptoauth", method = POST)
public class BlockchainController {
    private final static String PINATA_KEY = "1eeb0837f8d0ae21e000";
    private final static String PINATA_SECRET = "f638ab8f499066727efb6eab377503fb30bd0a797c732cfbfbdada4c89f4f890";


    /*========================================== Authentication Servlet ==============================================*/
    @ResponseBody
    @PostMapping("/login")
    public AjaxResponse loginPost(@Valid @ModelAttribute("user") User user, Errors errors,
                                  @RequestParam(value = "userAddress") String address,
                                  @RequestParam(value = "privateKey") String privateKey,
                                  HttpServletRequest request) throws Exception {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null && session.getAttribute("admin") == null) {
            if (errors.hasFieldErrors("email") || errors.hasFieldErrors("password")) {
                ajaxResponse.addMsg("email", errorMsgField(errors, "email"));
                ajaxResponse.addMsg("password", errorMsgField(errors, "password"));
                return ajaxResponse;
            } else if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
                ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
                return ajaxResponse;
            }
            if (checkPrivateKey(privateKey, ajaxResponse))
                return ajaxResponse;

            BlockchainServiceImpl service = new BlockchainServiceImpl(privateKey);

            if (!service.addressEquals(address)) {
                ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                return ajaxResponse;
            }

            if (service.isAdmin(address)) {
                if (!service.loginAdmin(address, user.getEmail(), user.getPassword()))
                    ajaxResponse.addMsg("loginError", "incorrect");
                else {
                    ajaxResponse.addMsg("successAdmin", "ok");
                    if (session.getAttribute("noRole") != null &&
                            session.getAttribute("noRole").toString().compareToIgnoreCase(address) == 0)
                        session.removeAttribute("noRole");

                    session.setAttribute("admin", address);
                    session.setAttribute("service", service);
                }
            } else if (service.isUser(address)) {
                if (!service.loginUser(address, user.getEmail(), user.getPassword()))
                    ajaxResponse.addMsg("loginError", "incorrect");
                else {
                    ajaxResponse.addMsg("successUser", "ok");
                    if (session.getAttribute("noRole") != null &&
                            session.getAttribute("noRole").toString().compareToIgnoreCase(address) == 0)
                        session.removeAttribute("noRole");

                    session.setAttribute("user", address);
                    session.setAttribute("service", service);
                }
            } else
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
            if (address == null || address.compareTo("undefined") == 0 || address.isEmpty()) {
                ajaxResponse.addMsg("userAddress", "Nessun account rilevato. Accedere a Metamask.");
                return ajaxResponse;
            }
            if (checkPrivateKey(privateKey, ajaxResponse))
                return ajaxResponse;

            BlockchainServiceImpl service = new BlockchainServiceImpl(privateKey);

            if (!service.addressEquals(address)){
                ajaxResponse.addMsg("notEqualsAddress", "incorrect");
                return ajaxResponse;
            }
            return registerForRoles(user, address, ajaxResponse, session, service);
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
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
        return false;
    }

    @NotNull
    private AjaxResponse registerForRoles(@ModelAttribute("user") @Valid User user,
                                          @RequestParam("userAddress") String address,
                                          AjaxResponse ajaxResponse, HttpSession session,
                                          BlockchainServiceImpl service) {
        try {
            if (user.getRole().compareToIgnoreCase("User") == 0 && !service.isAdmin(address)) {
                service.registerUser(address, user.getEmail(), user.getPassword());
                session.setAttribute("service", service);
                session.setAttribute("noRole", address);
                ajaxResponse.addMsg("success", "user");
                writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Non Attivo,");
                return ajaxResponse;
            }
            else if (user.getRole().compareToIgnoreCase("Admin") == 0 && !service.isUser(address)) {
                service.registerUser(address, user.getEmail(), user.getPassword());

                session.setAttribute("service", service);

                if (service.isAdmin(address)) {
                    session.setAttribute("admin", address);
                    writeAddress(address + "," + user.getEmail() + "," + user.getRole() + ",Attivo,");
                    ajaxResponse.addMsg("adminActive", "admin");
                } else {
                    session.setAttribute("noRole", address);
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

    @ResponseBody
    @PostMapping("/activeAddress")
    public AjaxResponse activeAddress(@RequestParam(value = "address") String address,
                                      @RequestParam(value = "role") String role,
                                      @RequestParam(value = "status") String status,
                                      @RequestParam(value = "addressMetamask") String addressMetamask,
                                      HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null && session.getAttribute("service") != null){
            if (address != null && !address.isEmpty() &&
                    role != null && !role.isEmpty() &&
                    status != null && !status.isEmpty() &&
                    addressMetamask != null && !addressMetamask.isEmpty()){

                BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
                if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    addressMetamask.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
                    ajaxResponse.addMsg("notEqualsAddress", "error");
                    return ajaxResponse;
                }
                if (role.compareToIgnoreCase("user") == 0){
                    if (currentService.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) == 0){
                        ajaxResponse.addMsg("revokeAdmin", "ok");
                        return ajaxResponse;
                    } else if (currentService.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
                        ajaxResponse.addMsg("notRevoke", "error");
                        return ajaxResponse;
                    } else if (currentService.isUser(address)){
                        ajaxResponse.addMsg("alreadyActive", "error");
                        return ajaxResponse;
                    }
                    currentService.addUser(address);
                    if (status.compareToIgnoreCase("non attivo") == 0)
                        status = "Attivo";
                    updateAddress(address, status, role);
                    ajaxResponse.addMsg("success", status + "," + role);
                    return ajaxResponse;
                }
                else if (role.compareToIgnoreCase("admin") == 0) {
                    if (currentService.isAdmin(address)){
                        ajaxResponse.addMsg("alreadyActive", "error");
                        return ajaxResponse;
                    }
                    if (currentService.isUser(address))
                        currentService.removeUser(address);

                    currentService.addAdmin(address);
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
                                      @RequestParam(value = "addressMetamask") String addressMetamask,
                                      HttpServletRequest request) throws Exception {

        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null && session.getAttribute("service") != null){
            if (address != null && !address.isEmpty() &&
                    role != null && !role.isEmpty() &&
                    status != null && !status.isEmpty() &&
                    addressMetamask != null && !addressMetamask.isEmpty()){

                BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
                if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                        addressMetamask.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
                    ajaxResponse.addMsg("notEqualsAddress", "error");
                    return ajaxResponse;
                }
                return checkRevokeRole(address, role, status, addressMetamask, ajaxResponse, session);
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
            if (session.getAttribute("admin") != null && session.getAttribute("service") != null
                    && session.getAttribute("admin").toString().compareToIgnoreCase(address) == 0){
                BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");

                JSONArray jsonNfts = currentService.getMyNfts_json();
                String nftToBeAssigned = nftToBeAssigned(currentService, jsonNfts,address);
                if (nftToBeAssigned != null){
                    ajaxResponse.addMsg("nftToBeAssigned", nftToBeAssigned);
                    return ajaxResponse;
                }
                StringBuilder burned = new StringBuilder();
                for (Object jsonObject: jsonNfts){
                    JSONObject element = (JSONObject) jsonObject;
                    burned.append((element.get("url").toString()).split("https://gateway.pinata.cloud/ipfs/")[1]).append(",");
                }
                currentService.addUser(address);
                currentService.removeAdmin(address);
                session.removeAttribute("admin");
                session.setAttribute("user", address);
                ajaxResponse.addMsg("burned", burned.toString());
                ajaxResponse.addMsg("pinata_key", PINATA_KEY);
                ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
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
            if (session.getAttribute("admin") != null && session.getAttribute("service") != null
                    && session.getAttribute("admin").toString().compareToIgnoreCase(address) == 0){
                BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
                if (currentService.isUser(address)) {
                    /*
                    Rimuovere NFT user
                     */
                    currentService.removeUser(address);
                    session.removeAttribute("user");
                }

                if (currentService.isAdmin(address)) {
                    JSONArray jsonNfts = currentService.getMyNfts_json();
                    String nftToBeAssigned = nftToBeAssigned(currentService, jsonNfts, address);
                    if (nftToBeAssigned != null){
                        ajaxResponse.addMsg("nftToBeAssigned", nftToBeAssigned);
                        return ajaxResponse;
                    }
                    StringBuilder burned = new StringBuilder();
                    for (Object jsonObject: jsonNfts){
                        JSONObject element = (JSONObject) jsonObject;
                        burned.append((element.get("url").toString()).split("https://gateway.pinata.cloud/ipfs/")[1]).append(",");
                    }
                    ajaxResponse.addMsg("burned", burned.toString());
                    ajaxResponse.addMsg("pinata_key", PINATA_KEY);
                    ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
                    currentService.removeAdmin(address);
                    session.removeAttribute("admin");
                }
                ajaxResponse.addMsg("redirect", "ok");
                updateAddress(address, "Non Attivo", null);
            } else  ajaxResponse.addMsg("error", "error");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.addMsg("error", "error");
        }
        return ajaxResponse;
    }

    @NotNull
    private AjaxResponse checkRevokeRole(@RequestParam("address") String address,
                                         @RequestParam("role") String role,
                                         @RequestParam("status") String status,
                                         @RequestParam("addressMetamask") String addressMetamask,
                                         AjaxResponse ajaxResponse, HttpSession session) throws Exception {
        BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
        if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                addressMetamask.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
            ajaxResponse.addMsg("notEqualsAddress", "incorrect");
            return ajaxResponse;
        }
        if (currentService.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) == 0){
            ajaxResponse.addMsg("revoke", "ok");
            return ajaxResponse;
        }
        else if (currentService.isAdmin(address) && address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0){
            ajaxResponse.addMsg("notRevoke", "error");
            return ajaxResponse;
        }
        currentService.removeUser(address);
        if (status.compareToIgnoreCase("Attivo") == 0)
            status = "Non Attivo";

        session.removeAttribute("user");
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

                    if (role != null)
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

    /* Se un Admin ha un NFT ancora da assegnare, allora non può perdere il ruolo di Admin */
    private String nftToBeAssigned(BlockchainServiceImpl currentService,
                                   JSONArray jsonNfts, String address) throws Exception {
        for (Object jsonObject: jsonNfts){
            JSONObject element = (JSONObject) jsonObject;

            if (element.get("owner").toString().compareToIgnoreCase(address) != 0)
                return element.get("name").toString();
        }

        for (Object jsonObject: jsonNfts){
            JSONObject element = (JSONObject) jsonObject;
            currentService.burnNft(new BigInteger(element.get("tokenId").toString()));
        }
        return null;
    }


    /*================================== NFT Marketplace Servlet (Admin Role) ========================================*/
    @ResponseBody
    @PostMapping("/marketplace/open-marketplace")
    public AjaxResponse flipMarketplace(@RequestParam(value = "addressMetamask") String addrMetamask,
                                        @RequestParam(value = "flag") boolean flag,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null &&
                session.getAttribute("service") != null && addrMetamask != null){

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    (!currentService.addressEquals(addrMetamask) && addrMetamask.compareTo("start") != 0)) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }
            currentService.flipSaleState(flag);
            ajaxResponse.addMsg("flag", String.valueOf(flag));
        }
        else
            ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace")
    public AjaxResponse adminViewMarketplace(@RequestParam(value = "addressMetamask") String addrMetamask,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null &&
                session.getAttribute("service") != null && addrMetamask != null){

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    (!currentService.addressEquals(addrMetamask) && addrMetamask.compareTo("start") != 0)) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }
            ajaxResponse.addMsg("jsonData", currentService.getMyNfts_string());
            ajaxResponse.addMsg("adminAddress", session.getAttribute("admin").toString());
        }
        else
            ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace/pinata/create-nft")
    public AjaxResponse adminViewNftsOnPinata(HttpServletRequest request){
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("admin") != null){
            ajaxResponse.addMsg("adminAddress", session.getAttribute("admin").toString());
            ajaxResponse.addMsg("pinata_key", PINATA_KEY);
            ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
        }
        else ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace/create-nft")
    public AjaxResponse createNft(@RequestParam(value = "name") String name,
                            @RequestParam(value = "category") String category,
                            @RequestParam(value = "price") String price,
                            @RequestParam(value = "validUntil") String validUntil,
                            @RequestParam(value = "sale") String sale,
                            @RequestParam(value = "addressMetamask") String addressMetamask,
                            @RequestParam(value = "description") String description,
                            HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        AjaxResponse ajaxResponse = new AjaxResponse();

        if (addressMetamask != null && session.getAttribute("admin") != null &&
            session.getAttribute("service") != null) {

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    addressMetamask.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0 ) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }

            if (name == null || name.isBlank() || name.compareToIgnoreCase("KriptoToken") == 0) {
                ajaxResponse.addMsg("name", "error");
                return ajaxResponse;
            }

            if (!regexField("category", category)) {
                ajaxResponse.addMsg("category", "error");
                return ajaxResponse;
            }

            if (!regexField("^[0-9]*$", price) && Integer.parseInt(price) < 1
                    && Integer.parseInt(price) > 99999) {
                ajaxResponse.addMsg("price", "error");
                return ajaxResponse;
            }

            if (!regexField("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", validUntil)) {
                ajaxResponse.addMsg("validUntil", "error");
                return ajaxResponse;
            }

            if (!regexField("^[0-9]*$", sale) &&
                    Integer.parseInt(price) < 5 && Integer.parseInt(price) > 50) {
                ajaxResponse.addMsg("sale", "error");
                return ajaxResponse;
            }

            if (description == null){
                ajaxResponse.addMsg("description", "error");
                return ajaxResponse;
            }
            else if (description.isEmpty() || description.length() > 2000){
                ajaxResponse.addMsg("description", "error");
                return ajaxResponse;
            }

            String upper = category.substring(0, 1);
            String str = upper.toUpperCase() + category.substring(1);

            ajaxResponse.addMsg("ok", "ok");
            ajaxResponse.addMsg("name", name);
            ajaxResponse.addMsg("category", str);
            ajaxResponse.addMsg("seller", addressMetamask);
            ajaxResponse.addMsg("owner", addressMetamask);
            ajaxResponse.addMsg("price", price);
            ajaxResponse.addMsg("validUntil", validUntil);
            ajaxResponse.addMsg("sale", sale);
            ajaxResponse.addMsg("description", description);
            ajaxResponse.addMsg("pinata_key", PINATA_KEY);
            ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
            return ajaxResponse;
        }
        else if (session.getAttribute("user") != null) {
            ajaxResponse.addMsg("invalidUser", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace/save-nft")
    public AjaxResponse saveNftOnBlockchain(@RequestParam(value = "name") String name,
                                  @RequestParam(value = "category") String category,
                                  @RequestParam(value = "url") String url,
                                  @RequestParam(value = "description") String description,
                                  @RequestParam(value = "price") String price,
                                  @RequestParam(value = "validUntil") String validUntil,
                                  @RequestParam(value = "sale") String sale,
                                  @RequestParam(value = "address") String address,
                                  HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        AjaxResponse ajaxResponse = new AjaxResponse();

        if (address != null && session.getAttribute("admin") != null &&
            session.getAttribute("service") != null) {
            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            String upper = category.substring(0, 1);
            String str = upper.toUpperCase() + category.substring(1);

            long days = givenValidUntilDifference(validUntil);
            if (!currentService.mintNft(name, str, description, url, new BigInteger(price),
                    BigInteger.valueOf(days), new BigInteger(sale))){
                ajaxResponse.addMsg("errorMint", "error");
                return ajaxResponse;
            }
            ajaxResponse.addMsg("ok", "ok");
            return ajaxResponse;
        }
        else if (session.getAttribute("user") != null) {
            ajaxResponse.addMsg("invalidUser", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace/delete-nft")
    public AjaxResponse deleteNft(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "address") String address,
                                  HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        AjaxResponse ajaxResponse = new AjaxResponse();

        if (address != null && session.getAttribute("admin") != null &&
                session.getAttribute("service") != null) {

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0 ) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }
            if (currentService.balanceOf(session.getAttribute("admin").toString(), new BigInteger(id)) == 1L){
                String jsonString = currentService.getNftById(
                        session.getAttribute("admin").toString(), new BigInteger(id));
                JSONObject obj = (JSONObject) new JSONParser().parse(jsonString);

                if (obj.get("sold").toString().compareTo("false") == 0 &&
                    obj.get("owner").toString().compareToIgnoreCase(address) == 0){
                    if (currentService.burnNft(new BigInteger(id))){
                        String[] url = (obj.get("url").toString()).split("https://gateway.pinata.cloud/ipfs/");
                        ajaxResponse.addMsg("hash", url[1]);
                        ajaxResponse.addMsg("pinata_key", PINATA_KEY);
                        ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
                        return ajaxResponse;
                    }
                    ajaxResponse.addMsg("errorBurn", "error");
                    return ajaxResponse;
                }
                ajaxResponse.addMsg("bought", "error");
                return ajaxResponse;
            }
            ajaxResponse.addMsg("notNft", "ok");
            return ajaxResponse;
        }
        else if (session.getAttribute("user") != null) {
            ajaxResponse.addMsg("invalidUser", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    @ResponseBody
    @PostMapping("/marketplace/delete-nft-expired")
    public AjaxResponse deleteNftExpired(@RequestParam(value = "addressMetamask") String address,
                                         HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        AjaxResponse ajaxResponse = new AjaxResponse();

        if (address != null && session.getAttribute("admin") != null &&
                session.getAttribute("service") != null) {

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("admin").toString()) ||
                    address.compareToIgnoreCase(session.getAttribute("admin").toString()) != 0 ) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }

            JSONArray jsonNfts = currentService.getMyNfts_json();
            StringBuilder burned = new StringBuilder();

            for (Object jsonObject: jsonNfts){
                JSONObject element = (JSONObject) jsonObject;

                if (!currentService.isValidNft(new BigInteger(element.get("tokenId").toString())) &&
                    element.get("owner").toString().compareToIgnoreCase(element.get("seller").toString()) == 0){

                    currentService.burnNft(new BigInteger(element.get("tokenId").toString()));
                    burned.append(element.get("url").toString().split("https://gateway.pinata.cloud/ipfs/")[1]).append(",");
                }
            }
            ajaxResponse.addMsg("burned", burned.toString());
            ajaxResponse.addMsg("jsonData", currentService.getMyNfts_string());
            ajaxResponse.addMsg("pinata_key", PINATA_KEY);
            ajaxResponse.addMsg("pinata_secret", PINATA_SECRET);
            return ajaxResponse;
        }
        else if (session.getAttribute("user") != null) {
            ajaxResponse.addMsg("invalidUser", "error");
            return ajaxResponse;
        }
        ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }

    private boolean regexField(String pattern, String field) {
        if (field != null && !field.isBlank() && pattern != null) {
            if (pattern.compareTo("category") == 0) {
                return field.compareToIgnoreCase("Accessori") == 0 || field.compareToIgnoreCase("Corsi") == 0 ||
                        field.compareToIgnoreCase("Documentazione") == 0 || field.compareToIgnoreCase("Incontri") == 0;
            }
            else {
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(field);
                return m.find();
            }
        }
        return false;
    }

    private long givenValidUntilDifference(String validUntil) throws java.text.ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = sdf.parse(validUntil);
        Date secondDate = new Date();

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
    }


    /*=================================== NFT Marketplace Servlet (User Role) ========================================*/
    @ResponseBody
    @PostMapping("/marketplace-user")
    public AjaxResponse userViewMarketplace(@RequestParam(value = "addressMetamask") String addrMetamask,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null &&
                session.getAttribute("service") != null && addrMetamask != null){

            BlockchainServiceImpl currentService = (BlockchainServiceImpl) session.getAttribute("service");
            if (!currentService.addressEquals(session.getAttribute("user").toString()) ||
                    (!currentService.addressEquals(addrMetamask) && addrMetamask.compareTo("start") != 0)) {
                ajaxResponse.addMsg("invalidUser", "error");
                return ajaxResponse;
            }
            ajaxResponse.addMsg("jsonData", currentService.getNftsAllAdmin());
        }
        else
            ajaxResponse.addMsg("sessionEmpty", "error");
        return ajaxResponse;
    }
}
