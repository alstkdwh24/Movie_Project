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
        let reservationItem=event.target.closest("#reservation_lists");
        if(reservationItem){
            let reservationNumber=reservationItem.querySelector('input[name="reservation_number"]').value;
        fetch('/movie/Reservation/reservation_Delete',{
            method: "post",
            headers:{
                'Content-Type':'application/json',
            },
            body:JSON.stringify({reservation_number:reservationNumber}),
        })
            .then(response=>{
                if(response.ok){
                    reservationItem.remove();
                }else{
                    console.log("삭제 요청 실패:", response.statusText);
                }
            })
            .catch(error => {
                console.error('삭제 요청 중 에러 발생:', error);
            });
        }

    }
})