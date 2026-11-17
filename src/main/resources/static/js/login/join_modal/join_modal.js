let Join_modal=document.getElementById("join_Modal");
let joinModal_container=document.getElementById("joinModal_container")
Join_modal.onclick=function (){
    joinModal_container.style.display="flex";
    joinModal_container.style.width="100%";
    joinModal_container.style.height="100%";
}

joinModal_container.onclick=function () {
    joinModal_container.style.display="none";
}



//joinModal 안에 버튼
let joinModal_left=document.getElementById("joinModal_left");

let joinModal_right=document.getElementById("joinModal_right");

let realJoin_top_container=document.getElementById("realJoin_top_container")

joinModal_left.onclick=function () {
    location.href="/movie/login/join"
    
}
joinModal_right.onclick=function () {
    realJoin_top_container.style.display="flex";
    joinModal_container.style.display="none";

}

realJoin_top_container.onclick=function (event) {
    if(event.target===realJoin_top_container){
    realJoin_top_container.style.display="none";
}}






