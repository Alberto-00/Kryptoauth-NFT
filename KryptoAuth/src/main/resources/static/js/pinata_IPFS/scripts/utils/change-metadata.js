// Use the api keys by providing the strings directly
require('dotenv').config();

const { PINATA_API_KEY, PINATA_API_SECRET } = process.env;
const pinataSDK = require('@pinata/sdk');
const pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

pinata.testAuthentication().then((result) => {
    console.log(result); // response Authentication5

    getCID("prova").then((pinCID) => {
        //updatePin(pinCID)
    })

}).catch((err) => {
    //handle error here
    console.log(err);
});

async function getCID(name){
    const metadataFilter = {
        name: 'exampleName',
        keyvalues: {
            testKeyValue: {
                value: 'exampleFilterValue',
                op: 'eq'
            },
            testKeyValue2: {
                value: 'exampleFilterValue2',
                op: 'eq'
            }
        }
    };

    const filters = {
        status : "pinned",
        pageLimit : 1000,
        pageOffset: 0,
    };
    const pin = await pinata.pinList(filters);
    console.log(pin.rows[0].metadata.keyvalues)
    return pin.rows[0].ipfs_pin_hash;
}

function updatePin(ipfsPinHash){
    const metadata = {
        name: 'prova',
        keyvalues: {
            rew: 'newValue', //aggiunge una nuova chiave
            rew: 'pep', //modifica il valore di una chiave
            existingKey: null //elimina una chiave esistente
        }
    };

    var name = "gg"; // insert name
    pinata.hashMetadata(ipfsPinHash, metadata).then((result) => {
        //handle results here
        console.log(result);
    }).catch((err) => {
        //handle error here
        console.log(err);
    });
}