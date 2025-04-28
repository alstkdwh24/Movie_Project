let sub_menu_join = document.getElementById("sub");
// let joinModal_container=document.getElementById("joinModal_container")

sub_menu_join.onclick = function () {
    joinModal_container.style.display = "flex";
    joinModal_container.style.width = "100%";
    joinModal_container.style.height = "100%";
}

joinModal_container.onclick = function (event) {
    if (event.target === joinModal_container) {
        joinModal_container.style.display = "none";
    }
}