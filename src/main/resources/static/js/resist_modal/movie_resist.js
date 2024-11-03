
let resist_img=document.getElementById("resist_image");
let currentFiles; // 현재 파일을 저장할 변수


resist_img.ondragover=function (event) {
    event.preventDefault();
    resist_img.classList.add('hover'); // 스타일 변경
    console.log("on")
}

resist_img.ondragleave=function () {
    resist_img.classList.remove("hover"); //스타일 복원
}

resist_img.ondrop=function (event) {
    event.preventDefault();
    resist_img.classList.remove("hover");

    const files=event.dataTransfer.files;
    movie_resist(files);
}
function movie_resist(files) {
    for (const file of files) {
        console.log(file.name);
    const reader=new FileReader();
    reader.onload=function (e){
        console.log(e.target.result); // 파일 내용 출력 (콘솔에)

        currentFiles = new Blob([e.target.result], { type: file.type }); // Blob 생성
        console.log(currentFiles);

    };    reader.readAsText(file);

        console.log(reader);
        let newImage=document.createElement("img");

        newImage.src=URL.createObjectURL(file);
        newImage.style.width="100%"
        newImage.style.height = "100%";
    newImage.style.objectFit = "cover";

    resist_img.appendChild(newImage);

            }
}

resist_img.onclick=function resist_img_download(){
    if(currentFiles){
        const filename=resist_img.getAttribute('data-filename');
        const a = document.createElement('a');

        a.href= URL.createObjectURL(currentFiles);
        a.download=filename;
        document.body.appendChild(a);
        a.click();
        // URL.revokeObjectURL(a.href);
        // document.body.removeChild(a); // 링크 제거
    }
}
let resist_image=document.getElementById("resist_image");
// function image_load(input) {
//     let file=input.files[0];
//
//     let newImage=document.createElement("img");
//
//     newImage.src=URL.createObjectURL(file);
//     newImage.style.width = "100%";
//     newImage.style.height = "100%";
//     newImage.style.objectFit = "cover";
//
//     let container =document.getElementById("resist_img_two");
//     container.appendChild(newImage);
//
// }