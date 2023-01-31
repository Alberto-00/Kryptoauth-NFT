// Use the api keys by providing the strings directly
require('dotenv').config();

const { PINATA_API_KEY, PINATA_API_SECRET } = process.env;
const pinataSDK = require('@pinata/sdk');
const pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

pinata.testAuthentication().then((result) => {
    console.log(result); // response Authentication5

    getCID("prova").then((pinCID) => {
        deletePin(pinCID)
    })

}).catch((err) => {
    //handle error here
    console.log(err);
});

async function getCID(name){
    const filters = {
        status : "pinned",
        pageLimit : 1000,
        metadata : {
            name: name
        }
    };
    const pin = await pinata.pinList(filters);
    return pin.rows[0].ipfs_pin_hash;
}

function deletePin(ipfsPinHash){
    pinata.unpin(ipfsPinHash).then((result) => {
        //handle results here
        console.log(result);
    }).catch((err) => {
        //handle error here
        console.log(err);
    });
}