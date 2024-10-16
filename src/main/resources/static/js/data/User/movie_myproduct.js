document.addEventListener('DOMContentLoaded',(event)=> {
    let username = document.getElementById("username").value;
    console.log(username.textContent);
    $.ajax({
        url: "/getToken",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            imp_key: "4213686751742744",
            imp_secret: "zTckA0duwrtqtoJ4yr4GqiVKagkhyG0NAUpyw3Nkm7Ip8vdnpG9FRVuEowRCf6Ym2ZgqUTrn8fThAJ75"
        }), success: async function (response) {
            const token=response.response.access_token;
            let imp_uid="01929498-5641-4fab-18cb-cc9d5f38c129"

            await tokenGet(token, imp_uid);
            console.log(token)
            // 이후 필요한 API 요청에 이 토큰을 사용할 수 있습니다.
        }, error: function (jqXHR, textStatus, errorThrown) {
            console.error("토큰 요청 중 에러 발생:", textStatus, errorThrown); // 오류 처리
        }


    });

});
async function tokenGet(token,imp_uid) {
    console.log(token);
    $.ajax({
        type:"GET",
        url: `/payments/${imp_uid}`,
        data: { token: token }, // token을 data로 포함

        headers:{ Authorization:`Bearer ${token}`},
        // data: JSON.stringify({imp_uid:imp_uid}), // 빈 객체 전송,
        contentType: 'application/json',
     success: function (data){
           payment_report(data)
            console.log(`Authorization Bearer ${token}`)
        }
    })

}

function payment_report(data){
    let report=""
    data.forEach(function (result){
        // const maskedPayment=result.paymentId.replace(/.(?=.{4})/g, '*');
        const time=result.payment_time.substring(11,19);
        report+=`<div class="product_list_top" data-payment='${JSON.stringify(result)}'>` +
`<div id="paymentId" value="${result.paymentId}" >  ${result.paymentId} </div> ` +
            `<div class="list_bottom"> ${result.movie_title} </div>` +
            `        <div class="list_bottom">${result.movie_time}</div>` +
            `        <div class="list_bottom"> ${result.movie_place} </div>` +
            `        <div class="list_bottom"> ${result.movie_Seat} </div>` +
            `        <div class="list_bottom">  ${result.reservation_price}  </div>` +
            `        <div class="list_bottom"> ${time} </div>` +
            `        <div class="list_bottom">  ${result.username} </div>` +
            `    </div>`

    });
    $("#product_list").append(report);
}