// 'beforeend'를 추가하여 HTML을 추가
let userimage = document.getElementById("user_image");
let textarea = document.getElementById("textarea");
let write_time = document.getElementById("write");
let username_user = document.getElementById("user_ids");

let hidden_free_number=document.getElementById("hidden_free_number");
let hidden_free_numbers=hidden_free_number.value;

let hi = document.getElementById("gallery_name");
console.log(hi.textContent)
let his = hi.textContent
console.log("   his" + his
)
let username = username_user.value;
console.log("username:", username);
let talk_button_free = document.getElementById("talk_button_freetwo");

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


