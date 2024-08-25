// 카테고리 선택을 처리하는 함수
function getMovieCategory_List(event) {

    // 클릭한 요소의 data-set 속성에서 영화 정보를 가져옴
    const dataSet = $(event.target).attr('data-set');
    if (!dataSet) {
        console.error("data-set 속성이 없습니다.");
        return; // data-set 속성이 없으면 함수 종료
    }

    let movieData;
    try {
        movieData = JSON.parse(dataSet);
    } catch (error) {
        console.error("JSON 파싱 오류:", error);
        return; // JSON 파싱 오류 발생 시 함수 종료
    }

    // 카테고리 레벨에 따라 클래스 추가 및 제거
    const categoryLevel = movieData.movie_category_lv;
    const classNames = ['sub_menu_select', 'sub_menu_select2', 'sub_menu_select3'];

    if (categoryLevel >= 1 && categoryLevel <= 3) {
        $(event.currentTarget).find("div").removeClass(classNames.join(' ')); // 모든 클래스 제거
        $(event.target).addClass(classNames[categoryLevel - 1]); // 해당 카테고리 클래스 추가
    } else {
        console.error("유효하지 않은 movie_category_lv:", categoryLevel);
    }
}


function Movie_reservation_modal(event) {
    console.log(1);
    event.preventDefault();
    reservation_two.style.display="flex"
    document.getElementById("movieTitleInput").value = document.querySelector(".movie_name.movie_title246.sub_menu_select").textContent;




// 영화 장소 가져오기
    document.getElementById("moviePlaceInput").value = document.querySelector(".movie_name.movie_place246.sub_menu_select2").textContent;




// 영화 시간 가져오기
    document.getElementById("movieTimeInput").value = document.querySelector(".movie_name.movie_time246.sub_menu_select3").textContent;
// 의자 클릭 이벤트 설정

}

// 모달을 여는 함수

// 모달 닫기




let chair = document.querySelectorAll(".chair");


chair.forEach(function (chairElement) {


    chairElement.addEventListener('click', function () {

        let movieTimeInput=document.getElementById("movieTimeInput").value;
        let moviePlaceInput=document.getElementById("moviePlaceInput").value;
        let movieTitleInput=document.getElementById("movieTitleInput").value;
        let movie_Seat=document.querySelector(".input").value;
        $.ajax({
            type: "post",
            url: "/resist_reservation",
            contentType: "application/json",
            data: JSON.stringify({
                movieTitle: movieTitleInput,
                moviePlace: moviePlaceInput,
                movieTime: movieTimeInput,
                movie_Seat:movie_Seat
            }),
            success: function (response) {
                alert("성공하였습니다");
            },
            error: function (response) {
                alert("다시 시도하세요");
            }
        });
    });
});
reservation_two.onclick = function () {
    reservation_two.style.display = "none";
}
