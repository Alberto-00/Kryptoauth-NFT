package it.unisa.KryptoAuth.service;

import it.unisa.KryptoAuth.contracts.Authentication;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    private final static Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.1.31:7545"));
    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    private final static String CONTRACT_ADDRESS = "0x41576bBC16b53cAf7718D52279b3991f5384D910";
    private final static String ROOT_ADDRESS = "0xbA41B88de483924bcB80d430EcE4D83b44e8Aaa4";

    @Override
    public void deploy(String account)throws Exception{
        Credentials credentials = Credentials.create(account);
        ECKeyPair keyPair = credentials.getEcKeyPair();
        BigInteger privateKey = keyPair.getPrivateKey();
        BigInteger publicKey = keyPair.getPublicKey();
        Authentication authentication = Authentication.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT, ROOT_ADDRESS).send();
        System.out.println(authentication.isAdmin(account));
    }

    @Override
    public Authentication loadContract(String account) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(account);
        ECKeyPair keyPair = credentials.getEcKeyPair();
        BigInteger privateKey = keyPair.getPrivateKey();
        BigInteger publicKey = keyPair.getPublicKey();
        return Authentication.load(CONTRACT_ADDRESS, web3j, Credentials.create("3fe47c59f0b979e284a6199822315e9609cea3a0b7fe35054d662bb8ae2c410c"), GAS_PRICE, GAS_LIMIT);
    }

    @Override
    public boolean isAdmin(String address) throws Exception {
        return loadContract(address).isAdmin(address).send();
    }

    @Override
    public boolean isUser(String address) throws Exception {
        return loadContract(address).isAdmin(address).send();
    }

    @Override
    public boolean registerUser(String address, String name, String password) throws Exception {
        return loadContract(address).registerUser(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean registerAdmin(String address, String name, String password) throws Exception {
        return loadContract(address).registerAdmin(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean loginUser(String address, String name, String password) throws Exception {
        return loadContract(address).loginUser(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean loginAdmin(String address, String name, String password) throws Exception {
        return loadContract(address).loginAdmin(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean isUserLogged(String address) throws Exception {
        return loadContract(address).checkIsUserLogged(address).send();
    }

    @Override
    public boolean isAdminLogged(String address) throws Exception {
        return loadContract(address).checkIsAdminLogged(address).send();
    }

    @Override
    public void logoutUser(String address) throws Exception {
        loadContract(address).logoutUser(address).send();
    }

    @Override
    public void logoutAdmin(String address) throws Exception {
        loadContract(address).logoutAdmin(address).send();
    }

    @Override
    public void addUser(String address) throws Exception {
        loadContract(address).addUser(address).send();
    }

    @Override
    public void addAdmin(String address) throws Exception {
        loadContract(address).addAdmin(address).send();
    }

    @Override
    public void removeUser(String address) throws Exception {
        loadContract(address).removeUser(address).send();
    }
}
