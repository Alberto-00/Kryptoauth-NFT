// SPDX-License-Identifier: MIT

/********************************************************************************************** 
* Questo smart contract implementa una configurazione tradizionale tra admin e utenti:        *
*                                                                                             *
* -  DEFAULT_ADMIN_ROLE è il ruolo di admin di USER (default);                                *
* -  l'inidirizzo passato al costruttore è l'admin iniziale;                                  *
* -  gli admin possono aggiungere altri amministratori;                                       *
* -  gli admin possono concedere e revocare le autorizzazioni utente a qualsiasi account;     *
* -  l'unico modo per un admin di perdere il proprio ruolo di admin è di rinunciarvi.            *
*                                                                                             *
***********************************************************************************************/

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";

contract Authentication is AccessControl {
    bytes32 public constant USER_ROLE = keccak256("USER");
    
    struct User {
        address addr;
        string name;
        bytes32 password;
        bool isUserLoggedIn;
    }

    mapping(address => User) user;
    mapping(address => Admin) admin;

    constructor(address root) {
        _setupRole(DEFAULT_ADMIN_ROLE, root);
        _setRoleAdmin(USER_ROLE, DEFAULT_ADMIN_ROLE);
    }

    /*Ristretto ai membri che hanno l'admin role.*/
    modifier onlyAdmin(){
        require(isAdmin(msg.sender), "Restricted to admins.");
        _;
    }

    /*Ristretto ai membri che hanno l'user role.*/
    modifier onlyUser(){
        require(isUser(msg.sender), "Restricted to users.");
        _;
    }

    /*Ritorna 'true' se l'account ha il ruolo di admin.*/
    function isAdmin(address account) public virtual view returns (bool) {
        return hasRole(DEFAULT_ADMIN_ROLE, account);
    }
    
    /*Ritorna 'true' se l'account ha il ruolo di user.*/
    function isUser(address account) public virtual view returns (bool) {
        return hasRole(USER_ROLE, account);
    }

    /*Aggiunge un account con il ruolo di user (lo possono fare solo gli admin).*/
    function addUser(address account) public virtual onlyAdmin {
        grantRole(USER_ROLE, account);
    }

    /*Aggiunge un account con il ruolo di admin (lo possono fare solo gli admin).*/
    function addAdmin(address account) public virtual onlyAdmin {
        grantRole(DEFAULT_ADMIN_ROLE, account);
    }

    /*Rimuove un account dal ruolo di user (lo possono fare solo gli admin).*/
    function removeUser(address account) public virtual onlyAdmin {
        revokeRole(USER_ROLE, account);
    }
    
    /*Un admin rinucia ad esserlo.*/
    function renounceAdmin() public virtual {
        renounceRole(DEFAULT_ADMIN_ROLE, msg.sender);
    }

    /*
    * Nella funzione di registrazione dell'utente, se l'utente non si è già 
    * registrato i suoi dati vengono registrati sulla blockchain.
    */
    function registerUser(
        address _address,
        string memory _name,
        string memory _password
    ) public onlyUser returns (bool) {
        require(
            user[_address].addr == address(0x0000000000000000000000000000000000000000), // indirizzo eth nullo
            "already registered"
        );
        user[_address].addr = _address;
        user[_address].name = _name;
        user[_address].password = keccak256(abi.encodePacked(_password));
        user[_address].isUserLoggedIn = false;
        addUser(_address);
        return true;
    }

    
    /**
    * La funzione di login fornisce la possibilità di accedere alla DApp e
    * la modifica del campo isUserLoggedIn su true: questo significa che 
    * l'utente accede correttamente. 
    */
    function loginUser(address _address, string memory _password) public returns (bool) {
        if (user[_address].password == keccak256(abi.encodePacked(_password))) {
            user[_address].isUserLoggedIn = true;
            return user[_address].isUserLoggedIn;
        } else 
            return false;
    }

    // verifica se l'utente è loggato o meno.
    function checkIsUserLogged(address _address) public view returns (bool){
        return user[_address].isUserLoggedIn;
    }

    // logout dell'utente
    function logoutUser(address _address) public {
        user[_address].isUserLoggedIn = false;
    }


    struct Admin {
        address adminAddress;
        string name;
        bytes32 password;
        bool isAdminLoggedIn;
    }
    
    function registerAdmin(
        address _address,
        string memory _name,
        string memory _password
    ) public onlyAdmin returns (bool) {
        require(
            admin[_address].adminAddress == address(0x0000000000000000000000000000000000000000), // indirizzo eth nullo
            "already registered"
        );
        admin[_address].adminAddress = _address;
        admin[_address].name = _name;
        admin[_address].password = keccak256(abi.encodePacked(_password));
        admin[_address].isAdminLoggedIn = false;
        addAdmin(_address);
        return true;
    }

    function loginAdmin(address _address, string memory _password) public returns (bool) {
        if (admin[_address].password == keccak256(abi.encodePacked(_password))) {
            admin[_address].isAdminLoggedIn = true;
            return admin[_address].isAdminLoggedIn;
        } else 
            return false;
    }

    // verifica se l'admin è loggato o meno.
    function checkIsAdminLogged(address _address) public view returns (bool) {
        return admin[_address].isAdminLoggedIn;
    }

    // logout dell'admin
    function logoutAdmin(address _address) public {
        admin[_address].isAdminLoggedIn = false;
    }
}