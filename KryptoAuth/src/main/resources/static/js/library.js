$(document).ready(function (){

    $('a').on('click',function(){
        const $anchor = $(this);
        $('html, body').animate({
            scrollTop: $($anchor.attr('href')).offset().top + "px"
        }, 800);
    });
})
