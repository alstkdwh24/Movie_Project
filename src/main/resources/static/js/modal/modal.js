
document.addEventListener('DOMContentLoaded', (event) => {


    let modals = document.querySelector(".modal_container");
    let buttons = document.querySelector("#modal_button");
    let closes = document.querySelector(".close_close");

    console.log("Number of buttons:", buttons.length);
    console.log("Number of modals:", modals.length);
    console.log("Number of close buttons:", closes.length);



            buttons.onclick = function () {
                modals.style.display = "flex";
                modals.style.justifyContent = "center";
                modals.style.alignItems = "center";
            }




            closes.onclick = function () {
                modals.style.display = "none";
            }




    modals.onclick= function(){

                modals.style.display = "none";


    }
});