// document.addEventListener('DOMContentLoaded', (event) => {
//
//     let Username=document.getElementById("username").value;
//     $.ajax({
//         type: "get",
//         url: "/movie/Reservation/reservation_board",
//         data:{username:Username, reservation_number:reservation_number},
//         success: function (data) {
//             create_reservation(data);
//         },
//         error: function (xhr, status, error) {
//             alert("에러가 발생하였습니다.");
//         }
//     });
// });
//
// function create_reservation(data) {
//     let reservation_pay = ''
//     data.forEach(function (result) {
//         reservation_pay += '<div id="reservation_lists"   data-reservation_report=\'' + JSON.stringify(result) + '\'>' +
//             '<div class="list_top">' + result.reservation_number +'</div>'+
//             '<div class="list_top" >' + result.movieTitle + '</div>' +
//             ' <div class="list_top">' + result.moviePlace + '</div>' +
//             '<div class="list_top">' + result.movieTime + '</div>' +
//             '<div class="list_top"><button class="submit">예약취소</button></div>' +
//
//             ' <div class="list_top">' +
//             '<button class="submit">결제진행</button>' +
//             '</div>' +
//
//             '</div>'
//
//
//     });
//     $("#reservation_board").append(reservation_pay);
//
// }


let cancel = document.querySelectorAll(".submit");
cancel.forEach(button => {
    button.onclick = function (event) {
        event.preventDefault();
        let reservationItem = event.target.closest(".reservation_item");
        if (reservationItem) {
            let reservationNumber = reservationItem.querySelector('input[name="reservation_number"]').value;
            fetch('/movie/Reservation/reservation_Delete', {
                method: "post",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({reservation_number: reservationNumber}),
            })
                .then(response => {
                    if (response.ok) {
                        reservationItem.remove();
                    } else {
                        console.log("삭제 요청 실패:", response.statusText);
                    }
                })
                .catch(error => {
                    console.error('삭제 요청 중 에러 발생:', error);
                });
        }

    }
})
let submit_pay = document.querySelectorAll(".submit_pay");
submit_pay.forEach(submit_pays => {

    submit_pays.onclick = async function (event) {
        console.log("버튼 클릭됨");

        // 클릭한 버튼의 부모 요소인 reservationItem을 찾습니다.
        let reservationItem = event.target.closest(".reservation_item");
        let reservationNumber = reservationItem.querySelector('input[name="reservation_number"]').value;
        let movie_title = reservationItem.querySelector("#movie_title").textContent;
        let movie_place = reservationItem.querySelector("#movie_place").textContent;
        let movie_time = reservationItem.querySelector("#movie_time").textContent;
        let movie_Seat = reservationItem.querySelector("#movie_Seat").textContent;
        let reservation_price = reservationItem.querySelector("#reservation_price").textContent;
        let username = document.getElementById("username").value;
        let email = "alstkdwh24@naver.com";
        let UserPhone = "01043324254"; // class로 변경
        let paymentId = reservationItem.querySelector("#paymentId").textContent;
        let merchant_uid="12313131"+Math.floor(Math.random()*1000);
        let imp_uid="12313131"+Math.floor(Math.random()*1000);
        function generateMerchantUid() {
            const timestamp = Date.now();
            const randomNum = Math.floor(Math.random() * 100000);
            return `merchant_${timestamp}_${randomNum}`; // 고유한 merchant_uid 생성
        }

        const merchantUid = generateMerchantUid();
        console.log(paymentId);
        IMP.init("imp00205734");

        IMP.request_pay({
         channelKey: "channel-key-b59867e5-cbbe-463d-a06d-7a441a0c3533",
            pay_method:"card",
            merchant_uid:merchant_uid,
            name:movie_title,
            amount: parseInt(reservation_price),
            buyer_email:email,
            buyer_name:username,
            buyer_tel:UserPhone,
            current:"KRW",
            vbank_due:"2025-12-24",
            m_redirect_url:"https://naver.com",
            period: {
                from: "20240527",
                to: "20241231"
            }
        }, );


    }

})
function get(response) {

    if (response.response && response.response.merchant_uid) {
        const imp_uid = response.response.imp_uid;
        const merchant_uid = response.response.merchant_uid;  // 주문 고유 번호
        const paymentId = response.response.paymentId;
        console.log("포트원 고유 거래번호 (imp_uid): ", imp_uid);
        console.log("상점 고유 주문번호 (merchant_uid): ", merchant_uid);
        console.log("결제 고유 ID: ", paymentId);
    } else {
        alert("요청실패")
    }
}