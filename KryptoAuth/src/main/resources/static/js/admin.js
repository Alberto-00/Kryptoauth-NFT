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
        if (operation === "disactive")
            ajaxRevokeAllRoles(globalAddress)

        else if (operation === "user")
            ajaxRevokeAdmin(globalAddress)
    })
})

function activeAddress(address, status, role){
    globalStatus = status
    globalAddress = address
    globalRole = role
    operation = 'user'

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
}

function disactiveAddress(address, status, role){
    globalStatus = status
    globalAddress = address
    globalRole = role
    operation = "disactive"

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

}

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
                window.location.href = "/kryptoauth/404"
            }

            if (data.msgError['notEqualsAddress'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Hai cambiato account in Metamask." +
                        "<br>Seleziona quello corretto.");
            }

            if (data.msgError['revokeAdmin'] != null){
                openPopupErrorRevokeRole()
            }

            if (data.msgError['notRevoke'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Solo l'account proprietario può rinunciare al suo ruolo di <i>Admin</i>.");
            }

            if (data.msgError['alreadyActive'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Account già attivato.");
            }

            if (data.msgError['success'] != null){
                openPopupSuccess()

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

                if (data.msgError['errorPage'] != null) {
                    window.location.href = "/kryptoauth/404"
                }

                if (data.msgError['notEqualsAddress'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Hai cambiato account in Metamask." +
                            "<br>Seleziona quello corretto.");
                }

                if (data.msgError['revoke'] != null){
                    openPopupErrorRevokeRole()
                }

                if (data.msgError['notRevoke'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Non sei l'account proprietario.<br>Non puoi disattivarlo.");
                }

                if (data.msgError['success'] != null){
                    const datas = data.msgError['success'].split(",")
                    const $radios = $('input:radio[name="user"]')
                    openPopupSuccess()

                    if (datas[0] === "Non Attivo"){
                        const $active = $('#active' + role)
                        if ($active.length){
                            $active.attr('class', 'not-active-reg')
                            $active.attr('id', 'notActive' + role)
                            $active.html("Non Attivo")
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
    } else {
        openPopupError()
        $('div.error-p').children('p').eq(1)
            .html("Account già disattivato.");
    }
}

function ajaxRevokeAllRoles(address){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/revokeRoles",
        data: {
            address: address,
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['redirect'] != null) {
                window.location.href = "/kryptoauth/logout"
            }

            if (data.msgError['error'] != null){
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

function ajaxRevokeAdmin(address){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/revokeAdmin",
        data: {
            address: address,
        },
        dataType: 'json',
        success: function (data) {
            if (data.msgError['redirect'] != null) {
                window.location.href = "/kryptoauth/logout"
            }

            if (data.msgError['error'] != null){
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

function openPopupError(){
    $(".shadow").fadeIn();
    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
}

function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
}

function openPopupSuccess(){
    $('#popupSuccess').children('div').eq(1)
        .children('p').eq(1).html("Salvataggio effettuato." +
        "<div class='icon-box-success'>" +
        "   <img src='/img/icons/check-solid.svg' alt='check'>" +
        "</div>");
    $(".shadow").css("display","block");
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