document.addEventListener('DOMContentLoaded', (event) => {
    const talk_button_update=document.getElementById("talk_button_update");
    talk_button_update.onclick=function (){
    document.free_detail.action="free_detail_update";
    document.free_detail.submit();
        document.free_detail.method="get"}
})