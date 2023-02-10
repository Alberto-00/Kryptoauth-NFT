package it.unisa.KryptoAuth.service;

import org.json.simple.JSONArray;
import java.math.BigInteger;

public interface BlockchainService {

    boolean isContractLoaded(String address) throws Exception;
    boolean addressEquals(String address) throws Exception;


    /*============================== Access Control contract ====================================*/
    boolean isAdmin(String address) throws Exception;
    boolean isUser(String address) throws Exception;
    boolean registerUser(String address, String name, String password) throws Exception;
    boolean loginUser(String address, String name, String password) throws Exception;
    boolean loginAdmin(String address, String name, String password) throws Exception;
    boolean addUser(String address) throws Exception;
    boolean addAdmin(String address) throws Exception;
    boolean removeUser(String address) throws Exception;
    boolean removeAdmin(String address) throws Exception;
    String getName(String address) throws Exception;


    /*================================== KryptoNFT contract =====================================*/
    boolean mintNft(String name, String category, String description, String url,
                    BigInteger price, BigInteger validUntil, BigInteger sale) throws Exception;
    boolean burnNft(BigInteger id) throws Exception;
    boolean assignNft(BigInteger id, String address) throws Exception;
    boolean buyFt(BigInteger amounts) throws Exception;
    boolean sellFt(BigInteger amounts) throws Exception;
    boolean buyNft(BigInteger id) throws Exception;
    boolean sellNftUser(BigInteger id) throws Exception;
    boolean useNft(BigInteger id) throws Exception;
    boolean burnNftUser(BigInteger id) throws Exception;
    boolean isValidNft(BigInteger id) throws Exception;
    boolean flipSaleState(boolean flag) throws Exception;
    boolean isMarketplaceActive() throws Exception;
    boolean setTokenPrice(BigInteger price) throws Exception;
    BigInteger getTokensUser() throws Exception;
    JSONArray getMyNfts_json() throws Exception;
    String getMyNfts_string() throws Exception;
    String getNftsAllAdmin() throws Exception;
    String getNftById(BigInteger id) throws Exception;
    JSONArray getNftsAddr(String address) throws Exception;
    long balanceOf(String address, BigInteger id) throws Exception;
}
