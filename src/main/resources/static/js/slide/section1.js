document.addEventListener('DOMContentLoaded', (event) => {
$.ajax({
    type:"get",
    url:"/movie/movie/mains"

})

    let moving = document.getElementById("moving");
    let movie_watch = document.getElementById("movie_watch");


    moving.onclick = function () {



    }

    movie_watch.onclick = function () {
        $.ajax({
            type: "get",
            url: "",
            contentType: "application/json",
            success: function () {

            },
            error: function () {

            }

        })
    }
})