// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.17;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";
import "@BokkyPooBahsDateTimeLibrary/contracts/BokkyPooBahsDateTimeLibrary.sol";
import "./Authentication.sol";

contract KryptoNFT is ERC1155, Authentication, ReentrancyGuard {
    using Counters for Counters.Counter;
    Counters.Counter private _nftCount;

    uint256 private priceToken = 0.04 ether; // KryproToken (KT) price
    bool private isSaleActive = false; // opened or closed marketplace

    mapping(uint256 => NFT) private _idToNFT; // associates the unique tokenId with an NFT structure

    struct NFT {
        uint256 tokenId;
        address seller;
        address owner;
        string name;
        string url;
        string category;
        string description;
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


    /*================================ Admin Functions =====================================*/
    /* Only Admin can create a new NFT in Blockchain */
    function mintNft(
        string memory _name,
        string memory _category,
        string memory _description,
        string memory _url,
        uint256 _price,
        uint256 _daysLimit,
        uint256 _sale
    ) public onlyAdmin {
        require(_daysLimit > 0 && _price > 0, "Days limit and price must be greater than zero");
        require(_sale > 4 && _sale < 51, "Sale error");

        _nftCount.increment();
        uint256 newTokenId = _nftCount.current();
        require(balanceOf(msg.sender, newTokenId) == 0, "NFT already exist");

        _mint(msg.sender, newTokenId, 1, bytes(_name));
        _idToNFT[newTokenId] = NFT(
            newTokenId,
            msg.sender,
            msg.sender,
            _name,
            _url,
            _category,
            _description,
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


    /*================================ User Functions =====================================*/
    /* Only User can buy KryptoToken (KT) */
    function buyFt(uint256 _amounts) public payable onlyUser nonReentrant{
        require(_amounts > 0, "Amounts must be greater than zero");
        require(msg.value == priceToken * _amounts, "WRONG! Not enough money sent");

        _mint(msg.sender, 0, _amounts, "KryptoToken");
    }

    /* User buy NFTs and the seller gets paid; the User has to wait admin to assign him the NFT */
    function buyNFT(uint256 _tokenId) public payable onlyUser nonReentrant {
        require(isSaleActive, "The marketplace is closed");
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(!nft.burned, "NFT doesn't exist");
        require(balanceOf(msg.sender, 0) >= nft.price, "Not enough tokens");
        require(nft.owner == nft.seller, "NFT already sold");
        require(isAdmin(nft.seller), "Seller is not an Admin");
        require(isValid(_tokenId), "NFT is expired");

        nft.owner = msg.sender;
        _burn(msg.sender, 0, nft.price);
        payable(nft.seller).transfer(priceToken * nft.price);
    }

    /* Assigns the ether to the User that sold his coins */
    function sellFt(uint256 _amounts) public payable onlyUser nonReentrant {
        require(_amounts > 0, "Amounts have not be 0");
        require(balanceOf(msg.sender, 0) >= _amounts, "Not enough token");

        _burn(msg.sender, 0, _amounts);
        payable(msg.sender).transfer(_amounts * priceToken);
    }

    /* Assigns the coins to the User that sold his NFT and transfers the NFT to seller */
    function sellNFTUser(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");
        require(isValid(_tokenId), "NFT is expired");

        if(isAdmin(nft.seller)){
            nft.sold = false;
            nft.owner = nft.seller;
            safeTransferFrom(msg.sender, nft.seller, _tokenId, 1, bytes(nft.name));
        }
        else{
            nft.burned = true;
            _burn(msg.sender, _tokenId, 1);
        }
        _mint(msg.sender, 0, nft.price, "KryptoToken");
    }

    /* User burn his NFT and transfers its to seller if he still has the Admin role */
    function useNFT(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");
        require(isValid(_tokenId), "NFT is expired");

        if(isAdmin(nft.seller)){
            nft.sold = false;
            nft.owner = nft.seller;
            safeTransferFrom(msg.sender, nft.seller, _tokenId, 1, bytes(nft.name));
        }
        else{
            nft.burned = true;
            _burn(msg.sender, _tokenId, 1);
        }
    }

    /* User burn his NFT expired */
    function burnNFTUser(uint256 _tokenId) public onlyUser {
        require(_tokenId > 0, "This is not an NFT");

        NFT storage nft = _idToNFT[_tokenId];
        require(balanceOf(msg.sender, _tokenId)  > 0 , "This address not have this NFT");

        nft.burned = true;
        _burn(msg.sender, _tokenId, 1);
    }


    /*================================= Other Functions ====================================*/
    /* Returns true if NFT doesn't expired */
    function isValid(uint256 _tokenId) public view returns (bool) {
        NFT storage nft = _idToNFT[_tokenId];
        return block.timestamp <= nft.validUntil;
    }

    /* Updates the state of the sale */
    function flipSaleState(bool _flag) public onlyAdmin {
        isSaleActive = _flag;
    }

    /* Returns the state of the sale */
    function isMarketplaceActive() public view returns (bool) {
        return isSaleActive;
    }

    /* Updates the price of the Token coin */
    function setTokenPrice(uint256 _priceToken) public onlyAdmin {
        priceToken = _priceToken;
    }

    /* Returns number of the User's coins */
    function getTokensUser() public view onlyUser returns (uint256) {
        return balanceOf(msg.sender, 0);
    }

    /* Returns NFTs of all Admins */
    function getNftsAllAdmin() public view returns (string memory) {
        string memory data = "[";
        uint size = _nftCount.current();
        bool flag = false;

        for (uint i = 0; i < size; i++) {
            if (!_idToNFT[i + 1].sold && balanceOf(_idToNFT[i + 1].seller, _idToNFT[i + 1].tokenId) == 1){
                if(!flag){
                    flag = true;
                    data = string.concat(data, getNft(i + 1));
                }
                else
                    data = string.concat(data, ',', getNft(i + 1));
            }
        }
        data = string.concat(data, "]");
        return data;
    }

    /* Returns NFTs of an address */
    function getNftsAddr(address _addr) public view returns (string memory) {
        string memory data = "[";
        uint size = _nftCount.current();
        bool flag = false;

        for (uint i = 0; i < size; i++) {
            if (balanceOf(_addr, _idToNFT[i + 1].tokenId) == 1){
                if(!flag){
                    flag = true;
                    data = string.concat(data, getNft(i + 1));
                }
                else
                    data = string.concat(data, ',', getNft(i + 1));
            }
        }
        data = string.concat(data, "]");
        return data;
    }

    /* Returns NFTs of the address that load this contract  */
    function getMyNfts() public view returns (string memory) {
        string memory data = string(abi.encodePacked("["));
        uint size = _nftCount.current();
        bool flag = false;

        for (uint i = 0; i < size; i++) {
            if (balanceOf(msg.sender, _idToNFT[i + 1].tokenId) == 1){
                if(!flag){
                    flag = true;
                    data = string.concat(data, getNft(i + 1));
                }
                else
                    data = string.concat(data, ',', getNft(i + 1));
            }
        }
        data = string.concat(data, "]");
        return data;
    }

    /* Returns size of my NFTs */
    function getSizeMyNfts() public view returns (uint){
        uint nftCount = _nftCount.current();
        uint myNftCount = 0;
        for (uint i = 0; i < nftCount; i++) {
            if (balanceOf(msg.sender, _idToNFT[i + 1].tokenId) == 1) {
                myNftCount++;
            }
        }
        return myNftCount;
    }

    /* Returns NFT by id */
    function getNft(uint256 _id) public view returns (string memory) {
        string memory data = string.concat('{"tokenId":"', Strings.toString(_idToNFT[_id].tokenId),
            '", "seller":"', _addressToString(_idToNFT[_id].seller),'", "owner":"', _addressToString(_idToNFT[_id].owner),
            '", "name":"', _idToNFT[_id].name,'", "category":"', _idToNFT[_id].category, '", "url":"', _idToNFT[_id].url,
            '", "description":"', _idToNFT[_id].description,'", "price":"', Strings.toString(_idToNFT[_id].price),
            '", "validUntil":"', getDateToString(_idToNFT[_id].validUntil));

        data = string.concat(data,'", "sale":"', Strings.toString(_idToNFT[_id].sale),'", "sold":"');

        if(_idToNFT[_id].sold)
            data = string.concat(data,'true"}');
        else
            data = string.concat(data,'false"}');

        return data;
    }


    /*================================= Private Functions ====================================*/
    /* Converts address type to string type */
    function _addressToString(address addr) private pure returns(string memory) {
        bytes memory addressBytes = abi.encodePacked(addr);
        bytes memory stringBytes = new bytes(42);

        stringBytes[0] = '0';
        stringBytes[1] = 'x';

        for(uint i = 0; i < 20; i++){
            uint8 leftValue = uint8(addressBytes[i]) / 16;
            uint8 rightValue = uint8(addressBytes[i]) - 16 * leftValue;

            bytes1 leftChar = leftValue < 10 ? bytes1(leftValue + 48) : bytes1(leftValue + 87);
            bytes1 rightChar = rightValue < 10 ? bytes1(rightValue + 48) : bytes1(rightValue + 87);

            stringBytes[2 * i + 3] = rightChar;
            stringBytes[2 * i + 2] = leftChar;
        }
        return string(stringBytes);
    }

    /* Returns day of month of timestamp */
    function day(uint timestamp) private pure returns (uint256){
        return BokkyPooBahsDateTimeLibrary.getDay(timestamp);
    }

    /* Returns year of month of timestamp */
    function year(uint timestamp) private pure returns (uint256){
        return BokkyPooBahsDateTimeLibrary.getYear(timestamp);
    }

    /* Returns month of month of timestamp */
    function month(uint timestamp) private pure returns (uint256){
        return BokkyPooBahsDateTimeLibrary.getMonth(timestamp);
    }

    /* Returns date in format YYYY-MM-DD */
    function getDateToString(uint _timestamp) private pure returns (string memory){
        uint _day = BokkyPooBahsDateTimeLibrary.getDay(_timestamp);
        uint _month = BokkyPooBahsDateTimeLibrary.getMonth(_timestamp);
        uint _year = BokkyPooBahsDateTimeLibrary.getYear(_timestamp);

        return string.concat(Strings.toString(_year), '-', Strings.toString(_month),'-', Strings.toString(_day));
    }
}