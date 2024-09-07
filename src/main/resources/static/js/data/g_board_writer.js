const submits=document.querySelector(".bottom_submit");
submits.onclick = function (e){
    e.preventDefault();
    document.Gallery_g_board.action="Gallery_g_board";
    document.Gallery_g_board.method="Post"

    console.log(1);
    document.Gallery_g_board.submit();
}



