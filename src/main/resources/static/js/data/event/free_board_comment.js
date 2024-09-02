

    // 'beforeend'를 추가하여 HTML을 추가
    let userimage=document.getElementById("user_image");
    let textarea = document.getElementById("textarea");
    let write_time = document.getElementById("write");
    let username=document.getElementById("username");
    let username_user=document.getElementById("user_id");
    let talk_button_free=document.getElementById("talk_button_freetwo");
    let datas = {
        comment: textarea.innerText,
        comment_date: write_time.innerText,
        username: username_user.value,
        user_image: userimage.value
    };

    // 함수 호출
talk_button_free.onclick = function (event) {
    event.preventDefault(); // 기본 제출 방지

    $.ajax({
            type: "POST",
            url: "/movie/community/free_board_comments",
            contentType: "application/json",
            // data: JSON.stringify({
            //     comment: textarea.value, // textarea의 값을 가져옵니다.
            //     comment_date: write_time.innerText, // 작성 시간을 가져옵니다 (innerText 사용).
            //     username: username_user.value, // user_id의 값을 가져옵니다.
            //     user_image: userimage.value // user_image의 값을 가져옵니다.
            // }),
            data:JSON.stringify(datas),
            success: function (response) {
                alert("성공하였습니다");
                let url = `/movie/community/free_detail?free_number=${encodeURIComponent(w_c)}`;
                location.href = url; // 페이지 이동
            },
            error: function (response) {
                alert("다시 시도하세요");
                console.log(datas); // 데이터 확인

            }
        });
    }


