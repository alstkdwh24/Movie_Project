function Movie_reservation_modal(e) {
    reservation_two.style.display = "flex";
    e.preventDefault();

    const clickedChair = e.target.closest('.chair');
    if (!clickedChair) return;

    const movieElement = clickedChair.querySelector('#movie_title246');
    const movieElement2 = clickedChair.querySelector('#movie_place246');
    const movieElement3 = clickedChair.querySelector('#movie_time246');

    const movieData = movieElement ? JSON.parse(movieElement.dataset.set) : null;
    const movieData2 = movieElement2 ? JSON.parse(movieElement2.dataset.set) : null;
    const movieData3 = movieElement3 ? JSON.parse(movieElement3.dataset.set) : null;

    if (movieData && movieData2 && movieData3) {
        setInputValue(".input_movie_title", movieData.movie_detail_title);
        setInputValue(".input_movie_place", movieData2.movie_detail_title);
        setInputValue(".input_movie_time", movieData3.movie_detail_title);

        const MovieTitleDatas = getInputValue(".input_movie_title");
        const MoviePlaceDatas = getInputValue(".input_movie_place");
        const MovieTimeDatas = getInputValue(".input_movie_time");

        setInputValue("#movieTitleInput", MovieTitleDatas);
        setInputValue("#moviePlaceInput", MoviePlaceDatas);
        setInputValue("#movieTimeInput", MovieTimeDatas);

        console.log("전송할 데이터:", {
            movieTitle: MovieTitleDatas,
            moviePlace: MoviePlaceDatas,
            movieTime: MovieTimeDatas
        });
    }
}

function setInputValue(selector, value) {
    document.querySelector(selector).value = value || '';
}

function getInputValue(selector) {
    return document.querySelector(selector).value;
}

document.querySelectorAll(".chair").forEach(chair => {
    chair.onclick = function(event) {
        Movie_reservation_modal(event);

        const MovieTitleData = getInputValue("#movieTitleInput");
        const MoviePlaceData = getInputValue("#moviePlaceInput");
        const MovieTimeData = getInputValue("#movieTimeInput");

        if (MovieTitleData && MoviePlaceData && MovieTimeData) {
            $.ajax({
                url: "/resist_reservation",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    movieTitle: MovieTitleData,
                    moviePlace: MoviePlaceData,
                    movieTime: MovieTimeData
                }),
                success: function(response) {
                    alert(`예약이 성공적으로 처리되었습니다:\n영화 제목: ${MovieTitleData}\n장소: ${MoviePlaceData}\n시간: ${MovieTimeData}`);
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                    alert(`예약 처리 중 오류가 발생했습니다.\n오류 코드: ${xhr.status}`);
                }
            });
        } else {
            alert("모든 정보를 입력해 주세요.");
        }
    }
});
