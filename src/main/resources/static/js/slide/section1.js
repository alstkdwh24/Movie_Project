document.addEventListener('DOMContentLoaded', (event) => {
    let inputs = document.querySelectorAll(".movie_real_image");
    let input_two=[];
    inputs.forEach((inputs,index)=>{
        input_two[index]=inputs;
    })
    let filename;
    let movietitles=[]
    let filePaths=[]
    let filePath = document.querySelectorAll("#filePath");
    filePath.forEach((filePath,index)=>{
        filePaths[index]=filePath.textContent.trim();
    })
    let movie_filename = document.querySelectorAll("#movie_filename");
    let movie_filename_two=[];
    movie_filename.forEach((movie_filename,index)=>{
        movie_filename_two[index]=movie_filename.textContent.trim();
    })
    let movietitle = document.querySelectorAll("#title");
    movietitle.forEach((movie_title, index) => {
         movie_title = movie_title.textContent.trim();
         movietitles[index]=movie_title;

        filename = filePath + "/" + movie_filename + "/" + movietitles[index];
        console.log(index);

            $.ajax({
                type: "get",
                url: `/movie_resist/files/${filePaths[index]}/${movie_filename_two[index]}/${movietitles[index]}`, // 서버 URL 추가
                xhrFields: {
                    responseType: "blob" // Blob 형식으로 응답 받기
                },
                beforeSend: function () {
                },
                success: function (response, status, xhr) {

                    const blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')}); // Blob 생성
                    let url = URL.createObjectURL(blob); // Blob URL 생성

                    let img = document.createElement("img"); // 이미지 요소 생성
                    img.src = url; // 이미지 URL 설정
                    img.alt = "영화이미지"; // alt 속성 설정

                    img.classList = "img"

                    input_two[index].appendChild(img); // 이미지 요소 추가
                    input_two[index].querySelector(".loader").remove(); // 로딩 스피너 제거

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



    })
})
        // console.log(file+ "file24");
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
