const pinataSDK = require('@pinata/sdk');
var pinata;

window.userWalletAddress = null

$(document).ready(function (){
    $("input[name='validUntil']").val(moment().format('YYYY-MM-DD'))
    sendAddressToBackend();

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
    })

    ajaxShowNftCreatedOnPinata();

    $(".shadow").click(function () {
        closePopupError()
        closePopupSuccess()
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
    });

    $('#confirmPopupSuccess').click(function (){
        closePopupSuccess()
    })

    $('a.category').click(function (){
        var copyText = $(this);

        copyText.select();
        copyText.setSelectionRange(0, 99999);
        navigator.clipboard.writeText(copyText.text());
    })

    $.validator.addMethod("categoryName", function (value){
        let $categories = $('#categories')
            $categories.empty();

        if (value.toLowerCase() === "accessori" || value.toLowerCase() === "corsi"
            || value.toLowerCase() === "documentazione" || value.toLowerCase() === "incontri") {
            $categories.text("Categoria (Accessori - Corsi - Documentazione - Incontri)");
            return true;
        } return false;
    }, "Categoria non trovata.");

    $.validator.addMethod("checkDate", function(value) {
        return moment(value, "YYYY-MM-DD", true).isValid();
    },'Scadenza non valida.');

    $.validator.addMethod("sameOrAfter", function(value) {
        return moment(value, "YYYY-MM-DD", true).isSameOrAfter(moment());
    },'Scadenza non valida.');

    $.validator.addMethod("ext", function(value) {
        const ext = value.substring(value.length - 4);
        const jpeg = value.substring(value.length - 5);
        return ext.localeCompare(".png") === 0 || ext.localeCompare(".jpg") === 0 ||
            ext.localeCompare(".svg") === 0 || jpeg.localeCompare(".jpeg") === 0
    },'Path immagine non valido.');


    $("form[name='create-nft']").validate({
        rules: {
            description:{
                required: true,
                maxlength: 2001
            },
            name: {
                required: true,
            },
            category: {
                required: true,
                categoryName: true,
            },
            price: {
                required: true,
                digits: true,
                min: 1,
                max: 99999
            },
            validUntil: {
                required: true,
                checkDate: true,
                sameOrAfter: true
            },
            sale: {
                required: true,
                digits: true,
                min: 5,
                max: 50
            },
            image: {
                required: true,
                ext: true
            }
        },
        messages: {
            description: {
                required: "Descrizione non inserita.",
                maxlength: "Descrizione massima di 2000 caratteri."
            },
            name: {
                required: "Nome non inserito."
            },
            category: {
                required: "Categoria non inserita."
            },
            price: {
                required: "Prezzo non inserito.",
                digits: "Prezzo non valido.",
                min: "Prezzo non valido.",
                max: "Prezzo non valido."
            },
            validUntil: {
                required: "Scadenza non inserita.",
            },
            sale: {
                required: "Sconto non inserito.",
                digits: "Sconto non valido: solo numeri.",
                min: "Sconto minimo 5%.",
                max: "Sconto massimo 50%."
            },
            image: {
                required: "Path immagine non inserito.",
            }
        },
        submitHandler: function(form) {
            if (sendAddressToBackend().localeCompare("ok") === 0){
                const $userAddress = $("input[name='userAddress']")

                if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun account rilevato. <br>Accedere a Metamask.");
                } else
                    ajaxCreateNft($userAddress);
            }
            return false;
        }
    });
})

function ajaxShowNftCreatedOnPinata(){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/pinata/create-nft",
        dataType: 'json',
        success: function (data) {
            const PINATA_API_KEY = data.msgError['pinata_key'];
            const PINATA_API_SECRET = data.msgError['pinata_secret'];
            pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

            pinata.testAuthentication().then((result) => {
                getMyCreatedNfts(data.msgError['adminAddress']).then((nfts) => {
                    appendNftPinata(nfts);
                })
            }).catch((err) => {
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            });
        }
    });
}

function ajaxCreateNft($userAddress){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/create-nft",
        data: {
            name: $("input[name='name']").val(),
            category: $("input[name='category']").val(),
            price: $("input[name='price']").val(),
            validUntil: $("input[name='validUntil']").val(),
            sale: $("input[name='sale']").val(),
            description: $("#description").val(),
            addressMetamask: $userAddress.val()
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else {
                var flag = false
                if (data.msgError['name'] === "error") {
                    flag = true
                    let $name = $('#name-error');
                    if ($name.length !== 0) $name.remove()

                    $('input[name="name"]').after(
                        '<label id="name-error" class="error" for="name">Nome non valido.</label>'
                    )
                }

                if (data.msgError['category'] === "error") {
                    flag = true
                    let $category = $('#category-error');
                    if ($category.length !== 0) $category.remove()

                    $('input[name="category"]').after(
                        '<label id="category-error" class="error" for="category">Categoria non trovata.</label>'
                    )
                }

                if (data.msgError['price'] === "error") {
                    flag = true
                    let $price = $('#price-error');
                    if ($price.length !== 0) $price.remove()

                    $('input[name="price"]').after(
                        '<label id="price-error" class="error" for="price">Prezzo non valido.</label>'
                    )
                }

                if (data.msgError['validUntil'] === "error") {
                    flag = true
                    let $validUntil = $('#price-error');
                    if ($validUntil.length !== 0) $validUntil.remove()

                    $('input[name="validUntil"]').after(
                        '<label id="validUntil-error" class="error" for="validUntil">Scadenza non valida.</label>'
                    )
                }

                if (data.msgError['sale'] === "error") {
                    flag = true
                    let $sale = $('#sale-error');
                    if ($sale.length !== 0) $sale.remove()

                    $('input[name="sale"]').after(
                        '<label id="sale-error" class="error" for="sale">Sconto non valido.</label>'
                    )
                }
                if (data.msgError['description'] === "error") {
                    flag = true
                    let $description = $('#description-error');
                    if ($description.length !== 0) $description.remove()

                    $('input[name="description"]').after(
                        '<label id="description-error" class="error" for="description">Descrizione non inserita.</label>'
                    )
                }
                if (data.msgError['errorMint'] === "error"){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Impossibile effettuare il <i>mint</i> sulla Blockchain.<br>Riprovare.");
                    return
                }
                if (data.msgError['ok'] === "ok") {
                    const PINATA_API_KEY = data.msgError['pinata_key'];
                    const PINATA_API_SECRET = data.msgError['pinata_secret'];
                    pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                    pinata.testAuthentication().then((result) => {
                        getMyCreatedNfts(data.msgError['seller']).then((nfts) => {
                            if (nfts.length !== 0) {
                                for(let i = 0;  i < nfts.length; i++) {
                                    if (nfts[i].metadata.name === data.msgError['name']) {
                                        uploadNft(nfts[i].ipfs_pin_hash, data);
                                        ajaxSaveOnBlockchain(data, nfts[i].ipfs_pin_hash);
                                        nfts.splice(i, 1);
                                        appendNftPinata(nfts);
                                        cleanInput();
                                        return;
                                    }
                                }
                            }
                            openPopupError()
                            $('div.error-p').children('p').eq(1)
                                .html("NFT non creato su Pinata, oppure nome errato.<br>Riprovare.");
                        })
                    }).catch((err) => {
                        console.log(err)
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }
                else if (flag){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Qualcosa è andato storto.<br>Riprovare.");
                }
            }
        },
        error: function (e) {
            console.log(e)
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Qualcosa è andato storto.<br>Riprovare.");
        }
    });
}

function ajaxSaveOnBlockchain(data, url){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/save-nft",
        data: {
            name: data.msgError['name'],
            category: data.msgError['category'],
            description: data.msgError['description'],
            url: url,
            price: data.msgError['price'],
            validUntil: data.msgError['validUntil'],
            sale: data.msgError['sale'],
            address: data.msgError['seller']
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['invalidUser'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
        },
        error: function (e) {
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Qualcosa è andato storto.<br>Riprovare.");
        }
    });
}

async function getMyCreatedNfts(address){
    const metadataFilter = {
        keyvalues: {
            seller: {
                value: address,
                op: 'iLike'
            },
            created: {
                value: "1",
                op: 'eq'
            }
        }
    };

    const filters = {
        status : "pinned",
        pageLimit : 1000,
        metadata : metadataFilter
    };

    const pin = await pinata.pinList(filters);
    return pin.rows;
}

function uploadNft(cid, data){
    const metadata = {
        name: data.msgError['name'].toString(),
        keyvalues: {
            category: data.msgError['category'].toString(),
            owner: data.msgError['owner'].toString(),
            price: data.msgError['price'].toString(),
            validUntil: data.msgError['validUntil'].toString(),
            description: data.msgError['description'].toString(),
            sale: data.msgError['sale'].toString(),
            sold: "false",
            created: null
        }
    };

   pinata.hashMetadata(cid, metadata).then((result) => {
        openPopupSuccess()
        $('div.success-p').children('p').eq(1)
            .html("Il tuo NFT è stato creato <br> con successo.");
    }).catch((err) => {
        openPopupError()
        $('div.error-p').children('p').eq(1)
            .html("Qualcosa è andato storto.<br>Riprovare.");
    });
}

function appendNftPinata(nfts){
    var $category_list = $('.categories-list');
    $category_list.empty();

    if (nfts.length !== 0) {
        for (let i = 0; i < nfts.length; i++) {
            $category_list.append(
                '<li><a class="category">' + nfts[i].metadata.name + '</a></li>'
            )
        }
    } else {
        $category_list.append(
            '<li><a class="category">Nessun NFT disponibile</a></li>'
        )
    }
}

function cleanInput(){
    $('input[name="name"]').val('')
    $('input[name="category"]').val('')
    $('input[name="price"]').val('')
    $("input[name='validUntil']").val(moment().format('YYYY-MM-DD'))
    $('input[name="sale"]').val('')
    $('#description').val('')
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