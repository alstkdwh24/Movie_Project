document.addEventListener('DOMContentLoaded', (event) => {
    let contents_img_two_one = [];
    let imageUrl = [];
    let filePath = [];
    let Gifticon_name = [];
    let Gifticon_filename = [];

    $.ajax({
        type: "get",
        url: "/movie_resist/movie_gifticon_list",
        contentType: "application/json",
        success: function (data) {


            let contents_img = document.querySelectorAll("#contents_img");

            contents_img.forEach((contents_img_two, index) => {
                contents_img_two_one[index] = contents_img_two;
                console.log(contents_img_two_one[index], "contents_img_twos[index]");

            });
            if (Array.isArray(data) && data.length > 0) {
                data.forEach((item, index) => {

                    console.log(item.gifticon_filename);
                    Gifticon_filename[index] = item.gifticon_filename;
                    console.log(Gifticon_filename[index], "Gifticonfile_name[index]")
                    imageUrl[index] = item.imageUrl;

                    console.log("imageUrl[index]", imageUrl[index]);

                    filePath[index] = item.filePath;

                    Gifticon_name[index] = item.gifticon_name;
                    console.log("Gifticon_name[index]", Gifticon_name[index]);


                    console.log("item.GifticonHtml", item.htmlContent); // 추가된 부분
                    $("#ant_threes").append(item.htmlContent); // HTML 추가
                    console.log(item.htmlContent, "item.GifticonHtml"); // 추가된 부분
                    console.log(item.htmlContent, "item.GifticonHtml"); // 추가된 부분

                    $.ajax({
                        type: "get",
                        url: `/movie_resist/movie_gifticon_list/file/${filePath[index]}/${Gifticon_filename[index]}`,
                        xhrFields: {
                            responseType: "blob" // Blob 형식으로 응답 받기
                        },
                        success: function (response, status, xhr) {
                            let ant_three = document.querySelectorAll("#contents_img");
                            ant_three.forEach((ant_three_two, index) => {
                                contents_img_two_one[index] = ant_three_two;
                            })

                            let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});

                            let url = URL.createObjectURL(blob);

                            let img = document.createElement("img");
                            img.src = url;
                            img.alt = "영화이미지";

                            img.className = "img";

                            contents_img_two_one[index].appendChild(img);
                            contents_img_two_one[index].querySelector(".loader").remove(); // 로딩 스피너 제거
                            setTimeout(() => {
                                URL.revokeObjectURL(url);
                            }, 100);
                        }
                    })

                })
            }
        }
    })


});