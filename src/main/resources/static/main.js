window.IMP.init("imp00205734");
let button=document.querySelectorAll(".submit_pay");
button.forEach(buttons=>{
    buttons.addEventListener("click",onClickPay);
});
IMP.request_pay({
    pg:"kakaopay",
    pay_method:"card",
    name:"영화",
    merchant_uid:"ORD11111"
})
