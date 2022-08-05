$(document).ready(function(){
    $(".hide").slice(0, 3).show();
    $("#loadMore").on("click", function(e){
        e.preventDefault();
        $(".hide:hidden").slice(0, 2).slideDown();
        if($(".hide:hidden").length == 0) {
            $("#loadMore").text("Nessun Contenuto");
        }
    });
})