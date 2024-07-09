const submit=document.querySelector(".bottom_submit");
submit.onclick = function (e){
    e.preventDefault();
    document.Question_writer.action="Question_writer";
    console.log(1);
    document.Question_writer.submit();
}