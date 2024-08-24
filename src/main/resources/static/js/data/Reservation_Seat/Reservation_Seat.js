// 전역 변수로 데이터 관리
let movieTitle = movieDataArray[0];
let moviePlace = movieDataArray[1];
let movieTime = movieDataArray[2];

function Movie_reservation_modal(e) {
    console.log(1);
    e.preventDefault();
    reservation_two.style.display = "flex"; // 예약 모달 표시
    console.log(movieDataArray);
    // 영화 제목 클릭 이벤트 설정

}

// reservation_two.onclick=function (){
//     reservation_two.style.display="none";
// }

function button() {
    // 서버에 예약 요청
    $.ajax({
        url: "/resist_reservation",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            movieTitle: movieTitle,
            moviePlace: moviePlace,
            movieTime: movieTime
        }),
        success: function (response) {
            alert(`예약이 성공적으로 처리되었습니다:\n영화 제목: ${movieTitle}\n장소: ${moviePlace}\n시간: ${movieTime}`);
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert(`예약 처리 중 오류가 발생했습니다.\n오류 코드: ${xhr.status}`);
        }
    });
}
