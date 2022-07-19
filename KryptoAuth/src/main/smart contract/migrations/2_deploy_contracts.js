var Authentication = artifacts.require("./Authentication.sol");

module.exports = function (deployer) {
  deployer.deploy(Authentication, "0xeA196D3d90FEf3902c1DC4044FeFbeb300A09770");
};
