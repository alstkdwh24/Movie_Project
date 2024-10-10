document.addEventListener('DOMContentLoaded',(event)=>{
   $.ajax({
       type:"get",
       url:"/movie/payment/report",
       contentType:"application/json",
       success:function (data) {
            payment_report(data);
       }
       ,
       error:function (){

       }
   })
});
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