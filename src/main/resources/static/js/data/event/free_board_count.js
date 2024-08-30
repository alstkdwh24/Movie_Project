
let w_c=document.getElementById("hidden");

   $.ajax({
      type: "Post",
      url: "/get_count",
      data: JSON.stringify({ free_number: w_c }),
      contentType: "application/json",
      success:
         function(){
            alert("성공하였습니다.")
         }
      });

