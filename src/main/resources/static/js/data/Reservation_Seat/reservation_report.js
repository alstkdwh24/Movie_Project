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
        let reservationItems = event.target.closest(".reservation_item");
        if (reservationItems) {
            let reservationNumber = reservationItems.querySelector('input[name="reservation_number"]').value;
            fetch('/movie/Reservation/reservation_Delete', {
                method: "post", headers: {
                    'Content-Type': 'application/json',
                }, body: JSON.stringify({reservation_number: reservationNumber}),
            })
                .then(response => {
                    if (response.ok) {
                        reservationItems.remove();
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

const BASE_URL = "http://localhost:9494/movie/Reservation/success_reservation_page";
let submit_pay = document.querySelectorAll(".submit_pay");
submit_pay.forEach(submit_pays => {
    submit_pays.onclick = async function (event) {
        console.log("버튼 클릭됨");

        // 클릭한 버튼의 부모 요소인 reservationItem을 찾습니다.
        let reservationItem = event.target.closest(".reservation_item"); // class로 변경
        if (reservationItem) {
            // 해당 항목의 데이터만 가져옵니다.
            let reservationNumber = reservationItem.querySelector('input[name="reservation_number"]').value;
            let movie_title = reservationItem.querySelector("#movie_title").textContent;
            console.log(movie_title)
            let movie_place = reservationItem.querySelector("#movie_place").textContent;
            let movie_time = reservationItem.querySelector("#movie_time").textContent;
            let movie_Seat = reservationItem.querySelector("#movie_Seat").textContent;
            let reservation_price = reservationItem.querySelector("#reservation_price").textContent;
            let username = document.getElementById("username").value; // 사용자 이름은 여전히 전체에서 가져옵니다.
            const now = new Date();
            const formattedDate = now.toISOString();
            console.log("선택된 예약 번호:", reservationNumber); // 선택된 예약 번호 출력

            // 결제 요청
            const response = await PortOne.requestPayment({
                storeId: "store-c84ff70f-5317-4896-b83f-9fb7b7d9ea75",
                paymentId: "12121212121" + Math.floor(Math.random()*100000000000),
                orderName: movie_title,
                totalAmount: parseInt(reservation_price), // 실제 결제 금액으로 설정
                currency: "KRW",
                channelKey: "channel-key-f1dd6390-3f83-4437-b1ba-9a964c60dfdb", // channelKey: "channel-key-ef1eb1f6-6217-4723-909c-d1f77002edeb",
                // channelKey: "channel-key-a8834583-df61-4d0a-82b0-976ae5f3d428",
                payMethod: "EASY_PAY",
                easyPay: {
                    easyPayProvider: "KAKAOPAY",
                },
                customer: {
                    fullName: username, phoneNumber: "0", email: "alstkdwh24@naver.com"
                },
            })
            $.ajax({
                url: "/payment/complete", method: "POST", contentType: "application/json", // headers에서 content-type 설정
                data: JSON.stringify({
                    username:username,
                    paymentId: response.paymentId, // 이 값이 실제로 올바른지 확인
                    movie_title: movie_title,
                    movie_place: movie_place,
                    movie_time: movie_time,
                    movie_Seat: movie_Seat,
                    reservation_price: parseInt(reservation_price), // 숫자로 변환
                    payment_time: new Date().toISOString()
                }), success: function (response) {
                    console.log("결제 완료:", response);
                    let reservationItems = event.target.closest(".reservation_item");

                    fetch('/movie/Reservation/reservation_Delete', {
                        method: "post", headers: {
                            'Content-Type': 'application/json',
                        }, body: JSON.stringify({reservation_number: reservationNumber}),
                    })
                        .then(response => {
                            if (response.ok) {
                                reservationItems.remove();
                            } else {
                                console.log("삭제 요청 실패:", response.statusText);
                            }
                        })
                        .catch(error => {
                            console.error('삭제 요청 중 에러 발생:', error);
                        });
                }, error: function (xhr, status, error) {
                    console.error("결제 실패:", status, error);
                }
            })

            
        }
    }
});



