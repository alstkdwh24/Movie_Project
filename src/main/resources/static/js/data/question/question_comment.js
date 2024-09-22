document.addEventListener('DOMContentLoaded', (event) => {
    let Q_number = document.getElementById("gallery_name").textContent;
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: "/movie/chats/question_comment",
        data: {Q_number: Q_number},
        success: function (data) {
            console.log(data)
            alert("성공하였숩니다.");
            create_question(data);
        },
        error: function () {
            alert("수정을 해주세요");
        }
    });
});

function create_question(data) {
    let Question = '';
    data.forEach(function (result) {
        Question += '<div id="talk_writer_free">' +

            '  <div id="talk_img_two">' +
            ' <input type="image" alt="" id="input_img_two">' +
            '</div>' +
            ' <div id="talk_contents_two">' +
            ' <div id="talk_contents_title_two"> ' +
            ' <div class="Question_comment_title">' +
            '                                <div class="Question_id">닉네임</div>' +
            '                                <div class="Question_id" data-Question=\'' + JSON.stringify(result) + '\'>' + result.nickname + '</div>' +
            '                            </div>' +
            '                            <div class="Question_comment_title">' +
            '                                <div class="Question_id">글쓴날짜</div>' +
            '                                <div class="Question_id">' + result.comment_date + '</div>' +
            '                            </div></div>' +
            ' <div id="talk_contents_content_two">' + result.comment + '</div>' +
            ' <div id="notice_two"></div>' +
            ' </div>' +
            '</div>'
    });
    $("#comment_dox").append(Question);
}
let comment_date_two = new Date().toISOString(); // 현재 날짜와 시간을 ISO 형식으로 설정

let nicknames = document.getElementById("name").textContent;
console.log(nicknames);
let Question_submit = document.getElementById("Question_comment_two");
console.log(Question_submit);

// gallery_name에서 Q_number 추출
let Q_number = parseInt(document.getElementById("gallery_name").textContent, 10);
console.log(Q_number);

let QNumber=document.getElementById("QNumber").value;
Question_submit.onclick = function () {

    let comments = document.getElementById("textareas").value;





    $.ajax({
        type: "POST", // POST 메소드로 설정
        contentType: "application/json",
        url: "/movie/chats/Question_comment_resist",
        data: JSON.stringify({
            nickname: nicknames,
            comment_date: comment_date_two,
            comment: comments,
            q_number: Q_number,
            username: QNumber
        }),
        success: function (response) {
            alert("성공하였습니다");
        },
        error: function (error) {
            console.error("AJAX 요청 실패:", error);
            alert("아쉽습니다.");
        }
    });

}
