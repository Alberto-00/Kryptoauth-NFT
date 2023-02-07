window.userWalletAddress = null

$(document).ready(function (){
    sendAddressToBackend();

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
    })

    ajaxGetNftByCategory("all", "start")

    $(".shadow").click(function () {
        closePopupError()

        if ($('#true').length) {
            closePopupError()
            window.location.href = "/kryptoauth"
        }
    });

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
})

function ajaxGetNftByCategory($category, addrMetamask){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/marketplace-user",
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
                        for (let i = 0; i < nftArr.length; i++){
                            appendNft(nftArr, i);
                        }
                    }
                }
                else {
                    $('div.blog-post').remove()
                    let count = 0;
                    if (nftArr.length !== 0) {
                        for (let i = 0; i < nftArr.length; i++) {
                            if (nftArr[i].seller.toLowerCase() === data.msgError['adminAddress'].toLowerCase()
                                && (nftArr[i].category.toLowerCase() === $category.children(":first").text().toLowerCase()
                                    || nftArr[i].category.toLowerCase() === $category.text().toLowerCase())){
                                appendNft(nftArr, i);
                                count++;
                            }
                        }
                    }
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

function appendNft(pin, i) {
    let hide, description
    if (i > 2) hide = "hide"
    else hide = ""

    if ((pin[i].description).length > 144)
        description = (pin[i].description).substring(0, 144);
    else
        description = pin[i].description + "...";

    $('div.cta-wrapper').before(
        '<div class="blog-post '+ hide +'">' +
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

function openPopupError(){
    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
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