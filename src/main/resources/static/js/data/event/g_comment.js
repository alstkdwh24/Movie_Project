document.addEventListener('DOMContentLoaded', (event) => {
    $("#nickname").text($("#name").text());
    // 닉네임
    let g_number_two = document.getElementById("g_numbers").value;
    console.log(g_number_two);
    event.preventDefault();

    $.ajax({
        type: "GET", // 대문자로 변경
        contentType: "application/json", // 대문자로 변경
        url: "/g_comment", // URL에 슬래시 추가
        data: { g_number: g_number_two }, // 쿼리 파라미터로 전달
        success: function (data) {
            create_g_comment(data);
        },
        error: function (xhr, status, error) { // 에러 시 추가 정보 출력
            alert("에러가 발생하였습니다.");
            console.log(xhr.responseText); // 서버의 에러 메시지 출력
        }
    });
});
    function create_g_comment(data) {
      let  g_comment=''
        data.forEach(function (result) {
            g_comment += '<div class="talk">' +
                '<div id="talk_img">' +
                '<input type="image" alt="" id="input_img">' +
                '</div>' +
                '<div id="talk_contents">' +
                '<div id="talk_contents_title">' +
                ' <div class="id_date">' +
                ' <div class="id_date">닉네임</div>' +
                '<div class="id_date" data-g_number=\'' + JSON.stringify(result) + '\'>' + result.nickname + '</div>' +


                '        </div>' +
                ' <div class="id_date">' +
                ' <div class="id_date">작성날짜</div>' +
                '<div class="id_date"> + result.comment_date + </div>' +
                '</div>' +
                '</div>' +
                ' <div id="talk_contents_content">' +
                result.comment +
                ' </div>' +
                '<button id="comment_submit">댓글달기</button>' +
                ' </div>'
        });
      $(".comment_two").append(g_comment);
    }



document.getElementById("nickname").textContent = document.getElementById("name").textContent
let nickname = document.getElementById("nickname").textContent
let comment_date_two = document.getElementById("comment_date").value;


let g_numbers = document.getElementById("g_numbers").value;
let comment_submit = document.getElementById("comment_submit");
comment_submit.onclick = function () {
    // textarea의 값을 가져오기
    let comment_write = document.getElementById("textareas").value;

    // comment_write 출력
    console.log(comment_write);
    $.ajax({
        type: "post",
        contentType: "application/JSON",
        url: "/g_resist",
        data: JSON.stringify({
            nickname: nickname,
            comment_date: comment_date_two,
            comment: comment_write,
            g_number: g_numbers
        }),
        success: function () {
            alert("성공했습니다.");
        },
        error: function () {
            alert("잘못되었습니다.")
        }
    })
}