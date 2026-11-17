document.addEventListener('DOMContentLoaded', (event) => {
    const talk_button_update = document.getElementById("talk_button_update_two");
    talk_button_update.onclick = function () {
        document.free_detail_two.action = "free_detail_updates";
        document.free_detail_two.method = "get"
        document.free_detail_two.submit();

    }
})
let gallery_blank_mini2 = document.querySelectorAll(".gallery_blank_mini2");
gallery_blank_mini2.forEach(function (gallery_blank_mini24) {
    gallery_blank_mini24.onclick = function () {
        document.free_detail_two.action = "/movie/community/free_detail/" + vo.free_number;
        document.freeboard.method="Post";
        document.freeboard.submit();

    }

});
