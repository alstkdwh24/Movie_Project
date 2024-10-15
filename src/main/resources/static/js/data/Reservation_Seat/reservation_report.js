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
        let username = document.getElementById("username").value; // 사용자 이름은 여전히 전체에서 가져옵니다.
        let email = "alstkdwh24@naver.com";
        let UserPhone = "01043324254";// class로 변경
        let paymentId = reservationItem.querySelector("#paymentId").textContent;


        console.log()
        console.log(paymentId)
        let response = await PortOne.requestPayment({

            storeId: "store-c84ff70f-5317-4896-b83f-9fb7b7d9ea75",
            paymentId: paymentId, // 이 부분이 유효한지 확인
            movie_place: movie_place,
            movie_time: movie_time,
            movie_Seat: movie_Seat,
            orderName: movie_title,
            totalAmount: parseInt(reservation_price), // 실제 결제 금액으로 설정
            currency: "KRW",
            channelKey: "channel-key-a8834583-df61-4d0a-82b0-976ae5f3d428",
            payMethod: "CARD",
            onClose: function() {
                console.log("결제가 취소되었습니다.");
                // 취소 시 필요한 추가 처리
            },
            customer: {
                fullName: username,
                phoneNumber: UserPhone,
                email: email
            },

        })
        if (response && response.paymentId) {
            await paymentId_check(response.paymentId, response, reservationItem, movie_title, movie_place, movie_time, movie_Seat, reservation_price, username, email, UserPhone)

        } else {
            alert("paymentId요청 실패");
        }


        async function paymentId_check(paymentId, response, reservationItem, movie_title, movie_place, movie_time, movie_Seat, reservation_price, username, email, UserPhone) {

            try {
                let responses = await fetch(`/payment/status?paymentId=${paymentId}&username=${username}`);
                if (responses.ok) {

                    let paymentStatus = await responses.json();
                    if (paymentStatus.status === "cancel") {
                        alert("결제가 취소되었습니다.")
                        stop();
                    }
                    await $.ajax({
                        url: "/payment/complete",
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify({
                            username: username,
                            paymentId: paymentId,
                            movie_title: movie_title,
                            movie_place: movie_place,
                            movie_time: movie_time,
                            movie_Seat: movie_Seat,
                            reservation_price: parseInt(reservation_price),
                            payment_time: new Date().toISOString(),
                            email: email,
                            UserPhone: UserPhone
                        }),
                        success: async function () {
                            console.log("성공");
                            let reservationNumber = reservationItem.querySelector('input[name="reservation_number"]').value;

                            await fetch('/movie/Reservation/reservation_Delete', {
                                method: "post",
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify({reservation_number: reservationNumber}),
                                // class로 변경

// 비동기 함수로 감싸기
                            })
                            reservationItem.remove();

                            alert("결제가 완료되었습니다.");
                        }
                    })

                }
            } catch
                (error) {
                alert("수정하세요")
            }
        }

    }
});


// 결제 완료 처리 함수
