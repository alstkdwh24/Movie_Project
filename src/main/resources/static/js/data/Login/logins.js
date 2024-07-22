document.addEventListener('DOMContentLoaded', ()=>{

    const Login=document.getElementById("login_button");

    Login.onclick=function (){
        document.login_div.action="Login_form";
        document.login_div.submit();
    }

});