const mo = document.querySelectorAll(".modal_good");
const bu = document.querySelectorAll(".community");
const closeButtons = document.querySelectorAll(".close_closes");


document.addEventListener('DOMContentLoaded', (event) => {

    console.log("Number of buttons:", bu.length);
    console.log("Number of modals:", modals.length);
    console.log("Number of close buttons:", closeButtons.length);

    bu.forEach((button, index) => {
        if (index < modals.length) {
            button.onclick = function () {
                mo[index].style.display = "flex";
                mo[index].style.justifyContent = "center";
                mo[index].style.alignItems = "center";
            }
        }
    });

    closeButtons.forEach((close, index) => {
        if (index < modals.length) {
            close.onclick = function () {
                mo[index].style.display = "none";
            }
        }
    });

    window.onclick = function (event) {
        mo.forEach(mo => {
            if (event.target === mo) {
                mo.style.display = "none";
            }
        });
    };
});
