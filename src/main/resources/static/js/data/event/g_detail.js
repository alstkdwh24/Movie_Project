document.addEventListener("DOMContentLoaded", (event) => {
    const talk_button_cancel=document.getElementById("talk_button_cancel");
    talk_button_cancel.onclick=function (){
        document.g_delete.action="g_delete";
        document.g_delete.submit();
        document.g_delete.method="Post";
    }

    const talk_button_update=document.getElementById("talk_button_update");
    talk_button_update.onclick=function (){
        document.g_delete.action="g_update";
        document.g_delete.submit();
        document.g_delete.method="get";
    }

});