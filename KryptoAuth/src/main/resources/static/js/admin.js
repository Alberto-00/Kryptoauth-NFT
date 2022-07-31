
var globalAddress, globalStatus, globalRole, operation

$(document).ready(function (){
    $(".shadow").click(function () {
        closePopupError()
        closePopupSuccess()
        closePopupPrivateKey()
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
        closePopupPrivateKey()
    });

    $('#confirmPopupSuccess').click(function () {
        closePopupSuccess()
    });

    $('#confirmPopupSuccessPrivateKey').on('click', function (){
        const $privateKey = $('input[name="privateKey"]')

        if (($privateKey.val() != null && $privateKey.val() !== '') &&
            $privateKey.val().length > 63 && $privateKey.val().length < 65) {
            closePopupPrivateKey()
            if (operation !== undefined && operation === "disactive")
                ajaxDisactiveAddress(globalAddress, globalStatus, globalRole, $privateKey.val())
            else
                ajaxActiveAddress(globalAddress, globalStatus, globalRole, $privateKey.val())
        } else {
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Chiave privata non corretta. <br>Riprovare.");
        }
    })

    $('#closePopupRevokeError').on('click', function (){
        closePopupErrorRevokeRole()
    })

    $('#revokeRole').on('click', function (){
        if (operation !== undefined && operation === "disactive")
            ajaxRevokeAllRoles(globalAddress)
        else
            ajaxRevokeAdmin(globalAddress)
    })
})

function activeAddress(address, status, role){
    globalStatus = status
    globalAddress = address
    globalRole = role
    operation = undefined

    ajaxActiveAddress(globalAddress, globalStatus, globalRole, $('input[name="privateKey"]').val())
}

function disactiveAddress(address, status, role){
    globalStatus = status
    globalAddress = address
    globalRole = role
    operation = "disactive"

    ajaxDisactiveAddress(globalAddress, globalStatus, globalRole, $('input[name="privateKey"]').val())
}

function ajaxActiveAddress(address, status, role, privateKey){
    const $statusActive = $('#active' + role)
    const $statusNotActive = $('#notActive' + role)

    if ($statusNotActive.length && (!$statusActive.length || $statusActive.val().localeCompare("Attivo") !== 0)){
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
                privateKey: privateKey
            },
            dataType: 'json',
            success: function (data) {
                if (data.msgError['sessionEmpty'] != null) {
                    window.location.href = "/kryptoauth/404"
                }

                if (data.msgError['contract'] != null){
                    openPopupPrivateKey()
                }

                if (data.msgError['privateKey'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Chiave privata non corretta. <br>Riprovare.");
                }

                if (data.msgError['notEqualsAddress'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Chiave privata non associata a questo account.<br>Riprovare.");
                }

                if (data.msgError['revokeAdmin'] != null){
                    openPopupErrorRevokeRole()
                }

                if (data.msgError['notRevoke'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Solo l'account proprietario può rinunciare al suo ruolo di <i>Admin</i>.");
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
    } else {
        openPopupError()
        $('div.error-p').children('p').eq(1)
            .html("Account già attivato.");
    }
}

function ajaxDisactiveAddress(address, status, role, privateKey){
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
                privateKey: privateKey
            },
            dataType: 'json',
            success: function (data) {
                if (data.msgError['notEqualsAddress'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Chiave privata non associata a questo account.<br>Riprovare.");
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

                if (data.msgError['contract'] != null){
                    openPopupPrivateKey()
                }

                if (data.msgError['privateKey'] != null){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Chiave privata non corretta. <br>Riprovare.");
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

function openPopupPrivateKey(){
    $(".shadow").css("display","block");
    $("#popupSuccessPrivateKey").css("display","block");
}

function closePopupPrivateKey(){
    $(".shadow").fadeOut();
    $("#popupSuccessPrivateKey").fadeOut();
}

function openPopupErrorRevokeRole(){
    $(".shadow").css("display","block");
    $("#popupErrorRevokeRole").css("display","block");
}

function closePopupErrorRevokeRole(){
    $(".shadow").fadeOut();
    $("#popupErrorRevokeRole").fadeOut();
}