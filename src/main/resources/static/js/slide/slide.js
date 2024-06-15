let movie_menu=document.querySelectorAll(".movie_menu");

document.addEventListener('DOMContentLoaded', (even) => {
    let slideOpen = document.querySelectorAll(".image_button");
    let slides = document.querySelectorAll(".slide_close");
    slideOpen.forEach((slidesOpen,index)=>{
    slidesOpen.onclick = function () {
        movie_menu[index].style.left = 0;
    }});
    slides.forEach((sliders,index)=>{
    sliders.onclick = function () {
        movie_menu[index].style.left = "-100%";
    }});
    console.log(movie_menu.length);
    console.log(slideOpen.length);
    console.log(slides.length);
});
