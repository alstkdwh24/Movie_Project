document.addEventListener('DOMContentLoaded', (event) => {
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
let gallery_blank_mini2 = document.querySelectorAll(".gallery_blank_mini2")
gallery_blank_mini2.forEach(function (gallery_blank_mini24) {
    gallery_blank_mini24.onclick = function () {
        document.free_detail.action="/movie/communithy/free_detail/vo.free_number" + vo.free_number;
        document.free_detail.method="Post";
        document.free_detail.submit();
    }

});
