
document.addEventListener('DOMContentLoaded', (event) => {
    const images = [
        "img/330px-a.jpg",
        "img/7gpCBgpVPqdCIqb_G7-d2Y8bXmGj5Pwlv5aPEPbHUwLZvR_xgaq64Re0DrhgcBkTwypFgQ6F99mnTzqcMB4nQVOrJ9v0S6zO8C9eGJ_msAxUKvopb84R0FPJmcH3iPk7bwDaBMWH-PDIZtcfPFt7Ug.webp",
        "img/201930189_1280.jpg",
        "img/K9uVM4dfQUtUEuZLwXhW1RDcKbA3z9saajgdSa4yXC9auIiGJUtKmKRlX5DBlsLzInndp8VLddsK1JCt1aiyNWAga_QEy6G_0l54TzAXD3nmYYznjMBPTgVQVESecjoNpB-qiSDFt-7qWYc8zEkBtA.webp",
        "img/4w84T0Vao2O_uvFZwuxehrec3fgBt0PBETJa2-5dxghEL8tWr6VjzMHpPjKLH89fV_UwjShjQRZlOwP03FnmsV2WLzGJ9Cv5YHmLjN6lwi8CBQjXIqO2cm7gzHW42q-QFXjmt2Rl3O6b2WKM2lNaSA.webp",
        "img/f793ec0c-d54d-46a6-822b-48b3902fda29.jpg",
        "img/스즈매의 문단속.jpeg",
        "img/너의 이름은.jpg"];
    let currentindex = 0;
    const bigImageElement = document.querySelector(".big_image");
    const big_image_arrow_button = document.querySelector(".big_image_arrow_button");
    const big_image_arrow_button_left = document.querySelector(".big_image_arrow_button_left");

    function showSlide(index) {
        bigImageElement.style.backgroundImage = `url(${images[index]})`;

    }

    function nextSlide() {
        currentindex = (currentindex + 1) % images.length;
        showSlide(currentindex);
    }

    function prevSlide() {
        currentindex = (currentindex - 1 + images.length) % images.length;
        showSlide(currentindex);
    }

    big_image_arrow_button.addEventListener('click', nextSlide);
    big_image_arrow_button_left.addEventListener('click', prevSlide);


    showSlide(currentindex);
});
