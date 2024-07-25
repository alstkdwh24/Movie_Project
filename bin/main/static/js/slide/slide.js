document.addEventListener('DOMContentLoaded', (event) => {
    const sneak_peek_movie_modalss=document.querySelector(".sneak_peek_movie");

    const sneak_modal=document.querySelector(".sneak_mobal");
    let trailer = document.getElementById("trailer");
    const bigImageElement = document.querySelector(".big_image");
    const big_image_arrow_button = document.querySelector(".big_image_arrow_button");
    const big_image_arrow_button_left = document.querySelector(".big_image_arrow_button_left");
const sneak_close=document.querySelector(".sneak_close");
const sneak_entry=document.querySelector(".sneak_entry")


    const images = [
        {img: "img/330px-a.jpg",trailer:"https://www.youtube.com/embed/Ko2NWhXI9e8?si=BSxIUcM6LXJibt61" },
        {img:"img/7gpCBgpVPqdCIqb_G7-d2Y8bXmGj5Pwlv5aPEPbHUwLZvR_xgaq64Re0DrhgcBkTwypFgQ6F99mnTzqcMB4nQVOrJ9v0S6zO8C9eGJ_msAxUKvopb84R0FPJmcH3iPk7bwDaBMWH-PDIZtcfPFt7Ug.webp", trailer:"https://www.youtube.com/embed/iByssURPjmE?si=2ufWjjMjdN_G5kv7" },
        {img:"img/201930189_1280.jpg",trailer: "https://www.youtube.com/embed/LU7ujHmsc-s?si=hRsxJsraMz9ZIq2k"},
        {img:"img/K9uVM4dfQUtUEuZLwXhW1RDcKbA3z9saajgdSa4yXC9auIiGJUtKmKRlX5DBlsLzInndp8VLddsK1JCt1aiyNWAga_QEy6G_0l54TzAXD3nmYYznjMBPTgVQVESecjoNpB-qiSDFt-7qWYc8zEkBtA.webp",trailer: "https://www.youtube.com/embed/eOrNdBpGMv8?si=ffEACqqlaYPZHyWE"},
        {img:"img/4w84T0Vao2O_uvFZwuxehrec3fgBt0PBETJa2-5dxghEL8tWr6VjzMHpPjKLH89fV_UwjShjQRZlOwP03FnmsV2WLzGJ9Cv5YHmLjN6lwi8CBQjXIqO2cm7gzHW42q-QFXjmt2Rl3O6b2WKM2lNaSA.webp",trailer: "https://www.youtube.com/embed/e78_CTFi_jQ?si=mFosWFOoTiWFDQ5b"},
        {img:"img/f793ec0c-d54d-46a6-822b-48b3902fda29.jpg",trailer: "https://www.youtube.com/embed/b_bBb1PO_N0?si=7BxtBWZn4vunbkmH"},
        {img:"img/door.jpeg",trailer:"https://www.youtube.com/embed/BBxtfGZodfo?si=xKv-UCxmEhXP1_U3" },
        {img:"img/your_name.jpg",trailer: "https://www.youtube.com/embed/enRm-9qF2L8?si=j9sMFTkSCs2R0Yht"}];
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
    sneak_close.onclick=function(){
        sneak_peek_movie_modalss.style.display="none";
    }
    window.onclick=function(event){
        if(event.target === sneak_peek_movie_modalss){
            sneak_peek_movie_modalss.style.display = "none";

        }
    }

    big_image_arrow_button.addEventListener('click', nextSlide);
    big_image_arrow_button_left.addEventListener('click', prevSlide);


    showSlide(currentindex);
});
