document.addEventListener("DOMContentLoaded",event=>{

    $.ajax({
        type:"get",
        url:"/movie_resist/event_list",
        contentType: "application/json",
        success:function (data) {
            console.log(data);

            $.ajax({
                type:"get",
                url:"",
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