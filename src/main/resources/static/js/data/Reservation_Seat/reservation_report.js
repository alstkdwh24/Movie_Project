// 예약 리포트: 예약 취소와 포트원 결제.
// 결제 확정은 서버(/payment/complete)가 포트원에서 직접 조회해 상태와 금액을 검증한다.
// 포트원 API Secret은 브라우저에 두지 않는다(storeId·channelKey는 공개 식별자).
(function () {
    const config = document.getElementById("portone-config");
    if (!config) {
        return; // 이 파일을 함께 불러오는 다른 화면에서는 아무것도 하지 않는다
    }

    document.querySelectorAll(".reservation_item").forEach(item => {
        const reservationNumber = item.querySelector('input[name="reservation_number"]').value;
        const paymentId = item.querySelector("#paymentId").textContent.trim();

        item.querySelector(".submit").addEventListener("click", async event => {
            event.preventDefault();
            const response = await fetch("/movie/Reservation/reservation_Delete", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({reservation_number: reservationNumber})
            });
            if (response.ok) {
                item.remove();
            } else {
                alert("예약을 취소하지 못했습니다.");
            }
        });

        item.querySelector(".submit_pay").addEventListener("click", async event => {
            event.preventDefault();
            const payment = await PortOne.requestPayment({
                storeId: config.dataset.storeId,
                channelKey: config.dataset.channelKey,
                paymentId: paymentId,
                orderName: item.querySelector("#movie_title").textContent.trim(),
                // 화면 금액으로 결제를 요청하지만, 서버가 포트원 결제 금액을 DB 예약 금액과 다시 비교한다
                totalAmount: parseInt(item.querySelector("#reservation_price").textContent, 10),
                currency: "CURRENCY_KRW",
                payMethod: "CARD"
            });
            if (payment.code != null) {
                alert(payment.message);
                return;
            }

            const response = await fetch("/payment/complete", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({paymentId: paymentId})
            });
            if (response.status === 201) {
                alert("결제가 완료되었습니다.");
                item.remove();
            } else {
                alert("결제를 확인하지 못했습니다. 결제 내역을 확인해 주세요.");
            }
        });
    });
})();
