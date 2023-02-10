const pinataSDK = require('@pinata/sdk');
var pinata;
var flag = false;
window.userWalletAddress = null;
$(document).ready(function (){
    sendAddressToBackend();

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
    })

    $(".shadow").click(function () {
        closePopupError()
        closePopupSuccess()
        if (flag) window.location.href = "/kryptoauth/shop"
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
    });

    $('#confirmPopupSuccess').click(function (){
        closePopupSuccess()
        window.location.href = "/kryptoauth/shop"
    })

    $('#burn').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else
                ajaxDeleteNft($userAddress.val());
        }
    })

    $('#buyNft').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else
                ajaxBuyNft($userAddress.val());
        }
    })

    $('#transferNft').click(function () {
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else
                ajaxTransferToUserNft($userAddress.val());
        }
    })

    $('#useNft').click(function () {
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else
                ajaxUseOrSellNft($userAddress.val(), "use");
        }
    })

    $('#sellNft').click(function () {
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else
                ajaxUseOrSellNft($userAddress.val(), "sell");
        }
    })
})

function ajaxUseOrSellNft(address, choice){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/use-or-sell-nft",
        data:{
            id: getRequestParam("id"),
            address: address,
            choice: choice
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['errorOwner'] != null){
                flag = false;
                openPopupError()

                if (choice === "use"){
                    $('div.error-p').children('p').eq(1)
                        .html("Impossibile attivare questo NFT." +
                            "<br>Non è stato acquistato da questo account.");
                }
                else {
                    $('div.error-p').children('p').eq(1)
                        .html("Impossibile vendere questo NFT." +
                            "<br>Non è stato acquistato da questo account.");
                }

            }
            else if (data.msgError['expired'] != null){
                flag = false;
                openPopupError()
                if (choice === "use"){
                    $('div.error-p').children('p').eq(1)
                        .html("Impossibile attivare questo NFT." +
                            "<br>NFT scaduto.");
                }
                else {
                    $('div.error-p').children('p').eq(1)
                        .html("Impossibile vendere questo NFT." +
                            "<br>NFT scaduto.");
                }
            }
            else {
                const hash = data.msgError['hash'];
                const PINATA_API_KEY = data.msgError['pinata_key'];
                const PINATA_API_SECRET = data.msgError['pinata_secret'];
                pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                pinata.testAuthentication().then((result) => {
                    if (data.msgError['newOwner'] == null){
                        deleteUseNft(hash).then((result) => {
                            openPopupSuccess();
                            if (choice === "use"){
                                $('div.success-p').children('p').eq(1)
                                    .html("Questo NFT è stato attivato.");
                            }
                            else {
                                $('div.success-p').children('p').eq(1)
                                    .html("Questo NFT è stato venduto.");
                            }
                            flag = true
                        }).catch((err) => {
                            flag = false;
                            openPopupError()
                            $('div.error-p').children('p').eq(1)
                                .html("Qualcosa è andato storto.<br>Riprovare.");
                        });
                    }
                    else {
                        updateUseNft(hash, data.msgError['newOwner']).then((result) => {
                            openPopupSuccess();
                            if (choice === "use"){
                                $('div.success-p').children('p').eq(1)
                                    .html("Questo NFT è stato attivato.");
                            }
                            else {
                                $('div.success-p').children('p').eq(1)
                                    .html("Questo NFT è stato venduto.");
                            }
                            flag = true
                        }).catch((err) => {
                            flag = false;
                            openPopupError()
                            $('div.error-p').children('p').eq(1)
                                .html("Qualcosa è andato storto.<br>Riprovare.");
                        });
                    }
                }).catch((err) => {
                    flag = false;
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Qualcosa è andato storto.<br>Riprovare.");
                });
            }
        }
    });
}

function ajaxTransferToUserNft(addr){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/assign-nft",
        data:{
            id: getRequestParam("id"),
            address: addr
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['errorTransfer'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Impossibile assegnare questo NFT." +
                        "<br>Non è stato ancora acquistato.");
            }
            else {
                const hash = data.msgError['hash'];
                const PINATA_API_KEY = data.msgError['pinata_key'];
                const PINATA_API_SECRET = data.msgError['pinata_secret'];
                pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                pinata.testAuthentication().then((result) => {
                    updateNft(hash, true).then((result) => {
                        openPopupSuccess();
                        $('div.success-p').children('p').eq(1)
                            .html("Trasferimento completato.");
                        flag = true
                    }).catch((err) => {
                        console.log(err)
                        flag = false;
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }).catch((err) => {
                    flag = false;
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Qualcosa è andato storto.<br>Riprovare.");
                });
            }
        }
    });
}

function ajaxBuyNft(addr){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/buy-nft",
        data:{
            id: getRequestParam("id"),
            address: addr
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['shopClosed'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Il negozio NFT è chiuso." +
                        "<br>Attendi che un Admin lo apri.");
            }
            else if (data.msgError['notEnoughMoney'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("KryptoToken insufficienti:" +
                        "<br>acquistane altri per comprare questo NFT.");
            }
            else if (data.msgError['alreadyBought'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Acquisto fallito." +
                        "<br>Hai già comprato questo NFT.");
            }
            else if (data.msgError['saleError'] != null){
                openPopupError()
                let $divError = $('div.error-p')
                $divError.children('div').eq(0).remove()
                $divError.children('p').eq(1)
                    .html("Acquisto fallito." +
                        "<br>Non puoi avere più del 50% di sconto <br>" +
                        "su questa categoria.");
            }
            else {
                const hash = data.msgError['hash'];
                const owner = data.msgError['owner'];
                const PINATA_API_KEY = data.msgError['pinata_key'];
                const PINATA_API_SECRET = data.msgError['pinata_secret'];
                pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                pinata.testAuthentication().then((result) => {
                    updateNft(hash, owner).then((result) => {
                        openPopupSuccess();
                        $('div.success-p').children('p').eq(1)
                            .html("Acquisto completato.<br> " +
                                "A breve verrà mostrato nel tuo profilo.");
                        flag = true
                    }).catch((err) => {
                        console.log(err)
                        flag = false;
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }).catch((err) => {
                    flag = false;
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Qualcosa è andato storto.<br>Riprovare.");
                });
            }
        }
    });
}

async function updateNft(hash, field){
    if (field === true){
        const metadata = {
            keyvalues: {
                sold: "true"
            }
        };
        await pinata.hashMetadata(hash, metadata);
    }
    else {
        const metadata = {
            keyvalues: {
                owner: field
            }
        };
        await pinata.hashMetadata(hash, metadata);
    }
}

async function updateUseNft(hash, field){
    const metadata = {
        keyvalues: {
            owner: field,
            sold: "false"
        }
    };
    await pinata.hashMetadata(hash, metadata);
}

async function deleteUseNft(hash){
    await pinata.hashMetadata(hash);
}

function ajaxDeleteNft(addr){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/delete-nft",
        data:{
            id: getRequestParam("id"),
            address: addr
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['noNft'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Il seguente account non ha questo NFT." +
                        "<br>Riprovare.");
            }
            else if (data.msgError['bought'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Eliminazione fallita: questo NFT" +
                        "<br>è stato già acquistato da un utente.");
            }
            else if (data.msgError['errorBurn'] != null){
                flag = false;
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            }
            else {
                const PINATA_API_KEY = data.msgError['pinata_key'];
                const PINATA_API_SECRET = data.msgError['pinata_secret'];
                pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                pinata.testAuthentication().then((result) => {
                    deleteNft(data.msgError['hash'])
                }).catch((err) => {
                    flag = false;
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Qualcosa è andato storto.<br>Riprovare.");
                });
            }
        }
    });
}

function deleteNft(ipfsPinHash){
    pinata.unpin(ipfsPinHash).then((result) => {
        openPopupSuccess()
        $('div.success-p').children('p').eq(1)
            .html("Eliminazione completato.");
        flag = true;
    }).catch((err) => {
        flag = false;
        openPopupError()
        $('div.error-p').children('p').eq(1)
            .html("Qualcosa è andato storto.<br>Riprovare.");
    });
}

function getRequestParam(name){
    if(name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}

function sendAddressToBackend() {
    if (typeof window.ethereum === 'undefined') {
        openPopupErrorMetamask();
        return "openPopup";
    }
    else if (!activeMetaMask())
        return "noAccount"
    return "ok"
}

async function activeMetaMask() {
    const isLocked = !(await ethereum._metamask.isUnlocked());
    if (isLocked) {
        $("#popupSuccess").fadeOut();
        openPopupErrorMetamask()
        $('div.error-p').children('p').eq(1)
            .html("Nessun account rilevato. <br>Accedere a Metamask.");

        $("input[name='userAddress']").val('')
        return false
    }
    else {
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' }).catch((e) => {
            return false
        })
        if (!accounts) {
            $("#popupSuccess").fadeOut();
            openPopupErrorMetamask()
            $('div.error-p').children('p').eq(1)
                .html("Nessun account rilevato. <br>Accedere a Metamask.");

            $("input[name='userAddress']").val('')
            return false
        }

        window.userWalletAddress = accounts[0]
        const addressField = document.querySelector("input[name='userAddress']")
        addressField.value = window.userWalletAddress
        return true
    }
}

function openPopupError(){
    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
    $("html, body").animate({scrollTop: 0}, 700);
}

function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
}

function openPopupSuccess(){
    $('div.success-p p:nth-child(2)')
        .html("Eliminazione completata <br>con successo.");

    $(".shadow").css("display","block");
    $("#popupSuccess").css("display","block");
    $("html, body").animate({scrollTop: 0}, 700);
}

function closePopupSuccess(){
    $(".shadow").fadeOut();
    $("#popupSuccess").fadeOut();
}

function openPopupErrorMetamask(){
    $('div.error-p').children('p').eq(1)
        .html("Metamask non è collegato.<br>" +
            "Oppure l'estensione non è presente.");

    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
    $("html, body").animate({scrollTop: 0}, 700);
}