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
    // closed.onclick=function(e){
    //     e.preventDefault();
    //     imin_menu_modal.style.display="none";
    // }
    window.onclick = function(event) {
        if ( event.target === imin_menu_modal) {
            console.log("Window clicked outside momo_modal");
            imin_menu_modal.style.display = "none";
        }
    }

});