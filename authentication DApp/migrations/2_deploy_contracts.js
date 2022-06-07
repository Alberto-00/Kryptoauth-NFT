var Authentication = artifacts.require("./Authentication.sol");

module.exports = function (deployer) {
  deployer.deploy(Authentication, '0x0fD737aFb101884A864E96c8943D8B7c15Ec3C4A');
};
