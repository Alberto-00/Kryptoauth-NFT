// SPDX-License-Identifier: MIT

/********************************************************************************************** 
* Questo smart contract implementa una configurazione tra admin e utenti:                     *
*                                                                                             *
* -  DEFAULT_ADMIN_ROLE è il ruolo di admin di USER (default);                                *
* -  l'indirizzo passato al costruttore è l'admin iniziale;                                   *
* -  gli admin possono aggiungere altri amministratori;                                       *
* -  gli admin possono concedere e revocare le autorizzazioni utente a qualsiasi account;     *
* -  l'unico modo per un admin di perdere il proprio ruolo di admin è di rinunciarvi.         *
*                                                                                             *
* Quando un utente o un admin si registrano entrambi non hanno un ruolo che verrà asseggnato  *
* in seguito dall'admin con "addUser" o "addAdmin".                                           *
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

    constructor() {
        _setupRole(DEFAULT_ADMIN_ROLE, msg.sender);
        //_setRoleAdmin(USER_ROLE, DEFAULT_ADMIN_ROLE);
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
    function addUser(address account) public virtual onlyAdmin returns (bool){
        if(!hasRole(USER_ROLE, account) &&
            !hasRole(DEFAULT_ADMIN_ROLE, account)){
            grantRole(USER_ROLE, account);
            return true;
        } return false;
    }

    /*Aggiunge un account con il ruolo di admin (lo possono fare solo gli admin).*/
    function addAdmin(address account) public virtual onlyAdmin returns (bool){
        if(!hasRole(DEFAULT_ADMIN_ROLE, account)){
            removeUser(account);
            grantRole(DEFAULT_ADMIN_ROLE, account);
            return true;
        } return false;
    }

    /*Rimuove un account dal ruolo di user (lo possono fare solo gli admin).*/
    function removeUser(address account) public virtual onlyAdmin returns (bool){
        if(hasRole(USER_ROLE, account)){
            revokeRole(USER_ROLE, account);
            return true;
        } return false;
    }

    /*Un admin rinucia ad esserlo.*/
    function renounceAdmin(address account) public virtual onlyAdmin returns (bool){
        if(hasRole(DEFAULT_ADMIN_ROLE, account)){
            renounceRole(DEFAULT_ADMIN_ROLE, account);
            return true;
        } return false;
    }

    function getAddress() public view returns (address) {
        return msg.sender;
    }

    /************************************************************************
    * Nella funzione di registrazione dell'utente, se l'utente non si è già  
    * registrato i suoi dati vengono registrati sulla blockchain.
    */
    function registerUser(
        address _address,
        string memory _name,
        string memory _password
    ) public returns (bool) {
        require(user[_address].addr == address(0x0000000000000000000000000000000000000000), "already registered");

        user[_address].addr = _address;
        user[_address].name = _name;
        user[_address].password = keccak256(abi.encodePacked(_password));
        user[_address].isUserLoggedIn = true;
        return true;
    }

    /***********************************************************************
    * La funzione di login fornisce la possibilità di accedere alla DApp   *
    * e la modifica del campo isUserLoggedIn su true: questo significa che *
    * l'utente accede correttamente.                                       *
    ************************************************************************/
    function loginUser(address _address, string memory _name, string memory _password) public returns (bool) {
        //keccak256(abi.encodePacked(...)) è usato per comparare le stringhe e richiede meno gas
        if (isUser(_address) && user[_address].password == keccak256(abi.encodePacked(_password)) &&
            keccak256(abi.encodePacked(user[_address].name)) == keccak256(abi.encodePacked(_name))) {

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
    ) public returns (bool) {
        require(admin[_address].adminAddress == address(0x0000000000000000000000000000000000000000), "already registered");

        admin[_address].adminAddress = _address;
        admin[_address].name = _name;
        admin[_address].password = keccak256(abi.encodePacked(_password));
        admin[_address].isAdminLoggedIn = true;
        return true;
    }

    function loginAdmin(address _address, string memory _name, string memory _password) public returns (bool) {
        if (isAdmin(_address) && admin[_address].password == keccak256(abi.encodePacked(_password)) &&
            keccak256(abi.encodePacked(admin[_address].name)) == keccak256(abi.encodePacked(_name))) {

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