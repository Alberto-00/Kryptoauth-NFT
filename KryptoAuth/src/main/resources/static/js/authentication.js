import {closePopupError, openPopupError} from './popup.js'
import {sendAddressToBackend} from './metamask.js'

window.timer = function(event){
    var now = new Date();
    var start = new Date(now.getFullYear(), 0, 0);
    var diff = (now - start) + ((start.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000);
    var oneDay = 1000 * 60 * 60 * 24;
    var day = Math.floor(diff / oneDay);

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

$(document).ready(function (){

    $.validator.addMethod("username_email", function (value){
        return /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,}[a-zA-Z0-9]$/.test(value) | /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value);
    }, "Email or Username field wrong.");

    $.validator.addMethod("username_email_lenght", function (value){
        return /^.{3,}$/.test(value);
    }, "Email or Username must have at last 3 characters.");

    $.validator.addMethod("strong_password", function (value) {
        return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])[A-Za-z\d=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-]{8,}$/.test(value)
    }, "Password wrong.");

    $.validator.addMethod("checklenght", function (value) {
        return /^.{8,}$/.test(value)
    }, "Password must have at last 8 characters.");

    $.validator.addMethod("checklower", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[a-z])/.test(value);
    }, "Password must have at last one lower character.");

    $.validator.addMethod("checkupper", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[A-Z])/.test(value);
    }, "Password must have at last one upper character.");

    $.validator.addMethod("checkdigit", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[0-9])/.test(value);
    },"Password must have at last one digit.");

    $.validator.addMethod("checkspecial", function (value){
        $('#repeat-pass').empty();
        return /^(?=.*[={}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])/.test(value);
    }, "Password must have at last one special character.");

    $.validator.addMethod("isEqual", function (value){
        $('#repeat-pass').empty();
        return $('input[name="password"]').val().localeCompare($('input[name="repeatPassword"]').val()) === 0
    }, "Repeat Password and Password are not equals.");

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
                required: "Password field is empty.",
            },
            email: {
                required: "Email or Username field is empty.",
            }
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    $('#log').on('click', function (){
        $("input[name='repeatPassword']").val($('#password').val())
    })

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
            }
        },
        messages: {
            password: {
                required: "Password field is empty.",
            },
            email: {
                required: "Email or Username field is empty.",
            },
            repeatPassword: {
                required: "Repeat Password field is empty.",
            }
        },
        submitHandler: function(form) {
            if (sendAddressToBackend().localeCompare("ok") === 0){
                const $userAddress = $("input[name='userAddress']")

                if (typeof $userAddress.val() === "undefined" || $userAddress === ''){
                    openPopupError()
                    $('div.error-p').children('p').eq(1)
                        .html("Nessun account rilevato. <br>Accedere a Metamask.");
                }

                $.ajax({
                    type: "POST",
                    url: "/kryptoauth/register",
                    data: {
                        email: $("input[name='email']").val(),
                        password: $("input[name='password']").val(),
                        repeatPassword: $("input[name='repeatPassword']").val(),
                        userAddress: $userAddress.val(),
                    },
                    dataType: 'json',
                    cache: false,
                    success: function (data) {
                        const $emailInput = $('#email-error')
                        const $passwordInput = $('#password-error')
                        const $repeatPasswordInput = $('#repeatPassword-error')

                        if (data.msgError['email'] != null){
                            if ($emailInput.length)
                                $emailInput.remove()
                            $('input[name="email"]').after('<label id="email-error" class="error" for="email">' + data.msgError['email'] + '</label>')
                        }

                        if (data.msgError['password'] != null){
                            if ($passwordInput.length)
                                $passwordInput.remove()
                            $('input[name="password"]').after('<label id="password-error" class="error" for="password">' + data.msgError['password'] + '</label>')
                        }

                        if (data.msgError['repeatPassword'] != null){
                            if ($repeatPasswordInput.length)
                                $repeatPasswordInput.remove()
                            $('input[name="repeatPassword"]').after('<label id="repeatPassword-error" class="error" for="email">' + data.msgError['repeatPassword'] + '</label>')
                        }

                        if (data.msgError['userAddress'] != null){
                            openPopupError()
                            $('div.error-p').children('p').eq(1)
                                .html("Nessun account rilevato. <br>Accedere a Metamask.");
                        }
                    },
                    error: function (e) {
                        console.log(e)
                    }
                });
            }
            return false;
        }
    });

    $(".shadow").click(function () {
        closePopupError()
    });

    $('#confirmPopupError').click(function () {
        closePopupError()
    });
})