document.addEventListener("DOMContentLoaded", function() {
   let gallery_blank_mini2 = document.querySelectorAll(".gallery_blank_mini2");

   gallery_blank_mini2.forEach(function(gallery_blank_mini) {
      gallery_blank_mini.onclick = function() {
         // hidden input의 value 가져오기
         let w_c = document.getElementById("writer_number2").textContent;

         // 변환된 값이 NaN인지 확인
         if (isNaN(w_c)) {
            alert("유효하지 않은 숫자입니다.");
            return; // 요청을 중단합니다.
         }

         // AJAX 요청
         $.ajax({
            type: "POST",
            url: "/movie/community/get_count",
            data: JSON.stringify({ free_number: w_c }),
            contentType: "application/json",
            success: function(response) {
               alert("성공하였습니다.");
               // AJAX 요청 성공 후 URL 이동
               let url = `/movie/community/free_detail?free_number=${encodeURIComponent(w_c)}`;
               location.href = url; // 페이지 이동

            },
            error: function(xhr, status, error) {
               alert("오류가 발생하였습니다.");
            }
         });
      };
   });
});
