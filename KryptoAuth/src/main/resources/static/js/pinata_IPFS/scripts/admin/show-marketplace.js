const pinataSDK = require('@pinata/sdk');
var pinata
window.userWalletAddress = null

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

        if ($('#true').length) {
            closePopupError()
            window.location.href = "/kryptoauth"
        }
    });

    $('#confirmPopupSuccess').click(function () {
        closePopupSuccess()
    })

    $('#confirmPopupError').click(function () {
        closePopupError()
        if ($('#true').length) {
            closePopupError()
            window.location.href = "/kryptoauth"
        }
    });

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
                ajaxBurnNftExpired($userAddress.val())
            }
        }
    })

    $('#openMarketplace').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxoFlipMarketplace($userAddress.val(), true);
            }
        }
    })

    $('#closeMarketplace').click(function (){
        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxoFlipMarketplace($userAddress.val(), false)
            }
        }
    })
})

function ajaxoFlipMarketplace(userAddress, flag){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/open-marketplace",
        data: {
            addressMetamask: userAddress,
            flag: flag
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
            else if (data.msgError['error'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            }
            else if (data.msgError['flag'] != null){
                let flag = "Negozio chiuso";

                if (data.msgError['flag'] === "true")
                    flag = "Negozio aperto";

                $('#flipMarketplace').fadeIn(800).html(flag)
                    .delay(1000).fadeOut(800)
            }
        },
        error: function (e) {
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Qualcosa è andato storto.<br>Riprovare.");

            if ($('#false').length)
                $('#false').attr("id", "true");
        }
    });
}

function ajaxGetNftByCategory($category, addrMetamask){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace",
        data: {
            addressMetamask: addrMetamask
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
                        for (let i = 0; i < nftArr.length; i++)
                            appendNft(nftArr, i, i);
                    }
                    otherNftsToLoad()
                }
                else {
                    $('div.blog-post').remove()
                    let count = 0;
                    if (nftArr.length !== 0) {
                        if ($category.children(":first").text().toLowerCase() === "Da assegnare".toLowerCase()
                            || $category.text().toLowerCase() === "Da assegnare".toLowerCase()){
                            for (let i = 0; i < nftArr.length; i++) {
                                if (nftArr[i].seller.toLowerCase() !== nftArr[i].owner.toLowerCase()
                                    && nftArr[i].sold === "false"){
                                    appendNft(nftArr, i, count);
                                    count++;
                                }
                            }
                        }
                        else {
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

            if ($('#false').length)
                $('#false').attr("id", "true");
        }
    });
}

function ajaxBurnNftExpired(userAddress){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace/delete-nft-expired",
        data: {
            addressMetamask: userAddress
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
                                appendNft(nftArr, i, i);
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

            if ($('#false').length)
                $('#false').attr("id", "true");
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

function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
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