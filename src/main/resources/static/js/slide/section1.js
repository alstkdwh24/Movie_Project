document.addEventListener('DOMContentLoaded', (event) => {
    const inputs = document.querySelectorAll('input[id^="staticImageUrlInput_"]').values();
    inputs.forEach(filenames => {


        let filename=filenames.value;
        // let filename = $(this).data('image-url'); // data-image-url 속성에서 URL 가져오기
        console.log(filename);
        $.ajax({
            type: "get",
            url: `/movie/files/${filename}`, // 서버 URL 추가
            xhrFields: {
                responseType: "blob" // Blob 형식으로 응답 받기
            },
            beforeSend: function () {
                $(".movie_real_image").append('<div class="loader">Loading...</div>'); // 로딩 스피너 추가
            },
            success: function (response, status, xhr) {
                const blob = new Blob([response], {type: xhr.getResponseHeader('Content-Type')}); // Blob 생성
                let url = URL.createObjectURL(blob); // Blob URL 생성

                let html = `<img src="${url}" alt="영화이미지">`;
                $(".movie_real_image").append(html); // 이미지 추가

                // 로딩 스피너 제거
                $(".loader").remove();
            },
            error: function (xhr, status, error) {
                console.error("Error fetching image:", error);
                $(".loader").remove(); // 로딩 스피너 제거
            }
        });
    });


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