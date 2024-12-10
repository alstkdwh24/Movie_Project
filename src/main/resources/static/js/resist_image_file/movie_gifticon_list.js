document.addEventListener('DOMContentLoaded',(event)=>{
let ant_three_array=[];
let imageUrl=[];
let filePath=[];
let Gifticon_name=[];
    $.ajax({
        type:"get",
        url:"/movie_resist/movie_gifticon_list",
        contentType:"application/json",
        success: function (data) {
            // let ant_three = document.querySelectorAll("#ant_three");
            // ant_three.forEach((ant_three_two, index) => {
            //     ant_three_array[index] = ant_three_two;
            // })
            if (Array.isArray(data) && data.length> 0 ) {
                data.forEach(function (item,index){
                    imageUrl[index]=item.imageUrl;

                    console.log("imageUrl[index]",imageUrl[index]);

                    filePath[index]=item.filePath;
                    Gifticon_name[index] = item.Gifticon_name;
                    console.log("Gifticon_name[index]", Gifticon_name[index]);

                    console.log("item.GifticonHtml", item.htmlContent); // 추가된 부분
                    $("#ant_threes").append(item.htmlContent); // HTML 추가
                    console.log(item.htmlContent, "item.GifticonHtml"); // 추가된 부분
                    console.log(item.htmlContent, "item.GifticonHtml"); // 추가된 부분

                })
            }
        }
    })


});