let resist_modal = document.querySelector(".resist_black");
let resist_modal_top=document.getElementById("resist_modal_top")
function movie_modal_resist(event) {
        resist_modal.style.display = "flex"; // 선택한 요소의 display 속성을 'flex'로 설정하여 보이게 합니다.
        console.log("나옵니다. 모달만 만들면 되요.")


    }
    resist_modal_top.onclick = function(event){
        resist_modal.style.display="none";
    }

resist_modal.onclick = function(event){
    if(resist_modal && event.target === resist_modal ){
    resist_modal.style.display="none";}

}