document.addEventListener('DOMContentLoaded', (event) => {

    let resist_modal_top = document.getElementById("resist_modal_top");
    let resist_modal = document.querySelector(".resist_black");
let writer=document.getElementById("writer");
    writer.onclick = function () {
        resist_modal.style.display = "flex";
    }

    resist_modal_top.onclick = function (event) {
        resist_modal.style.display = "none";
    }

    resist_modal.onclick = function (event) {
        if (resist_modal && event.target === resist_modal) {
            resist_modal.style.display = "none";
        }

    }
});