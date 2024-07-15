document.addEventListener('DOMContentLoaded', (event) => {
    const talk_button_update=document.getElementById("talk_button_update");
    talk_button_update.onclick=function (){
    document.free_detail.action="free_detail_update";
    document.free_detail.submit();
        document.free_detail.method="get"}

   const talk_button_cancel=document.getElementById("talk_button_cancel");
    talk_button_cancel.onclick=function(){
        document.free_detail.action="talk_button_cancel";
        document.free_detail.submit();
        alert("글이 지워졌습니다.")
    }

});