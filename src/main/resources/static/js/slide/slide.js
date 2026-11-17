document.addEventListener('DOMContentLoaded', (event) => {
    const sneak_peek_movie_modalss=document.querySelector(".sneak_peek_movie");

    const sneak_modal=document.querySelector(".sneak_mobal");
    let trailer = document.getElementById("trailer");
    const bigImageElement = document.querySelector(".big_image");
    const big_image_arrow_button = document.querySelector(".big_image_arrow_button");
    const big_image_arrow_button_left = document.querySelector(".big_image_arrow_button_left");
const sneak_close=document.querySelector(".sneak_close");
const sneak_entry=document.querySelector(".sneak_entry")


    // 슬라이드 이미지는 static/movie/img/hero/ 의 1920x720 가공본(원본은 movie/img/ 에 그대로 있음)
    const images = [
        {img: "img/hero/slide-1-avengers.jpg", trailer: "https://www.youtube.com/embed/Ko2NWhXI9e8?si=BSxIUcM6LXJibt61"},
        {img: "img/hero/slide-2-lotr.jpg", trailer: "https://www.youtube.com/embed/iByssURPjmE?si=2ufWjjMjdN_G5kv7"},
        {img: "img/hero/slide-3.jpg", trailer: "https://www.youtube.com/embed/LU7ujHmsc-s?si=hRsxJsraMz9ZIq2k"},
        {img: "img/hero/slide-4.jpg", trailer: "https://www.youtube.com/embed/eOrNdBpGMv8?si=ffEACqqlaYPZHyWE"},
        {img: "img/hero/slide-5.jpg", trailer: "https://www.youtube.com/embed/e78_CTFi_jQ?si=mFosWFOoTiWFDQ5b"},
        {img: "img/hero/slide-6-suzume.jpg", trailer: "https://www.youtube.com/embed/BBxtfGZodfo?si=xKv-UCxmEhXP1_U3"},
        {img: "img/hero/slide-7-yourname.jpg", trailer: "https://www.youtube.com/embed/enRm-9qF2L8?si=j9sMFTkSCs2R0Yht"}];
    let currentindex = 0;

    function showSlide(index) {
        bigImageElement.style.backgroundImage = `url(${images[index].img})`;
        bigImageElement.dataset.index=index;
    }

    function nextSlide() {
        currentindex = (currentindex + 1) % images.length;
        showSlide(currentindex);
    }

    function prevSlide() {
        currentindex = (currentindex - 1 + images.length) % images.length;
        showSlide(currentindex);
    }
    sneak_entry.onclick=function (){
        const currentImageIndex = bigImageElement.dataset.index;
        sneak_peek_movie_modalss.style.display="flex";
        trailer.src = images[currentImageIndex].trailer;

    }
    // 닫을 때 src를 비워야 영상 소리가 계속 나지 않는다
    function closeTrailer(){
        sneak_peek_movie_modalss.style.display="none";
        trailer.src = "";
    }
    sneak_close.onclick=closeTrailer;
    sneak_peek_movie_modalss.onclick=function(even){
        if(even.target === sneak_peek_movie_modalss){
            closeTrailer();
        }
    }

    big_image_arrow_button.addEventListener('click', nextSlide);
    big_image_arrow_button_left.addEventListener('click', prevSlide);


    showSlide(currentindex);
});
