document.addEventListener("DOMContentLoaded",(event) =>{

    let delicious_url=[];
    let filePath=[];
    let delicious_name=[];
    let delicious_filename=[];
    let delicious_image=[]
    $.ajax({
        type:"get",
        url:"/movie_resist/DeliciousVO_list",
        contentType:"application/json",
        success:function (data) {


            console.log(data);

            if(Array.isArray(data) && data.length > 0){
                data.forEach(function (item, index) {

                    delicious_url[index]=item.delicious_url;
                    console.log(delicious_url[index] , "delicious_url[index]");

                    filePath[index]= item.filePath;
                    console.log(filePath[index],"filePath[index]");

                    delicious_filename[index]=item.delicious_filename;

                    delicious_name[index]=item.delicious_name;
                    console.log(delicious_name[index], "delicious_name[index]");

                    $("#ant_one").append(item.delicious_htmlContent);

                    $.ajax({
                        type:"get",
                        url:`/movie_resist/DeliciousVO_list/files/${filePath[index]}/${delicious_filename[index]}`,
                        xhrFields: {
                            responseType: "blob" // Blob 형식으로 응답 받기
                        },
                        success:function (response, status, xhr) {
                            let contents_img=document.querySelectorAll("#contents_img_four");

                            contents_img.forEach((Delicious,index)=>{
                                delicious_image[index]=Delicious;
                            })
                            let blob=new Blob([response], {type:xhr.getResponseHeader('Content-Type')});
                            let url = URL.createObjectURL(blob);

                            let img = document.createElement("img");
                            img.src = url;
                            img.alt = "영화이미지";

                            img.className = "img";

                            delicious_image[index].appendChild(img);
                            delicious_image[index].querySelector(".loader").remove();
                            setTimeout(() => {
                                URL.revokeObjectURL(url);
                            }, 100);

                        },  error: function (xhr, status, error) {
                            console.error("Error fetching image:", error);
                            $(".loader").remove(); // 로딩 스피너 제거
                            console.log(11111)
                        }
                    })
                })
            }
        }
    })

})