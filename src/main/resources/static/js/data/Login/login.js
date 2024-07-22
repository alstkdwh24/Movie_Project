document.addEventListener('DOMContentLoaded', ()=>{

    const Login=document.getElementById("login_button");

    Login.onclick=function (){
        document.login_div.action="login_div";
        document.login_div.submit();
    }

});