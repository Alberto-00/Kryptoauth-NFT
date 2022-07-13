module.exports = {
  networks: {
    development: {
     host: "192.168.1.31",
     port: 7545,
     network_id: "*" // Match any network id
    }
  },
  compilers: {
    solc: {
      version: "0.8.0",
    }
  }
};