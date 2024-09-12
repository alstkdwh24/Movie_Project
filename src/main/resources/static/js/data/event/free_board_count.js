document.addEventListener("DOMContentLoaded", function() {
   let gallery_blank_mini2 = document.querySelectorAll(".gallery_blank_mini2");

   gallery_blank_mini2.forEach(function(gallery_blank_mini) {
      gallery_blank_mini.onclick = function() {
         // hidden input의 value 가져오기
         const freeNumber = gallery_blank_mini.dataset.freeNumber; // 데이터 속성에서 값 가져오기

         // 변환된 값이 NaN인지 확인
         if (isNaN(freeNumber) || freeNumber.trim() === "") {
            alert("유효하지 않은 숫자입니다.");
            return; // 요청을 중단합니다.
         }

         // AJAX 요청
         $.ajax({
            type: "POST",
            url: "/movie/community/get_count",
            data: JSON.stringify({ free_number: freeNumber }),
            contentType: "application/json",
            success: function(response) {
               alert("성공하였습니다.");
               // AJAX 요청 성공 후 URL 이동
               location.href = `/movie/community/free_detail?free_number=${freeNumber}`; // 페이지 이동
            },
            error: function(xhr, status, error) {
               alert("오류가 발생하였습니다.");
            }
         });

      };
   });
});
