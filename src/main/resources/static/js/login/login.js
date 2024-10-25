document.addEventListener("DOMContentLoaded",()=>
{
    const close=document.getElementById("login_button");

    close.onclick=function(){
    document.login_form.action="Login_form";
    document.login_form.submit();
    document.login_form.method="post";
        $.ajax({
            url: "/api/Tokens",
            type: "POST",
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify({
                apiSecret: "H1jLKhQTtkseyRU1Y5jIDuYdiliP05toRLkPXArC58qW1y1GMXHjBZi7Rp8sjJFcGMtgaG8gqcspANLf"
            }),
            success:function (response) {
                alert("성공했습니다.")
                console.log(response.accessToken);
                let accessToken=response.accessToken;
                sessionStorage.setItem("accessToken",response.accessToken);
                console.log(accessToken)

            }
            
        })

    }

});
