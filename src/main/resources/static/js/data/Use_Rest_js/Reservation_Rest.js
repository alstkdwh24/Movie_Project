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
    $('.sub_menu_toggle > div').click(function () {
        if (!$(this).hasClass("sub_menu_select")) {
            $(this).next('div').slideDown();
            $(this).addClass('sub_menu_select');
        } else {
            $(this).next('div').slideUp();
            $(this).removeClass("sub_menu_select");
        }
    });
});

function getMovieCategory_List(e) {

    e.preventDefault();
    if (e.target.tagName !== 'DIV') return;

    let set = $(e.target).data("set");

    $(e.currentTarget).find("div").removeClass("sub_menu_select");
    $(e.target).addClass("sub_menu_select");


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
        })

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

function movie_category_createtwo(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }
    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles" onclick="getMovieCategory_List(event);">';
    data.forEach(function (result, index) {
        movie_category += `
    <div class="movie_name" 
         data-set='${JSON.stringify(result)}'  id="movie_title246">
 ${result.movie_detail_title}
         <input type="hidden" class="input_movie_title" value="${result.movie_detail_title}" readonly>
    </div>`;
    });

    movie_category += '</div>';
    $("#reservation_board").append(movie_category);
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
    <div href="#" class="movie_name" 
         data-set='${JSON.stringify(result)}' id="movie_place246">
    ${result.movie_detail_title}
         <input type="hidden" class="input_movie_place" value="${result.movie_detail_title}"   readonly >
    </div>`
    });
    $("#reservation_board").append(movie_category);
}

$.fn.loading = function () {
    $(".loading").css({display: "block"});
    window.setTimeout(function () {
        $(".loading").css({display: "none"});
    }, 1000);
};

//---------------------------------------------------------------------------------------------------------------------------------------

function movie_category_creates(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }
    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles" onclick="Movie_reservation_modal(event)">';
    data.forEach(function (result, index) {

        movie_category += ` <div class="movie_name" data-set='${JSON.stringify(result)}' id="movie_time246" >
${result.movie_detail_title}
        <input type="hidden"  class="input_movie_time" value="${result.movie_detail_title}"  
           readonly >
            </div>`;


    });


    movie_category += '</div>';


    $("#reservation_board").append(movie_category);
}


let reservation_modal_contents = document.getElementById("reservation_modal_contents");
let reservation_two = document.getElementById("reservation_two");

function Movie_reservation_modal(e) {
    reservation_two.style.display = "flex"

    // let reservationData = {
    //     movieTitle: $("#movie_title").innerHTML(), // 영화 제목 입력란의 값
    //     movieTime: $('#movie_time').innerHTML(),   // 영화 시간 입력란의 값
    //     moviePlace: $('#movie_place').innerHTML()   // 사용자 이름 입력란의 값
    // };
    // console.log(movieTitle, movieTime, moviePlace);
    //
    //
    //     $.ajax({
    //         type:"POST",
    //         url:"/resist_reservation",
    //         data: JSON.stringify(reservationData),
    //         contentType: "application/json",
    //         success: function(response) {
    //             alert("예약이 완료되었습니다: " + response.movieTitle);
    //
    //         },
    //
    //         error: function(xhr) {
    //             alert("예약에 실패했습니다: " + xhr.responseText);
    //         }
    //
    //
    // } );

    // Reservation_Chair_Seat(event)
    movie_title_Rest(event);
    let movie_name = $(".movie_name");
    if (!movie_name.hasClass("sub_menu_select")) {
        movie_name.addClass("sub_menu_select");
    }


}

// 모달을 여는 함수
function Movie_reservation_modal(e) {
    reservation_two.style.display = "flex";

    // 모달을 열 때 예약 데이터를 설정
    let reservationData = {
        movieTitle: $(".input_movie_title").val(), // 영화 제목 입력란의 값
        movieTime: $(".input_movie_time").val(),   // 영화 시간 입력란의 값
        moviePlace: $(".input_movie_place").val()  // 영화 장소 입력란의 값
    };
    console.log(reservationData);

    // 서버에 예약 데이터 전송
    $.ajax({
        type: "POST",
        url: "/resist_reservation",
        data: JSON.stringify(reservationData),
        contentType: "application/json",
        success: function(response) {
            alert("예약이 완료되었습니다: " + response.movieTitle);
        },
        error: function(xhr) {
            alert("예약에 실패했습니다: " + xhr.responseText);
        }
    });

    // 예약 확인 후 선택된 영화 표시
    let movie_name = $(".movie_name");
    if (!movie_name.hasClass("sub_menu_select")) {
        movie_name.addClass("sub_menu_select");
    }
}

// 예약 데이터를 가져오는 함수
function getReservationData() {
    const movieTitle = $(".input_movie_title").map(function() {
        return $(this).val();
    }).get();

    const movieTime = $(".input_movie_time").map(function() {
        return $(this).val();
    }).get();

    const moviePlace = $(".input_movie_place").map(function() {
        return $(this).val();
    }).get();

    return {
        movieTitle: movieTitle,
        movieTime: movieTime,
        moviePlace: moviePlace
    };
}

// 예약 시트 선택 및 예약 데이터 전송
function Reservation_Chair_Seat(event) {
    event.preventDefault(); // 기본 동작 방지

    let reservationData = getReservationData();
    console.log(reservationData); // 콘솔에 객체 출력

    // 입력값 검증 (예: 영화 제목이 비어있지 않은지 확인)
    if (reservationData.movieTitle.length === 0) {
        alert("영화 제목을 선택해 주세요.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/resist_reservation",
        data: JSON.stringify(reservationData),
        contentType: "application/json",
        success: function(response) {
            alert("예약이 완료되었습니다: " + response.movieTitle);
        },
        error: function(xhr) {
            alert("예약에 실패했습니다: " + xhr.responseText);
        }
    });
}

// DOM이 준비되면 모달 트리거 및 데이터 수집 설정
$(document).ready(function() {
    // 모달 트리거
    $(".movie_name").click(function(event) {
        Movie_reservation_modal(event);
    });
});