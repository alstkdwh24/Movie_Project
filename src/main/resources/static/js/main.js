let movie = document.getElementById("main_div");
movie.onclick=function (e){
    document.MovieAction.action="/main";
    document.MovieAction.submit();
}