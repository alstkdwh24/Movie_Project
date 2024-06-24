document.addEventListener('DOMContentLoaded',(event) =>{
   const button_modal=document.querySelectorAll(".momo_button");
   const let_close=document.querySelectorAll(".close_lo");
    const momo_modal=document.querySelectorAll(".momo_container");

    console.log(button_modal.length);
   console.log(momo_modal.length);
   console.log(let_close.length);

   button_modal.forEach((button,index)=>{
       if(index<momo_modal.length){
           button.onclick = function (){
               momo_modal[index].style.display="flex";
               momo_modal[index].style.justifyContent='center';
               momo_modal[index].style.alignItems="center";
           }
       }


       });
    let_close.forEach((close_se, index)=> {
    if(index<momo_modal.length){
        close_se.onclick=function(){
            momo_modal[index].style.display="none";
        }
    }
    });
 momo_modal.forEach((element)=>{
    element.addEventListener('click',(event)=>{
       const modalContents=element.querySelectorAll(".mo_contents");
       console.log(modalContents,length);
       if(modalContents && modalContents.contains(event.target)){
           event.stopPropagation();
       }

    });
    element.addEventListener('click',(event)=>{
       if(event.target===element){
           element.style.display="none";
       }
    });
 });

});
