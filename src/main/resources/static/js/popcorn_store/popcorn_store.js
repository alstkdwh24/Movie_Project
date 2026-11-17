document.addEventListener('DOMContentLoaded', (event) => {

    let delicious_number = [];

    let delicious_url = [];
    let filePath = [];

    let delicious_filename = [];

    let delicious_name = [];

    let pakage_imgs = [];

    let pakage_img_three = [];
    $.ajax({

        type: "get",
        url: "/movie_resist/DeliciousVO_list_two",
        contentType: "application/json",
        success: function (data) {
            console.log(data);
            if (Array.isArray(data) && data.length > 0) {
                data.forEach(function (item, index) {


                    if (index < 4) {
                        delicious_number[index] = item.delicious_number;
                        console.log("delcious_number[index]", delicious_number[index]);

                        delicious_url[index] = item.delicious_url;
                        console.log("delicious_url[index]", delicious_url[index]);

                        filePath[index] = item.filePath;
                        console.log("filePath[index]", filePath[index]);

                        delicious_filename[index] = item.delicious_filename;
                        console.log("delicious_filename[index]", delicious_filename[index]);

                        delicious_name[index] = item.delicious_name;
                        console.log("delicious_name", delicious_name[index]);


                        $("#pakage_store_popcorn").append(item.delicious_htmlContent);
                        $.ajax({
                            type: "get",
                            url: `/movie_resist/DeliciousVO_list_two/files/${filePath[index]}/${delicious_filename[index]}`,
                            xhrFields: {
                                responseType: "blob" // Blob 형식으로 응답 받기
                            },
                            success: function (response, status, xhr) {
                                let pakage_img = document.querySelectorAll("#pakage_imgs");
                                pakage_img.forEach((pakage_img_two, index) => {
                                    pakage_imgs[index] = pakage_img_two;
                                })
                                let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});

                                let url = URL.createObjectURL(blob);

                                let img = document.createElement("img");

                                img.src = url;
                                img.alt = "영화이미지"

                                img.className = "img";

                                pakage_imgs[index].appendChild(img);
                                pakage_imgs[index].querySelector(".loader").remove();
                            }
                        })
                    }


                });


            }
        }

    })

    $.ajax({
        type: "get",
        url: "/movie_resist/DeliciousVO_list_three",
        contentType: "application/json",
        success: function (data) {
            if (Array.isArray(data) && data.length > 0) {
                data.forEach(function (item, index) {


                    if (4 <= index && index < 8) {
                        delicious_number[index] = item.delicious_number;
                        console.log("delcious_number[index]", delicious_number[index]);

                        delicious_url[index] = item.delicious_url;
                        console.log("delicious_url[index]", delicious_url[index]);

                        filePath[index] = item.filePath;
                        console.log("filePath[index]", filePath[index]);

                        delicious_filename[index] = item.delicious_filename;
                        console.log("delicious_filename[index]", delicious_filename[index]);

                        delicious_name[index] = item.delicious_name;
                        console.log("delicious_name", delicious_name[index]);


                        $("#pakage_store_combo").append(item.delicious_htmlContent);


                        $.ajax({
                            type: "get",
                            url: `/movie_resist/DeliciousVO_list_three/files/${filePath[index]}/${delicious_filename[index]}`,
                            xhrFields: {
                                responseType: "blob"
                            },
                            success: function (response, status, xhr) {
                                let pakage_img_threes = document.querySelectorAll("#pakage_imgs");
                                pakage_img_threes.forEach((pakage_img_three_one, index) => {
                                    pakage_img_three[index] = pakage_img_three_one;
                                })
                                let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});
                                let url = URL.createObjectURL(blob);

                                let img = document.createElement("img");
                                img.src = url;
                                img.alt = "영화이미지"

                                img.className = "img";

                                pakage_img_three[index].appendChild(img);
                                pakage_img_three[index].querySelector(".loader").remove();
                            }
                        })
                    }


                });


            }


        }
    })

    let gifticon_name = [];
    let gifticon_filename = [];
    let filePath_two = [];
    let imageUrl = [];
    let gift_img_two = [];
    $.ajax({
        type: "get",
        url: "/movie_resist/gifticonVO_Responses_two_list",
        contentType: "application/json",
        success: function (data) {

            if (Array.isArray(data) && data.length) {
                data.forEach(function (item, index) {
                    if (index < 3) {
                        gifticon_filename[index] = item.gifticon_filename;
                        console.log("gifticon_filename[index]", gifticon_filename[index]);

                        imageUrl[index] = item.imageUrl
                        console.log("imageUrl[index]", imageUrl[index]);

                        filePath_two[index] = item.filePath;
                        console.log("filePath_two[index]", filePath_two[index]);

                        gifticon_name[index] = item.gifticon_name;

                        $(".card_body_1").append(item.imageUrl);
                        $.ajax({
                            type: "get",
                            url: `/movie_resist/gifticonVO_Responses_two_list/files/${filePath_two[index]}/${gifticon_filename[index]}`,
                            xhrFields: {
                                responseType: "blob"
                            },
                            success: function (response, status, xhr) {
                                let gift_img = document.querySelectorAll(".gift_img")
                                gift_img.forEach((gift_img_twos, index) => {
                                    gift_img_two[index] = gift_img_twos;
                                })

                                let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});
                                let url = URL.createObjectURL(blob);

                                let img = document.createElement("img");


                                img.src = url;
                                img.alt = "영화 이미지";

                                img.className = "img";


                                gift_img_two[index].appendChild(img);
                                gift_img_two[index].querySelector(".loader").remove();
                            }
                        })

                    }

                })

            }
        }
    })

    let movie_ticket_url_two=[];
    let filePath_ticket=[];
    let movie_ticket_filename_array=[];
    let movie_filepath=[];
    let movie_ticket_name=[];
    let ticket_img_two_one=[];
    $.ajax({
        type: "get",
        url: "/movie_resist/popcon_store/movie_ticket_resist_list_two",
        contentType: "application/json",
        success: function (data) {
            if (Array.isArray(data) && data.length > 0) {
                data.forEach((item, index) => {
                    if (index < 3) {

                        movie_ticket_url_two[index] = item.movie_ticket_url;
                        filePath_ticket[index] = item.filePath;
                        movie_ticket_filename_array[index] = item.movie_ticket_filename;
                        movie_filepath[index] = item.movie_filepath;
                        movie_ticket_name[index] = item.movie_ticket_name;

                        $(".card_body").append(item.movie_ticket_htmlContent);

                        $.ajax({
                            type:"get",
                            url:`/movie_resist/popcon_store/movie_ticket_resist_list_two/files/${filePath_ticket[index]}/${movie_ticket_filename_array[index]}`,
                            xhrFields: {
                                responseType: "blob"
                            },
                            success:function (response,status,xhr) {
                                let ticket_img=document.querySelectorAll(".gift_img_two");
                                ticket_img.forEach((ticket_img_two,index)=>{
                                    ticket_img_two_one[index]=ticket_img_two;
                                })

                                let blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')});
                                let url = URL.createObjectURL(blob);

                                let img = document.createElement("img");


                                img.src = url;
                                img.alt = "영화 이미지";

                                img.className = "img";

                                ticket_img_two_one[index].appendChild(img);
                                ticket_img_two_one[index].querySelector(".loader").remove();
                            }
                        })
                    }

                })
            }
        }

    })
})