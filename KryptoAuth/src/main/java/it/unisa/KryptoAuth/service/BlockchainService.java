package it.unisa.KryptoAuth.service;

import it.unisa.KryptoAuth.contracts.Authentication;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface BlockchainService {

    Authentication loadContract(String account) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException;
    void deploy(String account)throws Exception;
    boolean isAdmin(String address) throws Exception;
    boolean isUser(String address) throws Exception;
    boolean registerUser(String address, String name, String password) throws Exception;
    boolean registerAdmin(String address, String name, String password) throws Exception;
    boolean loginUser(String address, String name, String password) throws Exception;
    boolean loginAdmin(String address, String name, String password) throws Exception;
    boolean isUserLogged(String address) throws Exception;
    boolean isAdminLogged(String address) throws Exception;
    void logoutUser(String address) throws Exception;
    void logoutAdmin(String address) throws Exception;
    void addUser(String address) throws Exception;
    void addAdmin(String address) throws Exception;
    void removeUser(String address) throws Exception;
}
