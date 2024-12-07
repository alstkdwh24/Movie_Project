document.addEventListener("DOMContentLoaded", event => {
    let movie_image_notice_three = [];
    let imageUrl = [];
    let filePath = [];
    let movie_filename = [];
let movie_image=[];

    $.ajax({
        type: "get",
        url: "/movie_resist/event_list_two",
        contentType: "application/json",
        success: function (data) {
            console.log(data); // 전체 데이터 로그
     let movie_image_notice = document.querySelectorAll("#movie_image_notice"); // 요소 배열로 변환
        movie_image_notice.forEach((movie_images,index)=>{
            movie_image[index]=movie_images;
            console.log(movie_image[index]);
        })

            if (Array.isArray(data) && data.length > 0) {


                data.forEach(function (item, index) {



                    imageUrl[index] = item.imageUrl;
                    console.log("imageUrl[index]" + imageUrl[index])
                    filePath[index] = item.filePath;
                    console.log("filePath[index]", filePath[index])
                    movie_filename[index] = item.movie_filename;
                    console.log("movie_filename[index]", movie_filename[index])
                    // 각 ResponseVO에서 HTML 콘텐츠를 가져와서 추가
                    $("#good_movies_title_bottom").append(item.htmlContent); // htmlContent 속성 사용
                    $.ajax({
                        type: "get",
                        url: `/movie_resist/event_list_two/files/${filePath[index]}/${movie_filename[index]}`,
                        xhrFields: {
                            responseType: "blob" // Blob 형식으로 응답 받기
                        },
                        success: function (response, status, xhr) {
                            let movie_image_notice = document.querySelectorAll("#movie_image_notice"); // 요소 배열로 변환
                            movie_image_notice.forEach((movie_image,index)=>{

                            })
                            let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});
                            let url = URL.createObjectURL(blob);

                            let img = document.createElement("img");
                            img.src = url;
                            img.alt = "영화이미지"

                            img.className = "img";

                            movie_image_notice[index].appendChild(img);
                            movie_image_notice[index].querySelector(".loader").remove(); // 로딩 스피너 제거
                            setTimeout(() => {
                                URL.revokeObjectURL(url);
                            }, 100);
                        },
                        error: function (xhr, status, error) {
                            console.error("Error fetching image:", error);
                            $(".loader").remove(); // 로딩 스피너 제거
                            console.log(11111)
                        }
                    })

                });
            } else {
                $("#good_movies_title_bottom").html("<p>이벤트가 없습니다.</p>"); // 이벤트가 없을 경우 메시지 표시
            }
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청에 실패했습니다:", error);
        }
    });
});
