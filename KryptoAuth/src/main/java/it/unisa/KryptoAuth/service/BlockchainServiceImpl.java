package it.unisa.KryptoAuth.service;

import it.unisa.KryptoAuth.contracts.KryptoNFT;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

/**
 * Implementa i servizi di business dell'applicazione, in particolare, richiama i metodi dello
 * Smart Contract {@link KryptoNFT} per effettuare le transazioni sulla Blockchain.
 */
public class BlockchainServiceImpl implements BlockchainService {

    private final static Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
    private final static String CONTRACT_ADDRESS = "0xeC75Acd1eae4C0Fe33b3b5BB3441d152fADf30DF";
    private final static String ipfs = "https://gateway.pinata.cloud/ipfs/";
    private final KryptoNFT kryptoNFT;

    public BlockchainServiceImpl(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        kryptoNFT = KryptoNFT.load(CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
    }


    /*===================================== Access Control contract ===========================================*/
    @Override
    public boolean isAdmin(String address) throws Exception {
        return kryptoNFT.isAdmin(address).send();
    }

    @Override
    public boolean isUser(String address) throws Exception {
        return kryptoNFT.isUser(address).send();
    }

    @Override
    public boolean registerUser(String address, String name, String password) throws Exception {
        return kryptoNFT.registerUser(address, name, password).send().isStatusOK();
    }

    @Override
    public boolean loginUser(String address, String name, String password) throws Exception {
        return kryptoNFT.loginUser(address, name, password).send();
    }

    @Override
    public boolean loginAdmin(String address, String name, String password) throws Exception {
        return kryptoNFT.loginAdmin(address, name, password).send();
    }

    @Override
    public boolean addUser(String address) throws Exception {
        return kryptoNFT.addUser(address).send().isStatusOK();
    }

    @Override
    public boolean addAdmin(String address) throws Exception {
        return kryptoNFT.addAdmin(address).send().isStatusOK();
    }

    @Override
    public boolean removeUser(String address) throws Exception {
        return kryptoNFT.removeUser(address).send().isStatusOK();
    }

    @Override
    public boolean removeAdmin(String address) throws Exception {
        return kryptoNFT.renounceAdmin(address).send().isStatusOK();
    }

    @Override
    public boolean isContractLoaded(String address) throws Exception {
        return kryptoNFT != null && kryptoNFT.getAddress().send().compareToIgnoreCase(address) == 0;
    }

    @Override
    public boolean addressEquals(String address) throws Exception {
        return kryptoNFT.getAddress().send().compareToIgnoreCase(address) == 0;
    }

    @Override
    public String getName(String address) throws Exception {
        return kryptoNFT.getName(address).send();
    }


    /*======================================= KryptoNFT contract ==============================================*/
    @Override
    public boolean mintNft(String name, String category, String description,
                           String url, BigInteger price, BigInteger validUntil, BigInteger sale) throws Exception {
        return kryptoNFT.mintNft(name, category, description, ipfs + url, price, validUntil, sale).send().isStatusOK();
    }

    @Override
    public boolean burnNft(BigInteger id) throws Exception {
        return kryptoNFT.burnNFT(id).send().isStatusOK();
    }

    @Override
    public boolean assignNft(BigInteger id, String address) throws Exception {
        return kryptoNFT.assignNFT(id, address).send().isStatusOK();
    }

    @Override
    public boolean buyFt(BigInteger amounts) throws Exception {
        return kryptoNFT.buyFt(amounts).send().isStatusOK();
    }

    @Override
    public boolean sellFt(BigInteger amounts) throws Exception {
        return kryptoNFT.sellFt(amounts).send().isStatusOK();
    }

    @Override
    public boolean buyNft(BigInteger id) throws Exception {
        Object obj = new JSONParser().parse(kryptoNFT.getNft(id).send());
        JSONObject nft = (JSONObject) obj;
        return kryptoNFT.buyNFT(id, new BigInteger(nft.get("price").toString())).send().isStatusOK();
    }

    @Override
    public boolean sellNftUser(BigInteger id) throws Exception {
        return kryptoNFT.sellNFTUser(id).send().isStatusOK();
    }

    @Override
    public boolean useNft(BigInteger id) throws Exception {
        return kryptoNFT.useNFT(id).send().isStatusOK();
    }

    @Override
    public boolean burnNftUser(BigInteger id) throws Exception {
        return kryptoNFT.burnNFTUser(id).send().isStatusOK();
    }

    @Override
    public boolean isValidNft(BigInteger id) throws Exception {
        return kryptoNFT.isValid(id).send();
    }

    @Override
    public boolean flipSaleState(boolean flag) throws Exception {
        return kryptoNFT.flipSaleState(flag).send().isStatusOK();
    }

    @Override
    public boolean isMarketplaceActive() throws Exception {
        return kryptoNFT.isMarketplaceActive().send();
    }

    @Override
    public boolean setTokenPrice(BigInteger price) throws Exception {
        return kryptoNFT.setTokenPrice(price).send().isStatusOK();
    }

    @Override
    public BigInteger getTokensUser() throws Exception {
        return kryptoNFT.getTokensUser().send();
    }

    @Override
    public JSONArray getMyNfts_json() throws Exception {
        Object obj = new JSONParser().parse(kryptoNFT.getMyNfts().send());
        return  (JSONArray) obj;
    }

    @Override
    public String getMyNfts_string() throws Exception {
        return kryptoNFT.getMyNfts().send();
    }

    @Override
    public String getNftById(BigInteger id) throws Exception{
        return kryptoNFT.getNft(id).send();
    }

    @Override
    public String getNftsAllAdmin() throws Exception {
        return kryptoNFT.getNftsAllAdmin().send();
    }

    @Override
    public JSONArray getNftsAddr(String address) throws Exception {
        Object obj = new JSONParser().parse(kryptoNFT.getNftsAddr(address).send());
        return  (JSONArray) obj;
    }

    @Override
    public long balanceOf(String address, BigInteger id) throws Exception{
        return kryptoNFT.balanceOf(address, id).send().longValue();
    }
}
