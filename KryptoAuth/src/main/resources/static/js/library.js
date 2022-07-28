$(document).ready(function (){

    // anchor page
    /*$('a').on('click',function(){
        const $anchor = $(this);
        $('html, body').animate({
            scrollTop: $($anchor.attr('href')).offset().top + "px"
        }, 800);
    });*/

    // down page without anchor
    $('#info-roadmap').on('click', function() {
        $("html, body").animate({ scrollTop: 700 }, 800);
    });

    $('#info-home').on('click', function() {
        $("html, body").animate({ scrollTop: 940 }, 800);
    });

    $('#info-goals').on('click', function() {
        $("html, body").animate({ scrollTop: 940 }, 800);
    });

    $('#verify').on('click', function() {
        $("html, body").animate({ scrollTop: 750 }, 800);
    });

    // set dynamic sticky navbar
    $(window).on("load", function () {
        setWidthNav();
    });

    $(window).on('resize', function(){
        setWidthNav();
    });

    function setWidthNav(){
        const nav = document.getElementById("nav");
        const width = nav.offsetWidth;
        $('#navbar-clone').css('width', width + "px");
    }
})


