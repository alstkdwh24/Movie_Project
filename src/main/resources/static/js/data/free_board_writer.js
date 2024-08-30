const submit=document.querySelector(".bottom_submit");
submit.onclick = function (){

    document.free_board_writer.action="/Gallery_free_board";
    document.free_board_writer.method="Post"
    console.log(1);
    document.free_board_writer.submit();
}