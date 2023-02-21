const pinataSDK = require('@pinata/sdk');
var pinata;
var globalAddress, globalStatus, globalRole, operation
window.userWalletAddress = null

$(document).ready(function (){
    sendAddressToBackend();

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
    })

    $(".shadow").click(function () {
        closePopupError()
        closePopupSuccess()
        closePopupErrorRevokeRole()
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
    });

    $('#confirmPopupSuccess').click(function () {
        closePopupSuccess()
    });

    $('#closePopupRevokeError').on('click', function (){
        closePopupErrorRevokeRole()
    })

    $('#revokeRole').on('click', function (){
        if (operation === "disactiveAdmin")
            ajaxRevokeAdmin(globalAddress, $("input[name='userAddress']").val())

        else if (operation === "disactiveUser")
            ajaxRevokeUser(globalAddress, $("input[name='userAddress']").val())

        else if (operation === "adminToUser")
            ajaxAdminToUser(globalAddress, $("input[name='userAddress']").val())
    })

    $('a.activeAddress').click(function (){
        globalStatus = $(this).children('label').eq(1).text()
        globalAddress = $(this).children('label').eq(0).text()
        globalRole = $(this).children('label').eq(2).text()

        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxActiveAddress(globalAddress, globalStatus, globalRole, $userAddress.val())
            }
        }
    })

    $('a.disactiveAddress').click(function (){
        globalStatus = $(this).children('label').eq(1).text()
        globalAddress = $(this).children('label').eq(0).text()
        globalRole = $(this).children('label').eq(2).text()

        if (sendAddressToBackend().localeCompare("ok") === 0){
            const $userAddress = $("input[name='userAddress']")

            if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
            } else {
                ajaxDisactiveAddress(globalAddress, globalStatus, globalRole, $userAddress.val())
            }
        }
    })
})

function ajaxActiveAddress(address, status, role, addressMetamask){
    var $role
    if (role.localeCompare("User") === 0)
        $role = "User"
    else {
        const name = "input[name='role" + role + "']:checked"
        $role = $(name).val()
    }

    $.ajax({
        type: "POST",
        url: "/kryptoauth/activeAddress",
        data: {
            address: address,
            role: $role,
            status: status,
            addressMetamask: addressMetamask
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }

            else if (data.msgError['notEqualsAddress'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }

            else if (data.msgError['notRevoke'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Solo l'account proprietario può rinunciare al suo ruolo di <i>Admin</i>.");
            }

            else if (data.msgError['alreadyActive'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Account già attivato.");
            }

            else if (data.msgError['adminToUser'] != null){
                openPopupErrorRevokeRole()
                $('div.error-p p:nth-child(2)')
                    .html("Sei sicuro di voler rinunciare al ruolo di <i>Admin</i>?<br>" +
                        "Perderai tutti i tuoi NFT.");
                operation = "adminToUser"
            }
            else if (data.msgError['userHaveNftToAssigned'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("L'account non può essere disattivato." +
                        "<br>L'utente ha acquistato degli NFT.");
            }
            else if (data.msgError['userHaveNft'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("L'account non può essere disattivato." +
                        "<br>L'utente possiede degli NFT.");
            }
            else if (data.msgError['success'] != null){
                openPopupSuccess()
                $('div.success-p p:nth-child(2)').text("Salvataggio effettuato.")

                const datas = data.msgError['success'].split(",")
                const $radios = $('input:radio[name="user"]')

                if (datas[0] === "Attivo"){
                    const $notActive = $('#notActive' + role)
                    if ($notActive.length){
                        $notActive.attr('class', 'active-reg')
                        $notActive.attr('id', 'active' + role)
                        $notActive.html("Attivo")
                    }
                }
                if(!$radios.is(':checked')) {
                    $radios.filter('[value=' + datas[1] + ']').prop('checked', true);
                }
                $('#roles' + role).html(datas[1])
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}

function ajaxDisactiveAddress(address, status, role, addressMetamask){
    const $statusActive = $('#active' + role)
    const $statusNotActive = $('#notActive' + role)

    if (!$statusNotActive.length || ($statusActive.length && $statusActive.val().localeCompare("Attivo") === 0)){
        let $role
        if (role.localeCompare("User") === 0)
            $role = "User"
        else {
            const name = "input[name='role" + role + "']:checked"
            $role = $(name).val()
        }
        $.ajax({
            type: "POST",
            url: "/kryptoauth/disactiveAddress",
            data: {
                address: address,
                role: $role,
                status: status,
                addressMetamask: addressMetamask
            },
            dataType: 'json',
            success: function (data) {
                if (data.msgError['sessionEmpty'] != null) {
                    window.location.href = "/kryptoauth/500"
                }

                else if (data.msgError['errorPage'] != null) {
                    window.location.href = "/kryptoauth/404"
                }

                else if (data.msgError['notEqualsAddress'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Hai cambiato account in Metamask." +
                            "<br>Seleziona quello corretto.");
                }

                else if (data.msgError['revokeAdmin'] != null){
                    openPopupErrorRevokeRole()
                    $('div.error-p p:nth-child(2)')
                        .html("Sei sicuro di voler rinunciare al ruolo di <i>Admin</i>?<br>" +
                            "Perderai tutti i tuoi NFT.");

                    operation = "disactiveAdmin"
                }

                else if (data.msgError['notRevoke'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Non sei l'account proprietario.<br>Non puoi disattivarlo.");
                }

                else if (data.msgError['alreadyDisactived'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Account già disattivato.");
                }

                else if (data.msgError['disactiveUser'] != null){
                    openPopupErrorRevokeRole()
                    $('div.error-p p:nth-child(2)')
                        .html("Sei sicuro di voler disattivare <br> questo Utente?");
                    operation = "disactiveUser"
                }
            },
            error: function (e) {
                console.log(e)
            }
        });
    } else {
        openPopupError()
        $('div.error-p').children('p').eq(1)
            .html("Account già disattivato.");
    }
}

function ajaxAdminToUser(address, addrMeta){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/adminToUser",
        data: {
            address: address,
            addressMetamask: addrMeta,
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['redirect'] != null) {
                const burned = data.msgError['burned'].split(",");

                if (burned[0] !== ""){
                    const PINATA_API_KEY = data.msgError['pinata_key'];
                    const PINATA_API_SECRET = data.msgError['pinata_secret'];
                    pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                    pinata.testAuthentication().then((result) => {
                        openPopupSuccess()
                        $('div.success-p p:nth-child(1)').text("Attendi...")
                        $('div.success-p p:nth-child(2)')
                            .html("A breve verrai reindirizzato<br> all'HomePage")
                        $('#confirmPopupSuccess').remove()

                        deleteNft(burned).then((result) => {
                            window.location.href = "/kryptoauth/logout"
                        });
                    }).catch((err) => {
                        console.log(err)
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }
                else
                    window.location.href = "/kryptoauth/logout"
            }
            else if (data.msgError['nftToBeAssigned'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Impossibile disattivare l'account:<br>NFT <i>" +
                        data.msgError['nftToBeAssigned'] + "</i> da assegnare.");
            }
            else if (data.msgError['error'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}

function ajaxRevokeAdmin(address, addrMeta){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/revokeAdmin",
        data: {
            address: address,
            addressMetamask: addrMeta,
            role: globalStatus
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['redirect'] != null) {
                const burned = data.msgError['burned'].split(",");

                if (burned[0] !== ""){
                    const PINATA_API_KEY = data.msgError['pinata_key'];
                    const PINATA_API_SECRET = data.msgError['pinata_secret'];
                    pinata = new pinataSDK(PINATA_API_KEY, PINATA_API_SECRET);

                    pinata.testAuthentication().then((result) => {
                        openPopupSuccess()
                        $('div.success-p p:nth-child(1)').text("Attendi...")
                        $('div.success-p p:nth-child(2)')
                            .html("A breve verrai reindirizzato<br> all'HomePage")
                        $('#confirmPopupSuccess').remove()

                        deleteNft(burned).then((result) => {
                            window.location.href = "/kryptoauth/logout"
                        });
                    }).catch((err) => {
                        console.log(err)
                        openPopupError()
                        $('div.error-p').children('p').eq(1)
                            .html("Qualcosa è andato storto.<br>Riprovare.");
                    });
                }
                else
                    window.location.href = "/kryptoauth/logout"
            }
            else if (data.msgError['nftToBeAssigned'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Impossibile disattivare l'account:<br>NFT <i>" +
                        data.msgError['nftToBeAssigned'] + "</i> da assegnare.");
            }
            else if (data.msgError['notEqualsAddress'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['error'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}

function ajaxRevokeUser(address, addrMeta) {
    $.ajax({
        type: "POST",
        url: "/kryptoauth/revokeUser",
        data: {
            address: address,
            addressMetamask: addrMeta,
            role: globalRole
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/500"
            }
            else if (data.msgError['error'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Qualcosa è andato storto.<br>Riprovare.");
            }
            else if (data.msgError['notEqualsAddress'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }
            else if (data.msgError['userHaveNftToAssigned'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("L'account non può essere disattivato." +
                        "<br>L'utente ha acquistato degli NFT.");
            }
            else if (data.msgError['userHaveNft'] != null){
                closePopupErrorRevokeRole()
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("L'account non può essere disattivato." +
                        "<br>L'utente possiede degli NFT.");
            }
            else {
                closePopupErrorRevokeRole()
                const datas = data.msgError['success'].split(",")
                const $radios = $('input:radio[name="user"]')

                openPopupSuccess()
                $('div.success-p p:nth-child(2)').text("Salvataggio effettuato.")

                if (datas[0] === "Non Attivo"){
                    const $active = $('#active' + globalRole)
                    if ($active.length){
                        $active.attr('class', 'not-active-reg')
                        $active.attr('id', 'notActive' + globalRole)
                        $active.html("Non Attivo")
                    }
                }

                if(!$radios.is(':checked')) {
                    $radios.filter('[value=' + datas[1] + ']').prop('checked', true);
                }
                $('#roles' + globalRole).html(datas[1])
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}

async function deleteNft(ipfsPinHash){
    for (let i = 0; i < ipfsPinHash.length - 1; i++) {
        await pinata.unpin(ipfsPinHash[i]);
    }
}

function openPopupError(){
    $(".shadow").fadeIn().css("display", "block");
    $("#popupError").css("display", "block");
}

function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
}

function openPopupSuccess(){
    $(".shadow").fadeIn().css("display","block");
    $("#popupSuccess").css("display","block");
}

function closePopupSuccess(){
    $(".shadow").fadeOut();
    $("#popupSuccess").fadeOut();
}

function openPopupErrorRevokeRole(){
    $(".shadow").css("display","block");
    $("#popupErrorRevokeRole").css("display","block");
}

function closePopupErrorRevokeRole(){
    $(".shadow").fadeOut();
    $("#popupErrorRevokeRole").fadeOut();
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