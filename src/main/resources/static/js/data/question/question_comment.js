// document.addEventListener('DOMContentLoaded', (event) => {
//
//
//     $.ajax({
//         type: "get",
//         contentType: "application/json",
//         url: "/question_comment",
//         success: function (data) {
//             alert("성공하였숩니다.");
//             create_question(data);
//         },
//         error: function () {
//             alert("수정을 해주세요");
//         }
//     });
//
// });
//
// function create_question(data) {
//     let Question = '';
//     data.forEach(function (result) {
//         Question += '<div id="talk_writer_free">' +
//
//             '  <div id="talk_img_two">' +
//             ' <input type="image" alt="" id="input_img_two">' +
//             '</div>' +
//             ' <div id="talk_contents_two">' +
//             ' <div id="talk_contents_title_two"> ' +
//             ' <div class="Question_comment_title">' +
//             '                                <div class="Question_id">닉네임</div>' +
//             '                                <div class="Question_id" data-Question=\'' + JSON.stringify(result) + '\'>' + result.nickname + '</div>' +
//             '                            </div>' +
//             '                            <div class="Question_comment_title">' +
//             '                                <div class="Question_id">글쓴날짜</div>' +
//             '                                <div class="Question_id">' + result.comment_date + '</div>' +
//             '                            </div></div>' +
//             ' <div id="talk_contents_content_two">' + result.comment + '</div>' +
//             ' <div id="notice_two"></div>' +
//             ' </div>' +
//             '</div>'
//     });
//     $("#comment_dox").append(Question);
// }


let nicknames = document.getElementById("name").textContent;
console.log(nicknames)

let Question_submit = document.getElementById("Question_comment_two");
console.log(Question_submit);
Question_submit.onclick = function () {

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/Question_comment_resist",
        data: JSON.stringify({
            nickname: nicknames,
            comment_date: comment_date_two,
            comment: comment_submit
        }),
        success: function (response) {
            alert("성공하였습니다")
        }, error:
            function (error) {
                alert("아쉽습니다.")
            }

    });
}