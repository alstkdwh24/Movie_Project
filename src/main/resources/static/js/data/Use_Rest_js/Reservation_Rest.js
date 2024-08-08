
    $.ajax({
        type: "get",
        url: "/getMovieCategory",
        success: function (data) {
            console.log(data);
            movie_category_createtwo(data);
            console.log("/getMovieCategory");

        },
        error: function (err, status) {
            console.log(err, status);
            alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
        }
    });

    $(document).ready(function () {
        $('.sub_menu_toggle > a').click(function () {
            if (!$(this).hasClass("sub_menu_select")) {
                $(this).next('ul').slideDown();
                $(this).addClass('sub_menu_select');
            } else {
                $(this).next('ul').slideUp();
                $(this).removeClass("sub_menu_select");
            }
        });
    });

    function getMovieCategory_List(e) {

        e.preventDefault();
        if (e.target.tagName !== 'A')
            return;

        let set = $(e.target).data("set");

        $(e.currentTarget).find("a").removeClass("sub_menu_select");
        $(e.target).addClass("sub_menu_select");

        if (set.hasOwnProperty('movie_category_lv')) {
            console.log("존재합니다.");
            console.log("set");
        }

        if (set.movie_category_lv === 1) {
            console.log("1lv");
            $(e.currentTarget).loading();
            $(e.currentTarget).movie_category_remove();

            $.ajax({
                type: "get",
                url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                success: function (data) {
                    console.log(data);
                    movie_category_create(data);
                },
                error: function (err, status) {
                    console.log(err, status);
                }
            });

            // 추가하는 과정


            $(e.target).movie_category_set();
        } else if (set.movie_category_lv === 2) {
            console.log("1lv");
            $(e.currentTarget).loading();
            $(e.currentTarget).movie_category_remove();

            $.ajax({
                type: "get",
                url: "/getMovieCategoryChild/" + set.group_id + "/" + set.movie_category_lv + "/" + set.movie_detail_lv,
                success: function (data) {
                    console.log(data);
                    movie_category_creates(data);
                },
                error: function (err, status) {
                    console.log(err, status);
                }
            });

            // 추가하는 과정


            $(e.target).movie_category_set();
        }
    }

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


    function movie_category_create(data){
        createMovieCategory(data,'movie_place');
    }


    function movie_category_createtwo(data){
        createMovieCategory(data,'movie_title');
    }


    function movie_category_creates(data){
        createMovieCategory(data,'movie_time');
    }

    function createMovieCategory(data, clickHandler){
        console.log(data);
        if(!Array.isArray(data)){
            console.log("data is not an array or is undefined", data);
            return;
        }

        let movie_category = '<ul class="movies_titles" onclick="getMovieCategory_List(event);">';
        data.forEach(function(result, index){
            let onClickFunction = index === data.length -1
                ? 'Movie_reservation_modal(event)':
                  `${clickHandler}(event)`;

            movie_category += `
            <li class="movie_name">
                <a href="#" data-set='${JSON.stringify(result)}' name="movie_title" onclick="${onClickFunction}">
                    ${result.movie_detail_title}
                </a>
            </li>`;

        });

        movie_category +='</ul>';
        $("#reservation_board").append(movie_category);
    }
//---------------------------------------------------------------------------------------------------------------------------------------

        let reservation_container=document.getElementById("reservation_modal");
        let closecl=document.getElementById("closecl");
let reservation_modal_contents=document.getElementById("reservation_modal_contents");

    let reservation_submit=document.querySelectorAll("#reservation_submit");
    reservation_submit.onclick=function (){
        document.reservation.action="reservation_resist";
        document.reservation.submit();
        document.reservation.method="Post"
        console.log("안녕");
    }

    function Movie_reservation_modal() {

        reservation_container.style.display="flex";

    }
    closecl.onclick=function (){

        reservation_container.style.display="none";


    }
    reservation_container.onclick=function (e){
        e.preventDefault();
        reservation_container.style.display="none";
    }
//---------------------------------------------------------------------------------------
    //데이터 보내기

    // $.ajax({
    //     type: "get",
    //     url: "/reservation",
    //     success: function (data) {
    //         console.log(data);
    //         console.log("/getMovieCategory");
    //     },
    //     error: function (err, status) {
    //         console.log(err, status);
    //         alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
    //     }
    // });


