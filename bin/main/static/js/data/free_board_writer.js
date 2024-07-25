const submit=document.querySelector(".bottom_submit");
submit.onclick = function (e){
    e.preventDefault();
    document.free_board_writer.action="Gallery_free_board";
    console.log(1);
    document.free_board_writer.submit();
}