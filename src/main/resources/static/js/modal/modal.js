

   document.addEventListener('DOMContentLoaded',(eve)=> {


       let modals = document.querySelectorAll(".modal_container");
       let buttons = document.querySelectorAll(".chat_button");
       var closes = document.querySelectorAll(".close_close");

       console.log("Number of buttons:", buttons.length);
       console.log("Number of modals:", modals.length);
       console.log("Number of close buttons:", closes.length);
       buttons.forEach((button, index) => {
           button.onclick = function () {
               modals[index].style.display = "flex";
               modals[index].style.justifyContent="center";
               modals[index].style.alignItems="center";

           }
       });
       closes.forEach((close, index) => {


           close.onclick = function () {
               modals[index].style.display = "none";
           }
       });

   });