document.addEventListener("DOMContentLoaded",()=>
{
    const close=document.getElementById("login_button");

    close.onclick=function(){
    document.login_form.action="Login_form";
    document.login_form.submit();
    document.login_form.method="post";
    }

});
