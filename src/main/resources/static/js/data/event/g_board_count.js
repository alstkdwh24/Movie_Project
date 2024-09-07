let gallery_content=document.querySelectorAll(".gallery_blank_mini2")
gallery_content.forEach(function (gallery_contents){
    gallery_contents.onclick=function (){
        let gNumber=gallery_contents.dataset.g_number;
        console.log(gNumber);
        $.ajax({
            type:"Post",
            url:"/movie/community/g_board_count",
            contentType:"application/JSON",
            data:JSON.stringify({g_number:gNumber}),
            success: function (){
                alert("조회수 1이 증가하였습니다.")
                location.href=`/movie/community/g_detail?g_number=${gNumber}`;
            },
            error:function (){
                alert("에러가 발생했습니다.")
            }

        });
    }
});