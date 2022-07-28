package it.unisa.KryptoAuth.service;

import it.unisa.KryptoAuth.contracts.Authentication;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    private final static Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
    private final static String CONTRACT_ADDRESS = "0x8D234c808e4D47C00C7611325D780bB98093561E";
    private static String privateKey;
    private static Authentication authentication = null;

    @Override
    public Authentication deploy(String privateKey) throws Exception{
        Credentials credentials = Credentials.create(privateKey);
        return Authentication.deploy(web3j, credentials, new DefaultGasProvider()).send();
    }

    @Override
    public Authentication loadContract(String privateKey) {
        BlockchainServiceImpl.privateKey = privateKey;
        Credentials credentials = Credentials.create(BlockchainServiceImpl.privateKey);
        authentication = Authentication.load(CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
        return authentication;
    }

    @Override
    public boolean isAdmin(String address) throws Exception {
        return loadContract(privateKey).isAdmin(address).send();
    }

    @Override
    public boolean isUser(String address) throws Exception {
        return loadContract(privateKey).isUser(address).send();
    }

    @Override
    public boolean registerUser(String address, String name, String password) throws Exception {
        return loadContract(privateKey).registerUser(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean loginUser(String address, String name, String password) throws Exception {
        return loadContract(privateKey).loginUser(address, name, password).send();
    }

    @Override
    public boolean loginAdmin(String address, String name, String password) throws Exception {
        return loadContract(privateKey).loginAdmin(address, name, password).send();
    }

    @Override
    public boolean addUser(String address) throws Exception {
        return loadContract(privateKey).addUser(address).send().isStatusOK();
    }

    @Override
    public boolean addAdmin(String address) throws Exception {
        return loadContract(privateKey).addAdmin(address).send().isStatusOK();
    }

    @Override
    public boolean removeUser(String address) throws Exception {
        return loadContract(privateKey).removeUser(address).send().isStatusOK();
    }

    @Override
    public boolean removeAdmin(String address) throws Exception {
        return loadContract(privateKey).renounceAdmin(address).send().isStatusOK();
    }

    @Override
    public boolean isContractLoaded(String address) throws Exception {
        return authentication != null && authentication.getAddress().send().compareToIgnoreCase(address) == 0;
    }

    @Override
    public boolean addressEquals(String address) throws Exception {
        return authentication.getAddress().send().compareToIgnoreCase(address) == 0;
    }
}
