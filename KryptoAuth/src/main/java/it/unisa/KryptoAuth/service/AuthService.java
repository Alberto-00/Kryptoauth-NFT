package it.unisa.KryptoAuth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;

import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger (AuthService.class);

    @Autowired
    private Web3j web3j;

    public EthAccounts getEthAccounts() throws ExecutionException, InterruptedException {
        return this.web3j.ethAccounts()
                .sendAsync()
                .get();
    }


}
