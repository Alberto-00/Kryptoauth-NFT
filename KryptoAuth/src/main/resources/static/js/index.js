$(document).ready(function (){
    $.validator.addMethod("emailMex", function (value){
        return /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value);
    }, "Email non corretta");

    $.validator.addMethod("message", function (value) {
        return /^.{10,}$/.test(value)
    }, "Messaggio troppo corto");

    $("form[name='message-form']").validate({
        rules: {
            email: {
                required: true,
                email: false,
                emailMex: true
            },
            nome: {
                required: true,
            },
            message: {
                required: true,
                message: true
            }
        },
        messages: {
            email: {
                required: "Email vuota",
            },
            nome: {
                required: "Nome vuoto",
            },
            message: {
                required: "Messaggio vuoto",
            }
        },
        submitHandler: function(form) {
            form.submit()
        }
    });
})