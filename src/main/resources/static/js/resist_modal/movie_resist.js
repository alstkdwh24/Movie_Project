let resist_img = document.getElementById("resist_image");
let currentFiles; // 현재 파일을 저장할 변수
let button_submit = document.getElementById("button_submit");
let fileElem = document.getElementById("fileElem");
let photo;
resist_img.ondragover = function (event) {
    event.preventDefault();
    resist_img.classList.add('hover'); // 스타일 변경
    console.log("on")
}

resist_img.ondragleave = function () {
    resist_img.classList.remove("hover"); //스타일 복원
}

resist_img.ondrop = function (event) {
    event.preventDefault();
    resist_img.classList.remove("hover");
    resist_img.textContent = "";
    const files=event.dataTransfer.files;
    movie_resist(files);
    let dataTransfer = new DataTransfer();

   for(let i=0; i < files.length; i++){
       dataTransfer.items.add(files[i]);
   }
   fileElem.files=dataTransfer.files;
    photo=fileElem.files;
    console.log("선택된 파일:", photo);
}
resist_img.onchange=function onchange(event) {

}

function movie_resist(files) {
    for (const file of files) {
        let movie_resist_file = file.name ;
        resist_img.setAttribute("data", movie_resist_file);
        const reader = new FileReader();
        reader.onload = function (e) {
            console.log(e.target.result); // 파일 내용 출력 (콘솔에)

            currentFiles = new Blob([e.target.result], {type: file.type}); // Blob 생성
            console.log(currentFiles);
            sessionStorage.setItem("currentFiles", currentFiles);

        };
        reader.readAsText(file);

        let newImage = document.createElement("img");

        newImage.src = URL.createObjectURL(file);
        newImage.style.width = "100%"
        newImage.style.height = "100%";
        newImage.style.objectFit = "cover";
        sessionStorage.setItem("newImage_src", newImage.src);

        resist_img.appendChild(newImage);

    }
}



resist_img.onclick = function () {

    if (currentFiles) {
        let filename = resist_img.getAttribute("data");
        const newImage_src = sessionStorage.getItem("newImage_src");
        const a = document.createElement('a');
        console.log("newImage_src:" + newImage_src)
        a.href = newImage_src;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a); // 링크 제거
        URL.revokeObjectURL(a.href);
    }
}


button_submit.onclick = function () {

    console.log("선택된 파일:", photo);
if(photo.length>0){
    for(let i=0;i<photo.length;i++){
        let photo_file=photo[i];
        console.log("photo_file"+ photo_file)
        let movie_filename = resist_img.getAttribute("data");
        let newImage_src = sessionStorage.getItem("newImage_src");
        let movie_title = document.getElementById("resist_title").value;
        let movie_textarea = document.getElementById("resist_textarea").value;

        // /\    console.log("fileelem"+ fileElem);
        let formData = new FormData();
        formData.append("movie_filename", movie_filename);
        formData.append("movie_textarea", movie_textarea);
        formData.append("movie_title", movie_title);
        formData.append("file",photo_file);

        $.ajax({
            type: "POST",
            url: "/movie_resist/upload_ok/movie_resist_two",
            processData: false,
            contentType: false,
            data: formData,
            success: function () {
                console.log(1);
            }
        })
    }

}

}
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