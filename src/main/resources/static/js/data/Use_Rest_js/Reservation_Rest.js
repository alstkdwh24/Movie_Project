let movieDataArray = []
$.ajax({
    type: "get", url: "/getMovieCategory", success: function (data) {
        console.log(data);
        movie_category_createtwo(data);

        console.log("/getMovieCategory");

    }, error: function (err, status) {
        console.log(err, status);
        alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
    }
});



function movie_category_createtwo(data) {
    console.log(data);
    if (!Array.isArray(data)) {
        console.log("data is not an array or is undefined:", data);
        return;
    }

    $.ajax({
        type: "get", url: "/getMovieCategory2", success: function (data) {
            console.log(data);
            movie_category_create(data);

            console.log("/getMovieCategory2");

        }, error: function (err, status) {
            console.log(err, status);
            alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
        }
    });
    console.log("data is an array or is defined:", data);
    let movie_category = '<div class="movies_titles" onclick="getMovieCategory_List(event);">';
    data.forEach(function (result, index) {

        movie_category += `
    <div class="movie_name movie_title246" 
         data-set='${JSON.stringify(result)}' >
 ${result.movie_detail_title}
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

    $.ajax({
        type: "get", url: "/getMovieCategory3", success: function (data) {
            console.log(data);
            movie_category_creates(data);

            console.log("/getMovieCategory3");

        }, error: function (err, status) {
            console.log(err, status);
            alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
        }
    });
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
    let movie_category = '<div class="movies_titles"  onclick="getMovieCategory_List(event);">';
    data.forEach(function (result, index) {


        movie_category += ` <div  class="movie_name movie_time246" data-set='${JSON.stringify(result)}'   >
${result.movie_detail_title}


            </div>`;


    });


    movie_category += '</div>';

    $("#reservation_board").append(movie_category);
}
let reservation_modal_contents = document.getElementById("reservation_modal_contents");
let reservation_two = document.getElementById("reservation_two");

