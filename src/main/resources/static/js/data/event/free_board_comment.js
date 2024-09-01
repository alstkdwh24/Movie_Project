function free_comment() {
    let answer = '<div class="talk">' +
        '<div id="talk_img">' +
        '    <input type="image" alt="프로필 이미지" id="input_img">' +
        '</div>' +
        '<div id="talk_contents">' +
        '    <div id="talk_contents_title"><div id="id"><div id="ids">아이디</div><div id="username"></div></div> <div id="write_time"><div id="write">작성시간</div><div id="comment_date"></div></div></div>' +
        '    <div id="talk_contents_content"><label id="label">' +
        '        <textarea id="textarea"></textarea>' +
       ' <button id="talk_button_free">댓글</button>'+
        '    </div></label>' +

        '</div>' +
        '</div>';

    // 'beforeend'를 추가하여 HTML을 추가
    document.querySelector(".coment").insertAdjacentHTML('afterbegin', answer);
}

let textarea = document.getElementById("textarea");
let write_time = document.getElementById("write_time");

// 함수 호출
free_comment();

$.ajax({
    type: "POST",
    url: "/free_board_comment",
    contentType: "application/json",
    data: JSON.stringify({
        coment: textarea,
        coment_date: write_time
    })
})
