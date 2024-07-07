const submits=document.querySelector(".bottom_submit");
submits.onclick = function (e){
    e.preventDefault();
    document.Gallery_g_board.action="Gallery_g_board";
    console.log(1);
    document.Gallery_g_board.submit();
}