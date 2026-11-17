document.addEventListener('DOMContentLoaded',(event)=>{

    const bu_m=document.querySelectorAll(".ww_ss");
    const let_c=document.querySelectorAll(".f_c");
    const momo_m=document.querySelectorAll(".free_modal_container");

    console.log(bu_m.length);
    console.log(momo_m.length);
    console.log(let_c.length);

    bu_m.forEach((button,index)=>{
        if(index<momo_m.length){
            button.onclick = function (){
                momo_m[index].style.display="flex";
                momo_m[index].style.justifyContent='center';
                momo_m[index].style.alignItems="center";
            }
        }


    });
    let_c.forEach((close_se, index)=> {
        if(index<momo_m.length){
            close_se.onclick=function(){
                momo_m[index].style.display="none";
            }
        }
    });
    momo_m.forEach((el)=>{
        el.addEventListener('click',(event)=>{
            const modalContents=el.querySelectorAll(".mo_contents");
            console.log(modalContents,length);
            if(modalContents && modalContents.contains(event.target)){
                event.stopPropagation();
            }

        });
        el.addEventListener('click',(event)=>{
            if(event.target===el){
                el.style.display="none";
            }
        });
    });


});