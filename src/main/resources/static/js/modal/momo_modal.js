const momo_modal=document.querySelectorAll(".momo_container");
document.addEventListener('DOMContentLoaded',(event) =>{
   const buttonmodal=document.querySelectorAll(".momo_button");
   const letclose=document.querySelectorAll(".close_lo");
   console.log(buttonmodal.length);
   console.log(momo_modal.length);
   console.log(letclose.length);

   buttonmodal.forEach((button,index)=>{
       if(index<momo_modal.length){
           buttonmodal.onclick = function (){
               momo_modal[index].style.display="flex";
               momo_modal[index].style.justifyContent='center';
               momo_modal[index].style.alignItems="center";
           }
       }


       });
    letclose.forEach((closese, index)=> {
    if(index<momo_modal.length){
        closese.onclick=function(){
            momo_modal[index].style.display="none";
        }
    }
    });
    momo_modal.forEach((element)=>{
        element.addEventListener('click',(event)=>{
            const modalContents=element.querySelectorAll(".mo_contents");
            console.log(modalContents);
            if(modalContents && modalContents.contains(event.target)){
                event.stopPropagation();
            }
            }
        );
        element.addEventListener('click',(event)=>{
            if(event.target===element){
                element.style.display='none';
            }
        })
    });

});