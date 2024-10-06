const submit=document.querySelector(".bottom_submit");
submit.onclick = function (){

    document.free_board_writer.action="Gallery_free_board";
    document.free_board_writer.method="Post"
    console.log(1);
    document.free_board_writer.submit();
}

const big_content_file=document.querySelector(".big_content_file");
big_content_file.addEventListener('dragover',(event)=>{
    event.preventDefault();
    big_content_file.classList.add("hover")
});
big_content_file.addEventListener('dragleave',()=>{
    big_content_file.classList.remove('hover');
});

big_content_file.addEventListener('drop',(event)=>{
   event.preventDefault();
   big_content_file.classList.remove('hover');

   const files=event.dataTransfer.files;
   handleFiles(files);

});

function handleFiles(files){
    for(const file of files){
        const listItem =document.createElement("div");
        listItem.textContent = file.name;
        big_content_file.appendChild(listItem);

        const reader = new FileReader();
        reader.onload=(e)=>{
            console.log(e.target.result);
            currentFile= new Blob([e.target.result],{type:file.type});
        }
        reader.readAsText(file)//파일을 덱스트로 읽기

    }
}