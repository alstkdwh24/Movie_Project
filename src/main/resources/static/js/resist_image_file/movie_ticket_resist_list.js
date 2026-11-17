document.addEventListener('DOMContentLoaded', (event) => {
    let imageUrl = [];
    let filePath = [];
    let movie_ticket_filename = [];
    let movie_ticket_image=[];

    $.ajax({
        type: "Get",
        url: "/movie_resist/movie_ticket_resist_list",
        contentType: "application/json",
        success: function (data) {
            console.log(data);

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(function (item, index) {
                    imageUrl[index] = item.movie_ticket_url;

                    console.log("imageUrl[index]", imageUrl[index]);

                    filePath[index] = item.filePath;
                    console.log("filePath[index]", filePath[index]);

                    movie_ticket_filename[index] = item.movie_ticket_filename;
                    console.log("movie_ticket_filename[index]", movie_ticket_filename[index]);

                    $("#ant_two").append(item.movie_ticket_htmlContent);

                    $.ajax({
                        type:"get",
                        url:`/movie_resist/movie_ticket_resist_list/files/${filePath[index]}/${movie_ticket_filename[index]}`,
                        xhrFields:{
                            responseType:"blob"
                        },
                        success: function (response, status, xhr) {
                            let contents_img=document.querySelectorAll("#contents_img_two");
                            contents_img.forEach((movie_ticket, index)=>{
                                movie_ticket_image[index]=movie_ticket;
                            })

                            let blob=new Blob([response],{type:xhr.getResponseHeader('Content-Type')});

                            let url=URL.createObjectURL(blob);

                            let img = document.createElement("img");
                            img.src=url;
                            img.alt="영화이미지";

                            img.className = "img";

                            movie_ticket_image[index].appendChild(img);
                            movie_ticket_image[index].querySelector(".loader").remove();
                            setTimeout(() => {
                                URL.revokeObjectURL(url);
                            }, 100);
                        }
                    })
                })
            }
        }

    })
})