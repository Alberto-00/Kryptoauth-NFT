
const pinataSDK = require('@pinata/sdk');
const fs = require('fs');
const pinata = new pinataSDK("1eeb0837f8d0ae21e000", "f638ab8f499066727efb6eab377503fb30bd0a797c732cfbfbdada4c89f4f890");

pinata.testAuthentication().then((result) => {
    console.log(result);

    const readableStreamForFile = fs.createReadStream('prova.png');
    const options = {
        pinataMetadata: {
            name: "prova",
            keyvalues: {
                customKey: 'customValue',
                customKey2: 'customValue2'
            }
        }
    };
    pinata.pinFileToIPFS(readableStreamForFile, options).then((result) => {
        //handle results here
        console.log(result);
    }).catch((err) => {
        //handle error here
        console.log(err);
    });

}).catch((err) => {
    //handle error here
    console.log(err);
});