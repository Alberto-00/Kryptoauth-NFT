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
        if (flag) window.location.href = "/kryptoauth/marketplace"
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
    });

    $('#confirmPopupSuccess').click(function (){
        closePopupSuccess()
        window.location.href = "/kryptoauth/marketplace"
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
})

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