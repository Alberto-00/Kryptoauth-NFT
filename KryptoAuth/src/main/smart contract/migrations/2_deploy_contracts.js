var KryptoNFT = artifacts.require("./KryptoNFT.sol");

module.exports = function (deployer) {
  deployer.deploy(KryptoNFT);
};