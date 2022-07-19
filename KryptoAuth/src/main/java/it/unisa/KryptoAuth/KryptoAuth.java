package it.unisa.KryptoAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@SpringBootApplication
public class KryptoAuth {
	public static void main(String[] args) {
		Web3j web3j = Web3j.build(new HttpService());

		SpringApplication.run(KryptoAuth.class, args);
	}
}
