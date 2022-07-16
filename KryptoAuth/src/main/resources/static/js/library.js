$(document).ready(function (){

    // anchor page
    $('a').on('click',function(){
        const $anchor = $(this);
        $('html, body').animate({
            scrollTop: $($anchor.attr('href')).offset().top + "px"
        }, 800);
    });

    // scroolbar for stiky navbar
    const scrollDemo = document.querySelector("body");
    const $navbar_clone = $('#navbar-clone')
    const $menu_clone = $('#menu-clone')
    const $cloneNavbarMenu = $('#cloneNavbarMenu')
    const $navbarMenu = $('#navbarMenu')

    scrollDemo.addEventListener("scroll", event => {
        if (`${scrollDemo.scrollTop}` >= 80){
            if (!$navbar_clone.hasClass('is-active'))
                $navbar_clone.addClass('is-active')

            if ($navbarMenu.hasClass('is-active')) {
                $navbarMenu.removeClass('is-active')
                $('span.icon-box-toggle').removeClass('active')
            }
        }
        if (`${scrollDemo.scrollTop}` < 80){
            if ($navbar_clone.hasClass('is-active'))
                $navbar_clone.removeClass('is-active')

            if ($cloneNavbarMenu.hasClass('is-active')) {
                $cloneNavbarMenu.removeClass('is-active')
            }
            $('span.icon-box-toggle').removeClass('active')
        }
    }, { passive: true });

    $menu_clone.on('click', function (){
        setTimeout( function(){
            if (!$navbar_clone.hasClass('is-active')) {
                $navbar_clone.addClass('is-active')
            }

            if ($menu_clone.hasClass('active') && $cloneNavbarMenu.hasClass('is-active')) {
                $menu_clone.addClass('active')
                $cloneNavbarMenu.addClass('is-active')
            }
        },25);
    })

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
        $navbar_clone.css('width', width + "px");
    }
})


