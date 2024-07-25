const Gallery_modal=document.querySelector(".Gallery_container");
document.addEventListener('DOMContentLoaded',(event)=>{
    const community_button=document.querySelector(".community");
    const G_C=document.querySelector(".G_C");
    console.log(Gallery_modal.length);
    console.log(community_button.length);
    console.log(G_C.length);
    community_button.onclick=function(){
        Gallery_modal.style.display="flex";

    }
    G_C.onclick=function(){
        Gallery_modal.style.display="none";
    }
    window.onclick = function(event) {

        // Gallery_modal 내부를 클릭했는지 확인
        if (Gallery_modal && event.target === Gallery_modal) {
            console.log("Window clicked outside Gallery_modal");
            Gallery_modal.style.display = "none";
        }
    }


} );


