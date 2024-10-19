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
                method: "post", headers: {
                    'Content-Type': 'application/json',
                }, body: JSON.stringify({reservation_number: reservationNumber}),
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
        let email = "alstkdwh24@naver.com";  // 실제 이메일로 변경 필요
        let UserPhone = "01043324254";  // 개인정보 주의
        let paymentId = reservationItem.querySelector("#paymentId").textContent;

        function generateMerchantUid() {
            const timestamp = Date.now();
            const randomNum = Math.floor(Math.random() * 100000);
            return `merchant_${timestamp}_${randomNum}`; // 고유한 merchant_uid 생성
        }

        const merchant_uid = generateMerchantUid();
        console.log("생성된 merchant_uid: ", merchant_uid);

        // 결제 요청 함수 호출

        const response = await PortOne.requestPayment({
            storeId: "store-c84ff70f-5317-4896-b83f-9fb7b7d9ea75",
            paymentId: paymentId,
            channelKey: "channel-key-406e8389-2fe6-43a1-a04b-0f1f2db5aef3",
            payMethod: "CARD",
            movie_place: movie_place,
            movie_time: movie_time,
            orderName: movie_title,
            totalAmount: parseInt(reservation_price),
            username: username,
            movie_Seat: movie_Seat,
            // buyer_email: email,
            // buyer_name: username,
            // buyer_tel: UserPhone,  // 개인정보 주의
            currency: "CURRENCY_KRW",
            m_redirect_url: "https://naver.com",
            customer: {
                fullName: "포트원", phoneNumber: "010-0000-1234", email: email
            },

        });
        if (response.code != null) {
            return alert(response.message);
        }
        $.ajax({
            url:"/api/Tokens",
            type:"POST",
            headers: {"Content-Type": "application/json"},
            data:JSON.stringify({
                apiSecret:"H1jLKhQTtkseyRU1Y5jIDuYdiliP05toRLkPXArC58qW1y1GMXHjBZi7Rp8sjJFcGMtgaG8gqcspANLf"
            }),
            success:async function (response) {
                let accessToken = response.accessToken;
                console.log(accessToken);
                $.ajax({
                    url: "/payment/complete",
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    data: JSON.stringify({
                        paymentId: paymentId,
                        orderName: movie_title,
                        storeId: "store-c84ff70f-5317-4896-b83f-9fb7b7d9ea75",
                        channelKey: "channel-key-406e8389-2fe6-43a1-a04b-0f1f2db5aef3",
                        payMethod: "CARD",
                        currency: "CURRENCY_KRW",
                        movie_place: movie_place,
                        movie_time: movie_time,
                        username: username,
                        movie_Seat: movie_Seat,
                        m_redirect_url: "https://naver.com",
                        customer: {
                            fullName: "포트원", phoneNumber: "010-0000-1234", email: email
                        },

                        totalAmount: parseInt(reservation_price)
                    }),
                    success: await function (response) {
                        let paymentId = response.paymentId;
                        console.log(paymentId);
                        let imp_uid = response.imp_uid;
                        console.log(imp_uid)
                        $.ajax({
                            type: "POST",
                            url: "payments/paymentId/pre-register",
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                                paymentId: paymentId
                            })
                        })
                    }
                })
            }


        })

    }


    // function handleResponse(response) {
    //     if (response.success) {
    //         const imp_uid = response.response.imp_uid;
    //         console.log("포트원 고유 거래번호 (imp_uid): ", imp_uid);
    //         console.log("상점 고유 주문번호 (merchant_uid): ", response.response.merchant_uid);
    //         console.log("결제 고유 ID: ", response.response.paymentId);
    //         alert("결제가 성공하였습니다.");
    //     } else {
    //         alert("결제에 실패하였습니다: " + response.error_msg);
    //     }
    // }

});
