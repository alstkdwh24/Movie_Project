let button_submit=document.getElementById("button_submit");
button_submit.onclick=function () {
    let event_file_name=document.getElementById("resist_image_two");
    console.log("event_file_name" + event_file_name);
    let formData= new FormData();
    let event_file=document.getElementById("resist_image");
    console.log("event_file"+ event_file);

    let event_name=document.getElementById("resist_title").textContent;
    let resist_textarea=document.getElementById("resist_textarea").textContent;
    formData.append("event_name",event_name);
    formData.append("resist_textarea", resist_textarea);
    formData.append("movie_filename",event_file);


    $.ajax({
        type:"POST",
        url:"/movie_resist/event_resist",
        data: formData,
        processData: false, // jQuery가 데이터를 처리하지 않도록 설정
        contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정

        success:function () {


        }

    })
}