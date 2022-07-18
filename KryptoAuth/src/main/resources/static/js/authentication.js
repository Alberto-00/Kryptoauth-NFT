function timer() {
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
timer();

$(document).ready(function (){

    $.validator.addMethod("username_email", function (value){
        return /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){2,18}[a-zA-Z0-9]$/.test(value) | /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value);
    }, "Email or Username field wrong.");

    $.validator.addMethod("strong_password", function (value) {
        return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[=^ ì{}+çò°àù§èé#@$!%€*?&:,;'._<>|-])[A-Za-z\d=^ ì{}+çò°àù§èé#@$!%€*?&:,;'._<>|-]{8,}$/.test(value)
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
        return /^(?=.*[={}+çò°àù§èé#@$!%€*?&:,;'._<>|-])/.test(value);
    }, "Password must have at last one special character.");

    $("form[name='login-form']").validate({
        rules: {
            email: {
                required: true,
                email: false,
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

    $("form[name='register-form']").validate({
        rules: {
            email: {
                required: true,
                email: false,
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
                required: "Password field is empty.",
            }
        },
        submitHandler: function(form) {
            /*$("#open").click(function(){
                $(".shadow").css("display","block");
                $("#popupSuccess").css("display","block");
                $("html, body").animate({scrollTop: 0}, 700);
            });

            $(".shadow").click(function(){
                $(this).fadeOut();
                $("#popupSuccess").fadeOut();
                form.submit();
            });

            $('#confirmPopupSuccess').click(function (){
                $(".shadow").fadeOut();
                $("#popupSuccess").fadeOut();
                form.submit();
            })*/

            $("#open").click(function () {
                $(".shadow").css("display", "block");
                $("#popupError").css("display", "block");
                $("html, body").animate({scrollTop: 0}, 700);
            });

            $(".shadow").click(function () {
                $(this).fadeOut();
                $("#popupError").fadeOut();
                form.submit();
            });

            $('#confirmPopupError').click(function () {
                $(".shadow").fadeOut();
                $("#popupError").fadeOut();
                form.submit();
            });
        }
    });
})