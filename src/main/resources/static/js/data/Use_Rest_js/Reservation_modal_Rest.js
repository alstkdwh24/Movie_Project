function movie_title_Rest(event) { // event 매개변수 추가
    event.preventDefault();
    let tag = event.currentTarget; // event 객체 사용
    let movie_titles = document.getElementById("movie_title24");
    let movie_places = document.getElementById("movie_place24");
    let movie_times = document.getElementById("movie_time24");


    // value 속성 사용
    movie_titles.innerHTML = document.querySelector("#movie_title246").innerHTML; // .innerHTML 사용
    movie_places.innerHTML = document.getElementById("movie_place246").innerText;
    movie_times.innerHTML = document.getElementById("movie_time246").innerText;
    movie_titles.value = document.querySelector("#movie_title246").innerHTML; // .innerHTML 사용
    movie_places.value = document.getElementById("movie_place246").innerText;
    movie_times.value = document.getElementById("movie_time246").innerText;

    console.log(movie_titles.innerText, movie_places.innerText, movie_times.innerText);
}
