document.addEventListener("DOMContentLoaded", (event)=>{

const momo_button=document.getElementById("menu");
    const imin_menu_modal=document.querySelector(".imin_menu_modal");
    const closed=document.getElementById("closed");
    console.log(momo_button);
    console.log(imin_menu_modal);
    console.log(closed);
    momo_button.onclick=function(){
    imin_menu_modal.style.display="flex";
    imin_menu_modal.style.justifyContent="center";
    imin_menu_modal.style.alignItems="center";
}
    closed.onclick=function(){
        imin_menu_modal.style.display="none";
    }
    imin_menu_modal.onclick = function(event) {
        if (imin_menu_modal && event.target === imin_menu_modal) {
            console.log("Window clicked outside momo_modal");
            imin_menu_modal.style.display = "none";
        }
    }

});