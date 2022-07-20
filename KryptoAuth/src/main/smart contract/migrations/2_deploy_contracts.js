var Authentication = artifacts.require("./Authentication.sol");

module.exports = function (deployer) {
  deployer.deploy(Authentication, "0xbA41B88de483924bcB80d430EcE4D83b44e8Aaa4");
};
