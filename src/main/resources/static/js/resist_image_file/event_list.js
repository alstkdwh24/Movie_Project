document.addEventListener("DOMContentLoaded",event=>{

    $.ajax({
        type:"get",
        url:"/movie_resist/event_list",
        contentType: "application/json",
        success:function (data) {
            let html='';
            data.forEach(function (event) {
                html+=event.htmlContent
            })
                $(".good_movies_title_bottom").append(html)

            $.ajax({
                type:"get",
                url:`/movie_resist/event_resist/${imgUrl}`,
                contentType:"application/json" ,
                xhrFields: {
                    responseType: "blob" // Blob 형식으로 응답 받기
                },
                success:function (){

                }
            })
        }

    })


});