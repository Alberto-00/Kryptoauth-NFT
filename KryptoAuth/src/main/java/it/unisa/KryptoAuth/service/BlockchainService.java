package it.unisa.KryptoAuth.service;

import it.unisa.KryptoAuth.contracts.Authentication;

public interface BlockchainService {

    Authentication loadContract(String account);
    Authentication deploy(String account)throws Exception;
    boolean isContractLoaded(String address) throws Exception;
    boolean addressEquals(String address) throws Exception;
    boolean isAdmin(String address) throws Exception;
    boolean isUser(String address) throws Exception;
    boolean registerUser(String address, String name, String password) throws Exception;
    boolean loginUser(String address, String name, String password) throws Exception;
    boolean loginAdmin(String address, String name, String password) throws Exception;
    boolean addUser(String address) throws Exception;
    boolean addAdmin(String address) throws Exception;
    boolean removeUser(String address) throws Exception;
    boolean removeAdmin(String address) throws Exception;
}
