export function openPopupErrorMetamask(){
    $('div.error-p').children('p').eq(1)
        .html("Metamask non è collegato.<br>" +
            "Oppure l'estensione non è presente.");

    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
    $("html, body").animate({scrollTop: 0}, 700);
}

export function openPopupError(){
    $(".shadow").css("display", "block");
    $("#popupError").css("display", "block");
    $("html, body").animate({scrollTop: 0}, 700);
}

export function closePopupError(){
    $(".shadow").fadeOut();
    $("#popupError").fadeOut();
}

export function openPopupSuccess(){
    $(".shadow").css("display","block");
    $("#popupSuccess").css("display","block");
    $("html, body").animate({scrollTop: 0}, 700);
}

export function closePopupSuccess(){
    $(".shadow").fadeOut();
    $("#popupSuccess").fadeOut();
}

export function openPopupPrivateKey(){
    $(".shadow").css("display","block");
    $("#popupSuccessPrivateKey").css("display","block");
    $("html, body").animate({scrollTop: 0}, 700);
}

export function closePopupPrivateKey(){
    $(".shadow").fadeOut();
    $("#popupSuccessPrivateKey").fadeOut();
}