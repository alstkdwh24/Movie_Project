















let reservation_container = document.getElementById("reservation_modal");
let closecl = document.getElementById("closecl");
function open_modal() {
    reservation_container.style.display = "flex";

    closecl.onclick = function () {

        reservation_container.style.display = "none";


    }
    reservation_container.onclick = function (e) {
        e.preventDefault();
        reservation_container.style.display = "none";
    }
}