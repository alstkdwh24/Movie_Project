const chat_modal=document.querySelectorAll(".chat_modal");
const modal_modals=document.querySelectorAll(".modal_modal")
document.addEventListener('DOMContentLoaded',(event)=>{
    const modalOpenButton=document.querySelectorAll(".modalOpenButton");
    const modalCloseButton=document.querySelectorAll('.chat_modal_button');



    console.log(chat_modal.length);
    console.log(modalOpenButton.length);
    console.log(modalCloseButton.length);

    modalOpenButton.forEach((momobutton,index)=>{
       momobutton.onclick = function (){
           chat_modal[index].style.display="flex";
       }
    });
    modalCloseButton.forEach((close,index)=>{
       close.onclick=function () {
       chat_modal[index].style.display="none";
       }
    });
    window.onclick=function(event) {
     chat_modal.forEach(modal=>{
        if(event.target===modal){
            modal.style.display="none";
        }else if(event.target===modal_modals) {
            event.stopPropagation();
        }
     });
    }



});
