document.addEventListener('DOMContentLoaded', (event) => {
    let filePath = document.querySelectorAll(".filePath");
    let movie_filename = document.querySelectorAll(".movie_filename");

    let filePath_two = [];
    let movie_filename_tow = [];
let filename=[];
    filePath.forEach((filePaths, index) => {

        filePath_two[index] = filePaths.textContent;
    })
    movie_filename.forEach((movie_filenames, index) => {
        movie_filename_tow[index] = movie_filenames.textContent;
    })
    for (let i = 0; i < filePath_two.length; i++) {
      filename[i] = filePath_two[i] + "/"+movie_filename_tow[i];


        console.log("filePath_two", filePath_two, "movie_filename_tow", movie_filename_tow)


    }
    for(let i=0;i<filename.length;i++) {
        let file=filename[i];




        let filenames = encodeURIComponent(file.replace(/\s+/g, '')); // URL 인코딩

        let parts=filenames.split("/");
        let date=parts[0];
        let date2=date.split("/")
        let first_date2=date2[0]
        let two_date2=date2[1];
        let fileName=parts[1];
        let finalFilename = first_date2;
        finalFilename = finalFilename.replace("%2F", "/");
        finalFilename=finalFilename.split("/");
        let finalFilename_two=finalFilename[1]
        let finalFilename_one=finalFilename[0]
        console.log("filename24"+finalFilename);


        // console.log(file+ "file24");
        $.ajax({
            type: "get",
            url: `/movie_resist/files/${finalFilename_one}/${finalFilename_two}`, // 서버 URL 추가
            xhrFields: {
                responseType: "blob" // Blob 형식으로 응답 받기
            },
            beforeSend: function () {
                $(".movie_real_image").append('<div class="loader">Loading...</div>'); // 로딩 스피너 추가
            },
            success: function (response, status, xhr) {

                const blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')}); // Blob 생성
                let url = URL.createObjectURL(blob); // Blob URL 생성

                let img = document.createElement("img"); // 이미지 요소 생성
                img.src = url; // 이미지 URL 설정
                img.alt = "영화이미지"; // alt 속성 설정
                img.style.width="100%";
                img.style.height="100%";
                $(".loader").remove(); // 로딩 스피너 제거

                let movie_real_image = document.querySelector(".movie_real_image");
                movie_real_image.appendChild(img); // 이미지 요소 추가
                setTimeout(() => {
                    URL.revokeObjectURL(url);
                }, 100);
            },
            error: function (xhr, status, error) {
                console.error("Error fetching image:", error);
                $(".loader").remove(); // 로딩 스피너 제거
                console.log(11111)
            }
        });
    }
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