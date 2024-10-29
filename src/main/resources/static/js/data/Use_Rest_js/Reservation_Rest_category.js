$.ajax({
    type: "get",
    url: "/getMovieCategory",
    success: function (data) {
        movie_category_create_two(data);

    },
    error: function (err, status) {
        console.log(err, status);
        alert("카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요")

    }
})
$(document).ready(function () {
    $('.sub_menu_toggle > div').click(function () {
        // movie_category_lv가 1인지 확인

        if (!$(this).hasClass("sub_menu_select")) {
            $(this).next('div').slideDown();
            $(this).addClass('sub_menu_select');
        } else {
            $(this).next('div').slideUp();
            $(this).removeClass("sub_menu_select");
        }

    });
});

$.fn.movie_category_set = function () {
    let dataSet = this.data("set");
    console.log("dataSet:", dataSet);
    let movie_category_number = dataSet.movie_category_number;
    let group_id = dataSet.group_id;
    console.log("group_id:", group_id);
    console.log("movie_category_number:", movie_category_number);
    $("input[name='movie_write_category']").val(group_id + movie_category_number);
};

$.fn.movie_category_remove = function () {
    while (this.next().length !== 0) {
        $(this).next().remove();
    }
};
$.fn.loading = function () {
    $(".loading").css({display: "block"});
    window.setTimeout(function () {
        $(".loading").css({display: "none"});
    }, 1000);
};


function movie_category_create_two(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }
    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles" >';
    data.forEach(function (result, index) {
        movie_category += `
        <div class="movie_name movie_title246" data-set='${JSON.stringify(result)}'>${result.movie_detail_title}
        </div>`;

    });
    movie_category += `</div>`;
    $("#reservation_board").append(movie_category);
    let movies_titles = document.querySelectorAll(".movies_titles");


    movies_titles.forEach(function (movie_title) {
        movie_title.addEventListener('click', function (event) {
            console.log("1");
            event.preventDefault();
            if (event.target.tagName !== 'DIV') {
                return
            }
            let set = $(event.target).data("set");

            $(event.currentTarget).find("div").removeClass("sub_menu_select");
            $(event.target).addClass("sub_menu_select");

            if (set.movie_category_lv === 1) {
                console.log("1lv");
                $(event.currentTarget).loading();
                $(event.currentTarget).movie_category_remove();

                $.ajax({
                    type: "get",
                    url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                    success: function (data) {
                        console.log(data);
                        movie_category_create(data)

                    }, error: function (err, status) {
                        console.log(err, status);

                    }
                })
                $(event.target).movie_category_set();
            } else if (set.movie_category_lv === 2) {
                console.log("1lv");
                $(event.currentTarget).loading();
                $(event.currentTarget).movie_category_remove();

                $.ajax({
                    type: "get",
                    url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                    success: function (data) {
                        console.log(data);
                        movie_category_creates(data);
                    }, error: function (err, status) {
                        console.log(err, status);

                    }
                });
                $(event.target).movie_category_set();
            }
        });
    });
}


function movie_category_create(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }


    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles" onclick="getMovieCategory_List(event);">';
    data.forEach(function (result, index) {

        movie_category += `
    <div href="#" class="movie_name movie_place246" 
         data-set='${JSON.stringify(result)}'">
            ${result.movie_detail_title}
    </div>`
    });
    $("#reservation_board").append(movie_category);
    let movies_titles = document.querySelectorAll(".movies_titles");
    movies_titles.forEach(function (movie_title) {
        movie_title.addEventListener('click', function (event) {
            console.log("1");
            event.preventDefault();
            if (event.target.tagName !== 'DIV') {
                return
            }
            let set = $(event.target).data("set");

            $(event.currentTarget).find("div").removeClass("sub_menu_select");
            $(event.target).addClass("sub_menu_select");


            if (set.movie_category_lv === 1) {
                console.log("1lv");
                $(event.currentTarget).loading();
                $(event.currentTarget).movie_category_remove();

                $.ajax({
                    type: "get",
                    url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                    success: function (data) {
                        console.log(data);
                        movie_category_create(data)

                    }, error: function (err, status) {
                        console.log(err, status);

                    }
                })
                $(event.target).movie_category_set();
            } else if (set.movie_category_lv === 2) {
                console.log("1lv");
                $(event.currentTarget).loading();
                $(event.currentTarget).movie_category_remove();

                $.ajax({
                    type: "get",
                    url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                    success: function (data) {
                        console.log(data);
                        movie_category_creates(data);
                    }, error: function (err, status) {
                        console.log(err, status);

                    }
                });
                $(event.target).movie_category_set();
            }

        })

    })
    // $.ajax({
    //     type: "get", url: "/getMovieCategory3", success: function (data) {
    //         console.log(data);
    //         movie_category_creates(data);
    //
    //         console.log("/getMovieCategory3");
    //
    //     }, error: function (err, status) {
    //         console.log(err, status);
    //         alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
    //     }
    // });

}

function movie_category_creates(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }
    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles"  onclick="getMovieCategory_List(event);">';
    data.forEach(function (result, index) {


        movie_category += ` <div  class="movie_name movie_time246" data-set='${JSON.stringify(result)}'   >
${result.movie_detail_title}


            </div>`;


    });


    movie_category += '</div>';

    $("#reservation_board").append(movie_category);
}