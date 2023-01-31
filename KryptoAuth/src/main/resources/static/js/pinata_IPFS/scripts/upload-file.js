require('dotenv').config();

const { PINATA_API_KEY, PINATA_API_SECRET } = process.env;
const pinataSDK = require('@pinata/sdk');
const fs = require('fs');
const pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

pinata.testAuthentication().then((result) => {
    console.log(result);

    const readableStreamForFile = fs.createReadStream('../../../img/favicon.png');
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