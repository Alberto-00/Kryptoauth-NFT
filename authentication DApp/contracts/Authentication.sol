/************************************************************************************************************** 
* I dati di accesso dell'utente vengono memorizzati come hash alla Blockchain tramite questo smart contract,  *
* infatti, ogni volta che un utente effettua il login ad una WebApp, l'hash ricavato dalle credenziali        *
* fornite dall'utente viene confrontato con l'hash memorizzato nello smart contract:                          *
*                                                                                                             *
*   - se c'è una corrispondenza, l'utente è autorizzato ad accedere al sito Web;                              *
*   - in caso contrario, l'accesso è negato.                                                                  *
*                                                                                                             *
***************************************************************************************************************/

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";

contract Authentication is ERC20, AccessControl {

    struct User {
        address addr;
        string name;
        string password;
        string ipfsImageHash;
        bool isUserLoggedIn;
    }

    mapping(address => User) user;

    address adminAddress;

    constructor() ERC20("UnisaToken", "UTK"){
        adminAddress = 0x5B38Da6a701c568545dCfcB03FcB875f56beddC4;
    }

    /*
    * Nella funzione di registrazione dell'utente, l'utente può effettuare la transazione su Meta Mask.
    * La funzione verifica che se l'utente non si sia già registrato richiedendo il controllo:
    * se l'utente non si registra già, i dati dell'utente vengono registrati sulla blockchain.
    */
    function registerUser(
        address _address,
        string memory _name,
        string memory _password,
        string memory _ipfsImageHash
    ) public notAdmin returns (bool) {
        //require(user[_address].addr != msg.sender);
        require(
            user[_address].addr ==
                address(0x0000000000000000000000000000000000000000), // indirizzo eth nullo
            "already registered"
        );
        user[_address].addr = _address;
        user[_address].name = _name;
        user[_address].password = _password;
        user[_address].ipfsImageHash = _ipfsImageHash;
        user[_address].isUserLoggedIn = false;
        return true;
    }

    
    /**
    * La funzione di login fornisce la possibilità di accedere alla Dapp e
    * la modifica del campo isUserLoggedIn su true: questo significa che 
    * l'utente accede correttamente. 
    */
    function loginUser(address _address, string memory _password) public returns (bool) {
        if (keccak256(abi.encodePacked(user[_address].password)) == keccak256(abi.encodePacked(_password))) {
            user[_address].isUserLoggedIn = true;
            return user[_address].isUserLoggedIn;
        } else 
            return false;
    }

    // check the user logged In or not
    function checkIsUserLogged(address _address) public view returns (bool, string memory){
        return (user[_address].isUserLoggedIn, user[_address].ipfsImageHash);
    }

    // logout the user
    function logoutUser(address _address) public {
        user[_address].isUserLoggedIn = false;
    }


    struct Admin {
        address adminAddress;
        string name;
        string password;
        string ipfsImageHash;
        bool isAdminLoggedIn;
    }
    
    mapping(address => Admin) admin;
    // admin registration function

    modifier onlyAdmin() {
        require(msg.sender == adminAddress);
        _;
    }

    modifier notAdmin() {
        require(msg.sender != adminAddress);
        _;
    }

    function registerAdmin(
        address _address,
        string memory _name,
        string memory _password,
        string memory _ipfsImageHash
    ) public onlyAdmin returns (bool) {
        //require(admin[_address].adminAddress != msg.sender);
        require(
            admin[_address].adminAddress ==
                address(0x0000000000000000000000000000000000000000), // indirizzo eth nullo
            "already registered"
        );
        admin[_address].adminAddress = _address;
        admin[_address].name = _name;
        admin[_address].password = _password;
        admin[_address].ipfsImageHash = _ipfsImageHash;
        admin[_address].isAdminLoggedIn = false;
        return true;
    }

    // admin login function
    function loginAdmin(address _address, string memory _password) public returns (bool) {
        if (keccak256(abi.encodePacked(admin[_address].password)) == keccak256(abi.encodePacked(_password))) {
            admin[_address].isAdminLoggedIn = true;
            return admin[_address].isAdminLoggedIn;
        } else 
            return false;
    }

    // check the admin logged In or not
    function checkIsAdminLogged(address _address) public view returns (bool, string memory) {
        return (admin[_address].isAdminLoggedIn, admin[_address].ipfsImageHash);
    }

    // logout the admin
    function logoutAdmin(address _address) public {
        admin[_address].isAdminLoggedIn = false;
    }

    function getAdminBalance(address _address) public view returns (uint256) {
        return (admin[_address].adminAddress.balance);
    }
}