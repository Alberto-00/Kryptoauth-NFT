// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";
import "./Authentication.sol";

contract KryptoNFT is ERC1155, Authentication, ReentrancyGuard {
    using Counters for Counters.Counter;
    Counters.Counter private _nftCount;

    uint256 private priceToken = 0.04 ether; // prezzo di un KryproToken (KT)
    bool private isSaleActive = false; // marketplace è aperto oppure chiuso

    mapping(uint256 => NFT) private _idToNFT; // associa il tokenId univoco a una struttura NFT.

    struct NFT {
        uint256 tokenId;
        address seller;
        address owner;
        string name;
        string category;
        uint256 price;
        uint256 validUntil;
        uint256 sale;
        bool sold;
        bool burned;
    }

    constructor() ERC1155("") {
    }

    function supportsInterface(bytes4 interfaceId) public view virtual override(ERC1155, AccessControl) returns (bool) {
        return super.supportsInterface(interfaceId);
    }

    /*================================ Admin functions =====================================*/
    /* Only Admin can create a new NFT in Blockchain */
    function mintNft(
        string memory _name,
        string memory _category,
        uint256 _price,
        uint256 _daysLimit,
        uint256 _sale
    ) public onlyAdmin {
        require(_daysLimit > 0 && _price > 0, "Days limit and price must be greater than zero");
        require(_sale > 4 &&_sale < 50, "Sale error");

        _nftCount.increment();
        uint256 newTokenId = _nftCount.current();
        require(balanceOf(msg.sender, newTokenId) == 0, "NFT already exist");

        _mint(msg.sender, newTokenId, 1, bytes(_name));
        _idToNFT[newTokenId] = NFT(
            newTokenId,
            msg.sender,
            msg.sender,
            _name,
            _category,
            _price,
            block.timestamp + (_daysLimit * 1 days),
            _sale,
            false,
            false
        );
    }

    /* Only Admin can burn the NFT that he didn't sell */
    function burnNFT(uint256 _tokenId) public onlyAdmin {
        require(_tokenId > 0, "This is not an NFT");
        require(balanceOf(msg.sender, _tokenId) > 0, "This address not have this NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(!nft.sold, "Token already sold");
        require(nft.owner == msg.sender && nft.owner == nft.seller, "NFT already bought");

        nft.burned = true;
        _burn(msg.sender, _tokenId, 1);
    }

    /* Only Admin can transfer the NFT sold to account which bought that */
    function assignNFT(uint256 _tokenId, address _addr) public onlyAdmin {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId) > 0, "This address not have this NFT");
        require(isUser(_addr), "This address isn't an User");
        require(nft.owner == _addr, "User address wrong");
        require(!nft.sold, "NFT already sold");

        nft.sold = true;
        safeTransferFrom(msg.sender, _addr, _tokenId, 1, bytes(nft.name));
    }


    /*================================ User functions =====================================*/
    /* Only User can buy KryptoToken (KT) */
    function buyFt(uint256 amounts) public payable onlyUser nonReentrant{
        require(amounts > 0, "Amounts must be greater than zero");
        require(msg.value == priceToken * amounts, "WRONG! Not enough money sent");

        _mint(msg.sender, 0, amounts, "KryptoToken");
    }

    /* User buy NFTs and the seller gets paid; the User has to wait admin to assign him the NFT */
    function buyNFT(uint256 _tokenId) public payable onlyUser nonReentrant {
        require(isSaleActive, "The marketplace is closed");
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(!nft.burned, "NFT doesn't exist");
        require(balanceOf(msg.sender, 0) >= nft.price, "Not enough tokens");
        require(nft.owner == nft.seller, "NFT already sold");
        require(isValid(_tokenId), "NFT is expired");

        nft.owner = msg.sender;
        _burn(msg.sender, 0, nft.price);
        payable(nft.seller).transfer(priceToken);
    }

    /* Assigns the ether to the User that sold his coins */
    function sellFt(uint256 amounts) public payable onlyUser nonReentrant {
        require(amounts > 0, "Amounts have not be 0");
        require(balanceOf(msg.sender, 0) >= amounts, "Not enough token");

        _burn(msg.sender, 0, amounts);
        payable(msg.sender).transfer(amounts * priceToken);
    }

    /* Assigns the coins to the User that sold his NFT and transfers the NFT to seller */
    function sellNFTUser(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");
        require(isValid(_tokenId), "NFT is expired");

        nft.sold = false;
        nft.owner = nft.seller;
        safeTransferFrom(msg.sender, nft.seller, _tokenId, 1, bytes(nft.name));
        _mint(msg.sender, 0, nft.price, "KryptoToken");
    }

    /* User burn his NFT and transfers its to seller */
    function useNFT(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");
        require(isValid(_tokenId), "NFT is expired");

        nft.sold = false;
        nft.owner = nft.seller;
        safeTransferFrom(msg.sender, nft.seller, _tokenId, 1, bytes(nft.name));
    }

    /* User burn his NFT expired */
    function burnNFTexpired(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");
        require(!isValid(_tokenId), "NFT isn't expired");

        nft.sold = false;
        nft.burned = true;
        nft.owner = nft.seller;
        _burn(msg.sender, _tokenId, 1);
    }

    /*================================= Other Functions ====================================*/
    /* Returns uri of a specific Token */
    function tokenURI(uint256 _tokenId) public view returns (string memory) {
        NFT storage nft = _idToNFT[_tokenId];
        require(!nft.burned, "URI: not exist this NFT");

        return string(abi.encodePacked(super.uri(_tokenId), Strings.toString(_tokenId), ".json"));
    }

    /* Returns true if NFT doesn't expired */
    function isValid(uint256 _tokenId) public view returns (bool) {
        NFT storage nft = _idToNFT[_tokenId];
        return block.timestamp < nft.validUntil;
    }

    /* Updates the state of the sale */
    function flipSaleState(bool _flag) public onlyAdmin {
        isSaleActive = _flag;
    }

    /* Updates the price of the Token coin */
    function setTokenPrice(uint256 _priceToken) public onlyAdmin {
        priceToken = _priceToken;
    }

    /* Returns number of the User's coins */
    function getTokensUser() public view onlyUser returns (uint256) {
        return balanceOf(msg.sender, 0);
    }

    /* Returns NFTs of an address  */
    function getMyNfts() public view returns (NFT[] memory) {
        uint nftCount = _nftCount.current();
        uint myNftCount = 0;
        for (uint i = 0; i < nftCount; i++) {
            if (balanceOf(msg.sender, _idToNFT[i + 1].tokenId) == 1) {
                myNftCount++;
            }
        }

        NFT[] memory nfts = new NFT[](myNftCount);
        uint nftsIndex = 0;
        for (uint i = 0; i < nftCount; i++) {
            if (balanceOf(msg.sender, _idToNFT[i + 1].tokenId) == 1) {
                nfts[nftsIndex] = _idToNFT[i + 1];
                nftsIndex++;
            }
        }
        return nfts;
    }
}