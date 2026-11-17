document.addEventListener("DOMContentLoaded", (event) => {
    let username=document.getElementById("user").value;
    console.log("Username:", username);
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/movie/api/account",
        data: { username: username },
        success: function (data) {
            account(data)

        }
        ,
        error: function () {
            alert("잘못되었습니다.")
        }

    });
});

function account(data) {
    let add = '';

    data.forEach(function (result, index) {


        add += '<div class="Mypage_titles"> 회원 수정</div>' +
            '<input type="hidden" value="'+result.username+'" id="username">'+
            ' <div class="Mypage_input" > ' +
            '   <div class="top_left">이름</div>' +
            ' <div class="top_right" data-add=\''+JSON.stringify(result)+'\'> <input type="text" class="account_text" id="namena" value="'+(result.namena)+'"></div>' +
            ' </div>' +
            '<div class="blank"></div>' +
            '<div class="Mypage_input" data-set="\''+JSON.stringify(result)+'\'">' +
            ' <div class="top_left">닉네임</div>' +
            ' <div class="top_right"><input type="text" class="account_text" id="nickname" value="'+(result.nickname)+'"></div>' +
            '</div>' +
            '<div class="blank"></div>' +

            '<div class="Mypage_input">' +
            '  <div class="top_left">비밀번호</div>' +
            '<div class="top_right"><!--<input type="text" class="account_text" id="password">--></div>' +
            '</div>' +
            '<div class="blank"></div>' +

            '   <div class="Mypage_input">' +
            '<div class="top_left">핸드폰번호</div>' +
            '<div class="top_right"><input type="text" class="account_text" value="'+(result.phone)+'" id="phone"></div>' +
            ' </div>' +
            ' <div class="blank"></div>' +

            '  <div class="Mypage_input">' +
            ' <div class="top_left">생년월일</div>' +
            ' <div class="top_right"><input type="date" class="account_text" value="'+(result.birthDate)+'" id="birthDate"></div>' +
            ' </div>' +
            '  <div class="blank"></div>' +

            '  <div class="Mypage_input">' +
            '<div class="top_left">성별</div>' +
            '  <div class="top_right"><input type="text" class="account_text" value="'+(result.gender)+'" id="gender"></div>' +
            '</div>' +
            '<div class="blank"></div>' +

            '  <div class="Mypage_input">' +
            '<div class="top_left">직업</div>' +
            ' <div class="top_right"><input type="text" class="account_text" value="'+(result.job)+'" id="job"></div>' +
            '</div>' +
            '<div class="blank"></div>' +

            '   <div class="Mypage_input">' +
            ' <div class="top_left">이메일</div>' +
            ' <div class="top_right"><input type="text" class="account_text" value="'+(result.email)+'" id="email"></div>' +
            '</div>'});

    $("#add").append(add);
}


