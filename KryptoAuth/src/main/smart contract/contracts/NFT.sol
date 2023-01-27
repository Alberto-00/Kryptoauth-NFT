// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "./Authentication.sol";

contract NFT is ERC1155, Authentication {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    uint256 private priceToken = 0.04 ether;
    bool private isSaleActive;

    struct NFTtoken {
        uint256 id;
        string name;
        uint256 price;
        uint256 validUntil;
        address toUser;
        bool sold;
    }

    mapping (uint256 => NFTtoken) nft;

    constructor() ERC1155("") {
        _tokenIds.increment();
        isSaleActive = false;
    }

    function supportsInterface(bytes4 interfaceId) public view virtual override(ERC1155, AccessControl) returns (bool) {
        return super.supportsInterface(interfaceId);
    }

    /*================================ Admin functions =====================================*/
    /* Only Admin can create a new NFT in Bloackchain, settings the name and the days limit */
    function mintNft(
        string memory nftName,
        uint256 price,
        uint256 daysLimit
    ) public onlyAdmin {
        require(daysLimit > 0 && price > 0, "Days limit and price must be greater than zero");
        require(_tokenIds.current() > 0, "Error id");
        require(balanceOf(msg.sender, _tokenIds.current()) == 0, "NFT already exist");

        _mint(msg.sender, _tokenIds.current(), 1, bytes(nftName));
        createNFT(_tokenIds.current(), nftName, price, daysLimit);
        _tokenIds.increment();
    }

    function createNFT(
        uint256 _tokenId,
        string memory _nftName,
        uint256 _price,
        uint256 _daysLimit
    ) private onlyAdmin {
        nft[_tokenId].id = _tokenId;
        nft[_tokenId].name = _nftName;
        nft[_tokenId].price = _price;
        nft[_tokenId].validUntil = block.timestamp + (_daysLimit * 1 days);
        nft[_tokenId].toUser = address(0x0);
        nft[_tokenId].sold = false;
    }

    /* Only Admin can burn the NFT that he didn't sell */
    function burnNFT(uint256 _tokenId) public onlyAdmin {
        require(_tokenId > 0 && nft[_tokenId].id > 0, "This is not an NFT");
        require(balanceOf(msg.sender, _tokenId) > 0, "This address not have this NFT");
        require(!nft[_tokenId].sold, "Token already sold");

        nft[_tokenId].id = 0;
        _burn(msg.sender, _tokenId, 1);
    }

    /* Only Admin can transfer the NFT sold */
    function assignNFT(uint256 _tokenId, address _addr) public onlyAdmin {
        require(_tokenId > 0 && nft[_tokenId].id > 0, "This is not an NFT");
        require(balanceOf(msg.sender, _tokenId) > 0, "This address not have this NFT");
        require(isUser(_addr), "This address isn't an User");
        require(nft[_tokenId].toUser != address(0x0) && nft[_tokenId].toUser == _addr, "User address wrong");
        require(!nft[_tokenId].sold, "NFT already sold");

        nft[_tokenId].sold = true;
        nft[_tokenId].toUser = address(0x0);
        safeTransferFrom(msg.sender, _addr, _tokenId, 1, bytes(nft[_tokenId].name));
    }


    /*================================ User functions =====================================*/
    /* Only User can buy KryptoToken (KT) */
    function buyFt(uint256 amounts) public payable onlyUser {
        require(amounts > 0, "Amounts must be greater than zero");
        require(msg.value >= priceToken * amounts, "WRONG! Not enough money sent");

        _mint(msg.sender, 0, amounts, "KryptoToken");
    }

    /* User buy NFTs */
    function buyNFT(uint256 _tokenId) public onlyUser {
        require(isSaleActive, "The marketplace is closed");
        require(_tokenId > 0 && nft[_tokenId].id == _tokenId, "NFT not exist");
        require(nft[_tokenId].toUser == address(0x0), "NFT already sold");
        require(balanceOf(msg.sender, 0) >= nft[_tokenId].price, "Not enough tokens");

        nft[_tokenId].toUser = msg.sender;
        _burn(msg.sender, 0, nft[_tokenId].price);
    }

    /* Assigns the ether to the User that sold his coins */
    function sellFt(uint256 amounts) public onlyUser {
        require(balanceOf(msg.sender, 0) >= amounts, "Not enough token");

        _burn(msg.sender, 0, amounts);
        payable(msg.sender).transfer(amounts * priceToken);
    }

    /* Assigns the coins to the User that sold his NFT */
    function sellNFTUser(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0 && nft[_tokenId].id > 0, "This is not a NFT");
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");

        nft[_tokenId].id = 0;
        _burn(msg.sender, _tokenId, 1);
        _mint(msg.sender, 0, nft[_tokenId].price, "KryptoToken");
    }

    /* Returns number of the User's coins */
    function getTokensUser() public view onlyUser returns (uint256) {
        return balanceOf(msg.sender, 0);
    }


    /*================================= Other Functions ====================================*/
    /* Returns uri of a specific Token */
    function tokenURI(uint256 _nftId) public view returns (string memory) {
        require(nft[_nftId].id == _nftId, "URI: not exist this NFT");

        return string(abi.encodePacked(super.uri(_nftId), Strings.toString(_nftId), ".json"));
    }

    /* Returns true if NFT doesn't expired */
    function isValid(uint256 _nftId) public view returns (bool) {
        return block.timestamp < nft[_nftId].validUntil;
    }

    /* Updates the state of the sale */
    function flipSaleState(bool flag) public onlyAdmin {
        isSaleActive = flag;
    }

    /* Updates the price of the Token coin */
    function setTokenPrice(uint256 _priceToken) public onlyAdmin {
        priceToken = _priceToken;
    }
}
