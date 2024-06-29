document.addEventListener('DOMContentLoaded',(event) =>{
   const button_modal=document.querySelector(".momo_button");
   const con_close=document.querySelector(".close_lo");
    const momo_modal=document.querySelector(".momo_container");

   button_modal.onclick=function(){
       momo_modal.style.display="flex";
    }
    con_close.onclick=function (){
       momo_modal.style.display="none";

    }
    window.onclick = function(event) {

        // Gallery_modal 내부를 클릭했는지 확인
        if (momo_modal && event.target === momo_modal) {
            console.log("Window clicked outside Gallery_modal");
            momo_modal.style.display = "none";
        }
    }

});
