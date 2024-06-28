const submit=document.querySelector(".bottom_submit");
submit.onclick = function (e){
    e.preventDefault();
    document.g_board_writer.action="Gallery_g_board";
    console.log(1);
    document.g_board_writer.submit();
}