document.addEventListener('DOMContentLoaded', (event) => {
    // 수정한거 DB에 등록하는 기능
    let button_submit = document.querySelector(".bottom_submit");

    button_submit.onclick = function (event ) {
        event.preventDefault(); // 기본 버튼 클릭 동작 방지

        document.free_board_writer.method = "Post"
        document.free_board_writer.action = "free_board_writer_update_two"

        document.free_board_writer.submit();
        alert("글이 수정되었습니다.")
    }
//     글 수정 취소하는 기능
    let bottom_close=document.querySelector(".bottom_close");
    bottom_close.onclick=function (event) {
        event.preventDefault(); // 기본 버튼 클릭 동작 방지

        let free_number = document.getElementById("free_number").value; // Hidden Input에서 free_number 값 가져오기
        document.free_board_writer.method = "GET"; // 요청 메서드 설정
        document.free_board_writer.action = `turn_back/${free_number}`; // action에 free_number 포함
        document.free_board_writer.submit(); // 폼 제출
    }

});
