
let modals = document.querySelectorAll(".modal_contents");
document.addEventListener('DOMContentLoaded', (event) => {

    let buttons = document.querySelectorAll(".modal_button");
    let closes = document.querySelectorAll(".close_close");

    console.log("Number of buttons:", buttons.length);
    console.log("Number of modals:", modals.length);
    console.log("Number of close buttons:", closes.length);

    buttons.forEach((button, index) => {
        if (index < modals.length) {
            button.onclick = function () {
                modals[index].style.display = "flex";
                modals[index].style.justifyContent = "center";
                modals[index].style.alignItems = "center";
            }
        }
    });

    closes.forEach((close, index) => {
        if (index < modals.length) {
            close.onclick = function () {
                modals[index].style.display = "none";
            }
        }
    });

    window.onclick = function (event) {
        modals.forEach(modal => {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        });
    };
});