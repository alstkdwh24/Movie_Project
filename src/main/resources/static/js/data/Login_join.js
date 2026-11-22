
const Login_join_submit=document.getElementById("button_colors");

const Login_join_cancel=document.getElementById("button_color");

Login_join_submit.onclick=function(){
    document.login_join.action="login_join";
    document.login_join.submit();
}
Login_join_cancel.onclick=function(){
 document.login_join.action="return_movie_mains";
    alert("취소 버튼이 클릭되었습니다.");
 document.login_join.submit();
};