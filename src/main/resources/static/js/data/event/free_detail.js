document.addEventListener('DOMContentLoaded', (event) => {
    event.preventDefault();
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

});


console.log(free_numbers);
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

