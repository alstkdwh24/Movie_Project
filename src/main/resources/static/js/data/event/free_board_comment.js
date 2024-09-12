document.addEventListener('DOMContentLoaded', (event) => {
    // 댓글 불러오기 기능
    let someFreeNumber = document.getElementById("freenumber").value;
    event.preventDefault();
    $.ajax({
        type: "get",
        contentType: "application/json",
        data: {free_number: someFreeNumber}, // someFreeNumber는 실제 free_number 값

        url: "/get_comment",
        success: function (data) {
            create_comment(data);
        },
        error: function (err, status) {
            alert("불러오는데 실패하였습니다.")
        }
    });
    const talk_button_update = document.getElementById("talk_button_update");
    talk_button_update.onclick = function () {
        document.free_detail.action = "free_detail_update";
        document.free_detail.submit();
        document.free_detail.method = "get"
    }

    const talk_button_cancel = document.getElementById("talk_button_cancel");
    talk_button_cancel.onclick = function () {
        document.free_detail.action = "talk_button_cancel";
        document.free_detail.submit();
        alert("글이 지워졌습니다.")
    }

    let gallery_blank_mini2 = document.querySelectorAll(".mini_title_good");
// gallery_blank_mini2.forEach(function (gallery_blank_mini24) {
//     gallery_blank_mini24.onclick = function () {
//         let vo_free_number = document.getElementById("writer_number2").textContent;
//
//         document.freeboard.action = `/movie/community/free_detail?free_number=${vo_free_number}`;
//         document.freeboard.method = "POST"; // 메서드를 POST로 설정
//         document.freeboard.submit(); // 폼 제출
//     }
// });


    function create_comment(data) {
        console.log(data)
        let movie_comment_list = ''
        ;
        data.forEach(function (result) {
            movie_comment_list += '<div id="talk_writer_free" >' +

                '<div id="talk_id">' +
                '<input type="image" alt="프로필 이미지" id="input_img_two">' +
                '</div>' +
                '<div id="talk_comments">' +
                '<div id="talk_contents_title_two">' +
                '  <div id="id_two">' +
                '<div id="ids_two">아이디</div>' +
                '<div id="username_two" data-freeid=\'' + JSON.stringify(result) + '\'>' + result.username + '</div>' + // 작은 따옴표 사용
                ' </div> ' +
                '<div id="write_time_two">' +
                '<div id="write_two">작성시간</div>' +
                '<div id="comment_date_two">' + result.comment_date + '</div>' +
                '</div>' +
                '</div>' +
                ' <div id="talk_contents_content">' + result.comment + '<label id="label">' +

                '</label>' +
                '</div>' +
                '</div>' +
                ' </div>'
        });
        $("#comment_two").append(movie_comment_list);
        console.log(movie_comment_list)
    }
});
// 'beforeend'를 추가하여 HTML을 추가 댓글 쓰기 기능들

let userimage = document.getElementById("user_image");
let textarea = document.getElementById("textarea");
let write_time = document.getElementById("comment_date");
let username_user = document.getElementById("user_ids");

let hidden_free_number = document.getElementById("hidden_free_number");
let hidden_free_numbers = hidden_free_number.value;

let hi = document.getElementById("gallery_name");
console.log(hi.textContent)
let his = hi.textContent
console.log("   his" + his
)
let username = username_user.value;
console.log("username:", username);
let talk_button_free = document.getElementById("talk_button_free_two");

console.log("free_number");


// 함수 호출
talk_button_free.onclick = function (event) {
    event.preventDefault(); // 기본 제출 방지

    $.ajax({
        type: "POST",
        url: "/free_board_comments",
        contentType: "application/json",
        data: JSON.stringify({
            comment: textarea.value, // textarea의 값을 가져옵니다.
            comment_date: write_time.innerText, // 작성 시간을 가져옵니다 (innerText 사용).
            username: username_user.value, // user_id의 값을 가져옵니다.
            user_image: userimage, // user_image의 값을 가져옵니다.
            free_number: his
        }),
        // data:JSON.stringify(data),
        success: function (response) {
            alert("성공하였습니다");
            let url = `/movie/community/free_detail?free_number=${encodeURIComponent(his)}`;
            location.href = url; // 페이지 이동
        },
        error: function (response) {
            alert("다시 시도하세요");
            console.log(data); // 데이터 확인

        }
    });
}


