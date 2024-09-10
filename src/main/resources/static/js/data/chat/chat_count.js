let gallery_blank_mini2=document.querySelectorAll(".gallery_blank_mini2");
gallery_blank_mini2.forEach(function (gallery_blank_mini2_24){
    gallery_blank_mini2_24.onclick=function (){
        const QNumber=gallery_blank_mini2_24.dataset.question;

        if (isNaN(QNumber) || QNumber.trim() === "") {
            alert("유효하지 않은 숫자입니다.");
            return; // 요청을 중단합니다.
        }

        $.ajax({
            type:"POST",
            url:"/movie/chats/Question_count",

            contentType:"application/json",
            data:JSON.stringify({Q_number:QNumber}),
            success:function (){
                alert("성공하였습니다.")
                location.href=`/movie/chats/Question_detail?Q_number=${QNumber}`
            },
            error: function(xhr, status, error) {
                alert("오류가 발생하였습니다.");
            }
        });
    }
});