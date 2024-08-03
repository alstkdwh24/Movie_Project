

$(document).ready(function() {
    $('.sub_menu_toggle > a').click(function(){

        if(!$(this).hasClass("sub_menu_select") ){
            $(this).next('ul').slideDown();
            $(this).addClass('sub_menu_select');
        } else {
            $(this).next('ul').slideUp();
            $(this).removeClass("sub_menu_select");
        }

    });
});
function getMovieCategory_List(e) {
    // e.preventDefault();
    if (e.target.tagName != 'a') return;
    let dataset = $(e.target).data("set");


    $(e.currentTarget).find("a").removeClass("sub_menu_select");
    $(e.target).addClass("sub_menu_select");

    if (dataset.movie_category_lv === 1 || dataset.movie_category_lv === 2) {
        console.log("1lv");
        $().loading();
        $(e.currentTarget).movie_category_remove();
        movie_category_create();

        $.ajax({
            type: "get",
            url: "/getMovieCategoryChild/" + dataset.group_id + "/" + dataset.movie_category_lv + "/" + dataset.movie_detail_lv,
            success: function (data) {
                console.log(data);
                movie_category_create(data);

            },
            error: function (err, status) {
                console.log(err, status);
            }
        });


        $(e.target).movie_category_set();

    }

}

$.fn.movie_category_set = function () {
    let movie_category_number = this.data("set").movie_category_number;
    let group_id = this.data("set").group_id;
    $("input[name='movie_write_category']").val(group_id + movie_category_number);
}
$.fn.movie_category_remove = function () {
    while (this.next().length != 0) {
        $(this).next().remove();
    }
}

function movie_category_create(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefine:", data);
        return;
    }
    let movie_category = '<div class="movies_titles" onclick="getMovieCategory_List(e);">';
    data.forEach(function (result, index) {
        movie_category += '<div class="movie_name"><a href="#" data-set=' + JSON.stringify(result) + '>' + result.movie_detail_title + '</a></div>';
    });
    movie_category += '</div>'
    $("#reservation_board").append(movie_category);
}

$.fn.loading = function () {
    $(".loading").css({display: "block"});
    window.setTimeout(function () {
        $(".loading").css({display: "none"});
    }, 1000);

}