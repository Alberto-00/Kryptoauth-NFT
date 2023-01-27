// SPDX-License-Identifier: MIT

/********************************************************************************************** 
* Il seguente Smart Contract fornisce il servizio di autenticazione assicurando la sicurezza  *
* mediante la definizione di ruoli specifici tra il semplice utente e l'amministratore;       *
* il tutto è permesso grazie all'utilizzo di OpenZeppelin:                                    *
*                                                                                             *
* -  DEFAULT_ADMIN_ROLE è il ruolo di admin;                                                  *
* -  USER_ROLE è il ruolo di utente;                                                          *
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
    bytes32 private constant USER_ROLE = keccak256("USER");

    struct User {
        address addr;
        string name;
        bytes32 password;
    }

    mapping(address => User) private user;

    constructor() {
        _setupRole(DEFAULT_ADMIN_ROLE, msg.sender);
        _setRoleAdmin(USER_ROLE, DEFAULT_ADMIN_ROLE);
    }


    /*=============================================== Setup roles ====================================================*/
    /* Restricted to members who have the admin role */
    modifier onlyAdmin(){
        require(isAdmin(msg.sender), "Restricted to admins.");
        _;
    }

    /* Restricted to members who have the user role */
    modifier onlyUser(){
        require(isUser(msg.sender), "Restricted to users.");
        _;
    }

    /* Returns 'true' if account has admin role */
    function isAdmin(address account) public view returns (bool) {
        return hasRole(DEFAULT_ADMIN_ROLE, account);
    }

    /* Returns 'true' if account has user role */
    function isUser(address account) public view returns (bool) {
        return hasRole(USER_ROLE, account);
    }

    /* Adds an account with the user role (restricted to admins) */
    function addUser(address account) public onlyAdmin {
        require(user[account].addr == account, "Address not registered");
        require(!hasRole(USER_ROLE, account), "Account already is an User");

        grantRole(USER_ROLE, account);
    }

    /* Change role account with the admin role and revokes the user role (restricted to admins) */
    function addAdmin(address account) public onlyAdmin {
        require(user[account].addr == account, "Address not registered");
        require(!hasRole(DEFAULT_ADMIN_ROLE, account), "Account already is an Admin");

        grantRole(DEFAULT_ADMIN_ROLE, account);
    }

    /* Removes user role from an account dal ruolo di user (restricted to admins) */
    function removeUser(address account) public onlyAdmin {
        require(user[account].addr == account, "Address not registered");
        require(hasRole(USER_ROLE, account), "Account isn't an User");

        revokeRole(USER_ROLE, account);
    }

    /* Admin renounces his admin role */
    function renounceAdmin(address account) public onlyAdmin {
        require(user[account].addr == account, "Address not registered");
        require(hasRole(DEFAULT_ADMIN_ROLE, account), "Account isn't an Admin");

        renounceRole(DEFAULT_ADMIN_ROLE, account);
    }

    /* Returns the address that call this function */
    function getAddress() public view returns (address) {
        return msg.sender;
    }


    /*========================================= Register / Login functions ===========================================*/
    /* If the user is not already registered, his data is recorded on the blockchain */
    function registerUser(
        address _address,
        string memory _name,
        string memory _password
    ) public {
        require(user[_address].addr != _address, "already registered");

        user[_address].addr = _address;
        user[_address].name = _name;
        user[_address].password = keccak256(abi.encodePacked(_password));
    }

    /* Login User */
    function loginUser(address _address, string memory _name, string memory _password) public view returns (bool) {
        //keccak256(abi.encodePacked(...)) è usato per comparare le stringhe e richiede meno gas
        if (isUser(_address) && user[_address].password == keccak256(abi.encodePacked(_password)) &&
            keccak256(abi.encodePacked(user[_address].name)) == keccak256(abi.encodePacked(_name))) {
            return true;
        }
        return false;
    }

    /* Login Admin */
    function loginAdmin(address _address, string memory _name, string memory _password) public view returns (bool) {
        if (isAdmin(_address) && user[_address].password == keccak256(abi.encodePacked(_password)) &&
            keccak256(abi.encodePacked(user[_address].name)) == keccak256(abi.encodePacked(_name))) {
            return true;
        }
        return false;
    }
}