var Authentication = artifacts.require("./Authentication.sol");

module.exports = function (deployer) {
  deployer.deploy(Authentication, '0xDFdfB19AaC836406BEE74cfb0003Cb49ead319A5');
};
