import {closePopupError, closePopupSuccess, openPopupError, openPopupSuccess, openPopupPrivateKey, closePopupPrivateKey} from './popup.js'
import {sendAddressToBackend} from './metamask.js'

window.timer = function(event){
    const now = new Date();
    const start = new Date(now.getFullYear(), 0, 0);
    const diff = (now - start) + ((start.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000);
    const oneDay = 1000 * 60 * 60 * 24;
    const day = Math.floor(diff / oneDay);

    document.getElementById('days-count').innerHTML = day + '';

    if (now.getHours() >= 0 && now.getHours() < 10)
        document.getElementById('hours-count').innerHTML = '0' + now.getHours();
    else
        document.getElementById('hours-count').innerHTML = now.getHours() + '';

    document.getElementById('minutes-count').innerHTML = now.getMinutes() + '';

    if (now.getSeconds() >= 0 && now.getSeconds() < 10)
        document.getElementById('seconds-count').innerHTML = '0' + now.getSeconds();
    else
        document.getElementById('seconds-count').innerHTML = now.getSeconds() + '';

    setTimeout('timer()', 1000);
}
timer()

var roleAddress = ""

$(document).ready(function (){
    sendAddressToBackend()

    ethereum.on('accountsChanged', function (accounts) {
        sendAddressToBackend();
        $('input[name="privateKey"]').val(undefined)
    })

    $.validator.addMethod("username_email", function (value){
        return /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,}[a-zA-Z0-9]$/.test(value) | /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value);
    }, "Email o Username errati.");

    $.validator.addMethod("username_email_lenght", function (value){
        return /^.{3,}$/.test(value);
    }, "Email o Username deve avere almeno 3 caratteri.");

    $.validator.addMethod("strong_password", function (value) {
        return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])[A-Za-z\d=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-]{8,}$/.test(value)
    }, "Password errata.");

    $.validator.addMethod("checklenght", function (value) {
        return /^.{8,}$/.test(value)
    }, "Password deve avere almeno 8 caratteri.");

    $.validator.addMethod("checklower", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[a-z])/.test(value);
    }, "Password deve avere almeno un carattere minuscolo.");

    $.validator.addMethod("checkupper", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[A-Z])/.test(value);
    }, "Password deve avere almeno un carattere maiuscolo.");

    $.validator.addMethod("checkdigit", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[0-9])/.test(value);
    },"Password deve avere almeno un numero.");

    $.validator.addMethod("checkspecial", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[={}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])/.test(value);
    }, "Password deve avere almeno un carattere speciale.");

    $.validator.addMethod("isEqual", function (value){
        $('#repeat-pass').empty();
        return $('input[name="password"]').val().localeCompare($('input[name="repeatPassword"]').val()) === 0
    }, "Ripeti Password e Password non coincidono.");

    $.validator.addMethod("lenght", function (value){
        const field = $('input[name="privateKey"]').val();
        return field.length > 63 && field.length < 65;
    }, "Private key errata.");

    $.validator.addMethod("radioCheck", function (value){
        const user = $('input[name="user"]');
        const admin = $('input[name="admin"]');
        return user.is(":checked") || admin.is(":checked");
    }, "Seleziona un campo.");

    $("form[name='login-form']").validate({
        rules: {
            email: {
                required: true,
                email: false,
                username_email_lenght: true,
                username_email: true,
            },
            password: {
                required: true,
                strong_password: true,
            }
        },
        messages: {
            password: {
                required: "Password vuota.",
            },
            email: {
                required: "Email o Username vuoti.",
            }
        },
        submitHandler: function(form) {
            if (sendAddressToBackend().localeCompare("ok") === 0){
                const $userAddress = $("input[name='userAddress']")
                const $privateKey = $('input[name="privateKey"]')

                if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun account rilevato. <br>Accedere a Metamask.");
                } else
                    ajaxLogin($privateKey, $userAddress);
            }
            return false;
        }
    });

    $("form[name='register-form']").validate({
        rules: {
            email: {
                required: true,
                email: false,
                username_email_lenght: true,
                username_email: true,
            },
            password: {
                required: true,
                checklower: true,
                checkupper: true,
                checkdigit: true,
                checkspecial: true,
                checklenght: true,
            },
            repeatPassword: {
                required: true,
                checklower: true,
                checkupper: true,
                checkdigit: true,
                checkspecial: true,
                checklenght: true,
                isEqual: true,
            },
            privateKey: {
                required: true,
                lenght: true,
            },
            admin: {
                radioCheck: true,
            },
            user: {
                radioCheck: true,
            },
        },
        messages: {
            password: {
                required: "Password vuota.",
            },
            email: {
                required: "Email o Username vuoti.",
            },
            repeatPassword: {
                required: "Ripeti Password vuota.",
            },
            privateKey: {
                required: "Private key vuota.",
            }
        },
        submitHandler: function(form) {
            if (sendAddressToBackend().localeCompare("ok") === 0){
                const $userAddress = $("input[name='userAddress']")
                const $privateKey = $('input[name="privateKey"]')

                if (typeof $userAddress.val() === "undefined" || $userAddress.val() === ''){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun account rilevato. <br>Accedere a Metamask.");
                } else
                    ajaxRegister($privateKey, $userAddress);
            }
            return false;
        }
    });

    $('#confirmPopupSuccessPrivateKey').on('click', function (){
        const $privateKey = $('input[name="privateKey"]')
        const $userAddress = $("input[name='userAddress']")

        if (($privateKey.val() != null && $privateKey.val() !== '') &&
            $privateKey.val().length > 63 && $privateKey.val().length < 65) {
            closePopupPrivateKey()
            if (window.location.href.indexOf("/kryptoauth/login") > -1) {
                ajaxLogin($privateKey, $userAddress)
            } else ajaxRegister($privateKey, $userAddress)
        } else {
            openPopupError()
            $('div.error-p').children('p').eq(1)
                .html("Chiave privata non corretta. <br>Riprovare.");
        }
    })

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

        if (roleAddress.localeCompare("admin") === 0)
            window.location.href = "/kryptoauth/loginAdmin";
        else
            window.location.href = "/kryptoauth";
    });
})

function ajaxLogin($privateKey, $userAddress){
    const $password = $("input[name='password']")

    $.ajax({
        type: "POST",
        url: "/kryptoauth/login",
        data: {
            email: $("input[name='email']").val(),
            password: $password.val(),
            repeatPassword: $password.val(),
            userAddress: $userAddress.val(),
            privateKey: $privateKey.val(),
        },
        dataType: 'json',
        success: function (data) {
            const $emailInput = $('#email-error')
            const $passwordInput = $('#password-error')
            $('input[name="privateKey"]').val(undefined)

            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/404";
            }

            if (data.msgError['email'] != null) {
                console.log("hhhh")
                if ($emailInput.length)
                    $emailInput.remove()
                $('input[name="email"]').after('<label id="email-error" class="error" for="email">' + data.msgError['email'] + '</label>')
            }

            if (data.msgError['password'] != null) {
                if ($passwordInput.length)
                    $passwordInput.remove()
                $('input[name="password"]').after('<label id="password-error" class="error" for="password">' + data.msgError['password'] + '</label>')
            }

            if (data.msgError['userAddress'] != null) {
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
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

            if (data.msgError['loginError'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Credenziali errate. <br> Riprovare.");
            }

            if (data.msgError['credentialsNotVerified'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("L'admin non ha ancora accettato la registrazione oppure le credenziali sono errate.");
            }

            if (data.msgError['successAdmin'] != null){
                window.location.href = "/kryptoauth/loginAdmin"
            }

            if (data.msgError['successUser'] != null){
                window.location.href = "/kryptoauth"
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}

function ajaxRegister($privateKey, $userAddress){
    $.ajax({
        type: "POST",
        url: "/kryptoauth/register",
        data: {
            email: $("input[name='email']").val(),
            password: $("input[name='password']").val(),
            repeatPassword: $("input[name='repeatPassword']").val(),
            userAddress: $userAddress.val(),
            privateKey: $privateKey.val(),
            role: $('input[name="user"]:checked').val(),
        },
        dataType: 'json',
        success: function (data) {
            const $emailInput = $('#email-error')
            const $passwordInput = $('#password-error')
            const $repeatPasswordInput = $('#repeatPassword-error')
            const $roleInput = $('#user-error')
            $('input[name="privateKey"]').val(undefined)

            if (data.msgError['sessionEmpty'] != null) {
                window.location.href = "/kryptoauth/404"
            }

            if (data.msgError['email'] != null) {
                if ($emailInput.length)
                    $emailInput.remove()
                $('input[name="email"]').after('<label id="email-error" class="error" for="email">' + data.msgError['email'] + '</label>')
            }

            if (data.msgError['password'] != null) {
                if ($passwordInput.length)
                    $passwordInput.remove()
                $('input[name="password"]').after('<label id="password-error" class="error" for="password">' + data.msgError['password'] + '</label>')
            }

            if (data.msgError['repeatPassword'] != null) {
                if ($repeatPasswordInput.length)
                    $repeatPasswordInput.remove()
                $('input[name="repeatPassword"]').after('<label id="repeatPassword-error" class="error" for="email">' + data.msgError['repeatPassword'] + '</label>')
            }

            if (data.msgError['role'] != null) {
                if ($roleInput.length)
                    $roleInput.remove()
                $('.radio-right').after('<label id="user-error" class="error" for="user">' + data.msgError['role'] + '</label>')
            }

            if (data.msgError['userAddress'] != null) {
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Nessun account rilevato. <br>Accedere a Metamask.");
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

            if (data.msgError['alredyRegistered'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Utente già registrato.");
            }

            if (data.msgError['errorAdmin'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Account <i>Admin</i>: completare correttamente il form.");
            }

            if (data.msgError['errorUser'] != null){
                openPopupError()
                $('div.error-p').children('p').eq(1)
                    .html("Account <i>User</i>: completare correttamente il form.");
            }

            if (data.msgError['success'] != null){
                openPopupSuccess();
            }

            if (data.msgError['adminActive'] != null){
                openPopupSuccess();
                $('#popupSuccess').children('div').eq(1)
                    .children('p').eq(1).html("Salvataggio effettuato.")
                roleAddress = "admin"
            }
        },
        error: function (e) {
            console.log(e)
        }
    });
}