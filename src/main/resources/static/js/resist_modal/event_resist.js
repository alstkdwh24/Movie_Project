let button_submit=document.getElementById("button_submit");
button_submit.onclick=function () {
    let event_file_name=document.getElementById("resist_image_two").value;
    let event_file=document.getElementById("resist_image_two");
    console.log("event_file_name" + event_file_name);
    console.log("event_file"+ event_file)

    $.ajax({
        type:"POST",
        url:"/movie_resist/event_resist"
    })
}