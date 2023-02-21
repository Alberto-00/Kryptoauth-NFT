const pinataSDK = require("@pinata/sdk");
var pinata;
var flag = true;

$(document).ready(function (){
    $(".hide").slice(0, 3).show();

    otherNftsToLoad()

    $("#loadMore").on("click", function(e){
        e.preventDefault();
        $(".hide:hidden").slice(0, 2).slideDown();
        otherNftsToLoad()
    });

    sendAddressToBackend();

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
    })

    ajaxGetNftByCategory("all", "start")

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

    $('#buyFt').click(function (){
        flag = true
    })

    $('#burnFt').click(function (){
        flag = false
    })

    $('a.category').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxGetNftByCategory($(this), $userAddress.val())
            }
        }
    })

    $('#burnNftExpired').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxBurnExpired($userAddress.val())
            }
        }
    })

    $("form[name='buy-ft']").validate({
        rules: {
            token:{
                required: true,
                min: 1
            }
        },
        messages: {
            token: {
                required: "Token da acquistare / vendere non selezionati",
                min: "Token da acquistare / vendere: quantità minima 1"
            },
        },
        submitHandler: function(form) {
            if (sendAddressToBackend().localeCompare("ok") === 0){
                const $userAddress = $("input[name='userAddress']")

                if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun account rilevato. <br>Accedere a Metamask.");
                } else{
                    if (flag) ajaxBuyFt($userAddress.val(), $("input[name='token']").val())
                    else ajaxSellFt($userAddress.val(), $("input[name='token']").val())
                }
            }
            return false;
        }
    });
})

function ajaxBuyFt(address, token){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/buy-ft",
        data: {
            address: address,
            token: token
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
            else if (data.msgError['tokenError'] != null){
                let $token = $('#token-error');
                if ($token.length !== 0) $token.remove()

                $('input[name="token"]').after(
                    '<label id="token-error" class="error" for="token">Token da acquistare: quantità minima 1</label>'
                )
            }
            else {
                let token = data.msgError['token'];
                $('span.token').text(token + " KT");
                openPopupSuccess();
                $('div.success-p').children('p').eq(1)
                    .html("Acquisto completato.<br> " +
                        "Adesso possiedi " + token + " KT.");
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

function ajaxSellFt(address, token){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/sell-ft",
        data: {
            address: address,
            token: token
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
            else if (data.msgError['tokenError'] != null){
                let $token = $('#token-error');
                if ($token.length !== 0) $token.remove()

                $('input[name="token"]').after(
                    '<label id="token-error" class="error" for="token">Token da acquistare / vendere: quantità minima 1</label>'
                )
            }
            else if (data.msgError['notToken'] != null){
                let $token = $('#token-error');
                if ($token.length !== 0) $token.remove()

                $('input[name="token"]').after(
                    '<label id="token-error" class="error" for="token">Token da acquistare / vendere: non possiedi questi token</label>'
                )
            }
            else {
                let token = data.msgError['token'];
                $('span.token').text(token + " KT");
                openPopupSuccess();
                $('div.success-p').children('p').eq(1)
                    .html("Operazione completata.<br> " +
                        "Adesso possiedi " + token + " KT.");
            }
        }
    });
}

function ajaxGetNftByCategory($category, addrMetamask){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/profile",
        data: {
            address: addrMetamask
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
                var nftArr = JSON.parse(data.msgError['jsonData']);
                if ($category === "all" || $category.children(":first").text() === "Tutti NFT" ||
                    $category.text() === "Tutti NFT"){
                    if ($category === "all")
                        $('.allnfts').text(nftArr.length);
                    else
                        cssCategory($category, nftArr.length)

                    $('div.blog-post').remove()
                    if (nftArr.length !== 0){
                        for (let i = 0; i < nftArr.length; i++){
                            appendNft(nftArr, i, i)
                        }
                    }
                    otherNftsToLoad()
                }
                else {
                    $('div.blog-post').remove()
                    let count = 0;
                    if ($category.children(":first").text().toLowerCase() === "Da assegnare".toLowerCase()
                        || $category.text().toLowerCase() === "Da assegnare".toLowerCase()){

                        var jsonToAssign = JSON.parse(data.msgError['jsonToAssign']);
                        if (jsonToAssign.length !== 0){
                            for (let i = 0; i < jsonToAssign.length; i++) {
                                appendNft(jsonToAssign, i, count)
                                count++;
                            }
                        }
                    }
                    else{
                        if (nftArr.length !== 0){
                            for (let i = 0; i < nftArr.length; i++) {
                                if (nftArr[i].category.toLowerCase() === $category.children(":first").text().toLowerCase()
                                    || nftArr[i].category.toLowerCase() === $category.text().toLowerCase()){
                                    appendNft(nftArr, i, count);
                                    count++;
                                }
                            }
                        }
                    }
                    otherNftsToLoad()
                    cssCategory($category, count);
                }
            }
        },
        error: function (e) {
            console.log(e);
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Qualcosa è andato storto.<br>Riprovare.");
        }
    });
}

function ajaxBurnExpired(address){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/delete-nft-expired",
        data: {
            addressMetamask: address
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
                var nftArr = JSON.parse(data.msgError['jsonData']);
                const burned = data.msgError['burned'].split(",");

                if (burned[0] !== ""){
                    const PINATA_API_KEY = data.msgError['pinata_key'];
                    const PINATA_API_SECRET = data.msgError['pinata_secret'];
                    pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                    pinata.testAuthentication().then((result) => {
                        for (let i = 0; i < burned.length - 1; i++){
                            deleteNft(burned[i]);
                        }
                        openPopupSuccess()
                        $('div.success-p p:nth-child(2)')
                            .html("Gli NFT scaduti sono stati <br> eliminati con successo.");

                        cssCategory($('ul.categories-list li:nth-child(1) a'), nftArr.length)
                        $('div.blog-post').remove()
                        if (nftArr.length !== 0){
                            for (let i = 0; i < nftArr.length; i++)
                                appendNft(nftArr, i, i)
                        }
                        otherNftsToLoad()
                    }).catch((err) => {
                        console.log(err)
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }
                else {
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun NFT scaduto.");
                }
            }
        },
        error: function (e) {
            console.log(e);
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Qualcosa è andato storto.<br>Riprovare.");
        }
    });
}

function deleteNft(ipfsPinHash){
    pinata.unpin(ipfsPinHash).then((result) => {
    }).catch((err) => {});
}

function appendNft(pin, i, count) {
    let hide, description
    if (count < 3) hide = 'style="display: block;"'
    else hide = ""

    if ((pin[i].description).length > 144)
        description = (pin[i].description).substring(0, 144) + "...";
    else
        description = pin[i].description;

    $('div.cta-wrapper').before(
        '<div class="blog-post hide" ' + hide + '>' +
        '<a href="/kryptoauth/marketplace/info-nft?id=' + pin[i].tokenId + '">' +
        <!-- Featured image -->
        '<div class="featured-image backgroundImage">' +
        '<img src="' + pin[i].url + '" ' + 'alt="">' +
        '</div>' +
        <!-- Content -->
        '<div class="content">' +
        '<div class="post-title">' + pin[i].name +
        '<span class="blog-date">' + pin[i].validUntil +'</span>' +
        '</div>' +
        '<p>' + description + '</p>' +
        '</div>' +
        '</a>' +
        '</div>'
    )
}

function cssCategory($category, size){
    $('a.is-active').removeClass('is-active');
    $('a.category').each(function () {
        if ($(this).children(":first").length){
            const text = $(this).children(":first").text();
            $(this).empty().text(text);
        }
        else{
            const text = $(this).text();
            $(this).empty().text(text);
        }
    })

    $category.addClass('is-active');
    const text = $category.text();
    $category.empty();
    $category.append(
        '<span>' + text + '</span>' +
        '<span class="tag">' + size + '</span>'
    )
}

function otherNftsToLoad(){
    let $blogPost = $("div.blog-post")

    if ($blogPost.length > 0){
        $blogPost.each(function( index ) {
            if ($(this).css('display') === 'none') {
                $("#loadMore").text("Carica Altro");
                return false
            }
            else
                $("#loadMore").text("Nessun Contenuto");
        });
    }
    else
        $("#loadMore").text("Nessun Contenuto");
}

function openPopupError(){
    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
}

function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
}

function openPopupSuccess(){
    $(".shadow").css("display","block");
    $("#popupSuccess").css("display","block");
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