let correction=document.querySelector(".corrections");

correction.onclick=function (e){

    let username=document.getElementById("username").value;
    let namena=document.getElementById("namena").value;
    let nickname=document.getElementById("nickname").value;
    let phone=document.getElementById("phone").value;
    let birthDate=document.getElementById("birthDate").value;
    let gender=document.getElementById("gender").value;
    let job=document.getElementById("job").value;
    let email=document.getElementById("email").value;
    console.log(nickname);
    $.ajax({
        type:"PUT",
        url:"/movie/api/modify/" + username,
        contentType: "application/json",
        data:JSON.stringify({
            namena:namena,
            nickname:nickname,
            phone:phone,
            birthDate:birthDate,
            gender:gender,
            job:job,
            email:email
        }),
        success:function (A){
            alert("완료되었습니다.");
            console.log("업데이트 성공:", data);
        },
        error:function () {

        }
    })
}