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

    // if (categoryLevel >= 1 && categoryLevel <= 3) {
    //     $(event.currentTarget).find("div").removeClass(classNames.join(' ')); // 모든 클래스 제거
    //     $(event.target).addClass(classNames[categoryLevel - 1]); // 해당 카테고리 클래스 추가
    // } else {
    //     console.error("유효하지 않은 movie_category_lv:", categoryLevel);
    // }
}


function Movie_reservation_modal(event) {
    console.log(1);
    event.preventDefault();
    reservation_two.style.display = "flex"
    document.getElementById("movieTitleInput").value = document.querySelector(".movie_name.movie_title246.sub_menu_select").textContent;


// 영화 장소 가져오기
    document.getElementById("moviePlaceInput").value = document.querySelector(".movie_name.movie_place246.sub_menu_select2").textContent;


// 영화 시간 가져오기
    document.getElementById("movieTimeInput").value = document.querySelector(".movie_name.movie_time246.sub_menu_select3").textContent;
// 의자 클릭 이벤트 설정
    document.getElementById("username_modal").value = document.getElementById("username").value
}

// 모달을 여는 함수

// 모달 닫기


let seat_resist = document.getElementById("seat_resist");
let chair = document.querySelectorAll(".chair");


chair.forEach(function (chairElement) {


    chairElement.addEventListener('click', function () {


        document.getElementById("chair_value").value = chairElement.getAttribute('value'); // 클릭된 의자 값 가져오기

        seat_resist.style.display = "flex"


    });
});




seat_resist.onclick = function () {
    console.log("세션이 만료되었습니다.")


    let movieTimeInput = sessionStorage.getItem("movie_time");
    if(movieTimeInput){
        let{movie_Time,expiry}=JSON.parse(movieTimeInput);
        let now = Date.now();
        if(now < expiry){
            sessionStorage.setItem("time", movie_Time);
        }else{
            sessionStorage.removeItem("movie_time");
            console.log("세션이 만료되었습니다.");
        }
    }

    let moviePlaceInput = sessionStorage.getItem("movie_place");

    if (moviePlaceInput) {
        let {Place_Input, expiry} = JSON.parse(moviePlaceInput);
        let now = Date.now();
        if(now < expiry) {
          console.log("성공")
            sessionStorage.setItem("place",Place_Input)

        } else {
            sessionStorage.removeItem("movie_place");
            console.log("세션이 만료되었습니다.");
        }
    }
    let time=sessionStorage.getItem("time")
    let place=sessionStorage.getItem("place")
    console.log(moviePlaceInput)
    let movieTitleInput = document.getElementById("movieTitleInput").value;
    let movieSeatInput = document.getElementById("chair_value").value
    let usernames = document.getElementById("username_modal").value
    let reservation_prices = document.getElementById("reservation_price").value;
    document.getElementById('paymentId').value = Math.floor(Math.random() * 1000000000).toString();
    let paymentId = document.getElementById('paymentId').value;
    console.log(reservation_prices);
    if (movieTimeInput != null && moviePlaceInput != null && movieTitleInput != null && movieSeatInput != null) {
        // 가격
        $.ajax({
            type: "post",
            url: "/resist_reservation",
            contentType: "application/json",
            data: JSON.stringify({
                movieTitle: movieTitleInput,
                moviePlace: place,
                movieTime: time,
                movieSeat: movieSeatInput,
                username: usernames,
                reservation_price: reservation_prices,
                email: "alstkdwh24@naver.com",
                phone: "01043324254",
                paymentId: paymentId
            }),
            success: function (response) {
                location.href = "/movie/Reservation/reservation_report"
                alert("성공하였습니다");
                sessionStorage.removeItem("place");
                sessionStorage.removeItem("time")
            },
            error: function (response) {
                alert("다시 시도하세요");
                sessionStorage.removeItem("place");
                sessionStorage.removeItem("time")
            }
        });
    } else {
        console.log("여기요")
        location.href = "/movie/Reservation/reservation"
    }
}