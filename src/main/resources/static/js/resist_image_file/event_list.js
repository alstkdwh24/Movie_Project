document.addEventListener("DOMContentLoaded", event => {
    $.ajax({
        type: "get",
        url: "/movie_resist/event_list_two",
        contentType: "application/json",
        success: function (data) {
            console.log(data); // 전체 데이터 로그

            // 기존 내용을 비우고 새로운 HTML 추가
            $("#good_movies_title_bottom").empty();

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(function (item) {
                    let uploadPaths=item.uploadPaths;
                    // 각 ResponseVO에서 HTML 콘텐츠를 가져와서 추가
                    $("#good_movies_title_bottom").append(item.htmlContent); // htmlContent 속성 사용
               $.ajax({
                   type:"get",
                   url:`/movie_resist/event_list_two/${uploadPaths}`,

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
