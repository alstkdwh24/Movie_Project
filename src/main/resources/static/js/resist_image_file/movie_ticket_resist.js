
let resist_img=document.getElementById("resist_image");

let currentFiles; // 현재 파일을 저장할 변수

let fileElem = document.getElementById("fileElem");

let photo;

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
    resist_img.textContent='';
    let dataTransfer=new DataTransfer();

    const files=event.dataTransfer.files;
    movie_resist(files);
    for(let i=0; i<files.length;i++){
        dataTransfer.items.add(files[i])
    }
    fileElem.files=dataTransfer.files;
    photo=fileElem.files;
}
function movie_resist(files) {
    for (const file of files) {
        let movie_resist_file=file.name;
        sessionStorage.setItem("data",movie_resist_file)
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
        sessionStorage.setItem("newImage_src",newImage.src)

        resist_img.appendChild(newImage);

    }
}

resist_img.onclick=function resist_img_download(){
    if(currentFiles){
        const filename=resist_img.getAttribute('data-filename');
        const a = document.createElement('a');
        let newImage_src = sessionStorage.getItem("newImage_src");

        a.href= URL.createObjectURL(currentFiles);
        a.download=filename;
        document.body.appendChild(a);
        a.click();
        URL.revokeObjectURL(a.href);
        document.body.removeChild(a); // 링크 제거
    }
}
let button_submit=document.getElementById("button_submit");
button_submit.onclick=function () {
    if(photo.length>0){
        for(let i=0; i<photo.length; i++){
            let photo_file=photo[i];
            let movie_ticket_filename=sessionStorage.getItem("data");
            console.log("movie_ticket_filename",movie_ticket_filename);

            let formData=new FormData();

            let uploadPaths="";

            let movie_ticket_name=document.getElementById("resist_title").value;
            let resist_textarea=document.getElementById("resist_textarea").value;
            formData.append("movie_ticket_name",movie_ticket_name);
            formData.append("movie_ticket_filename",movie_ticket_filename);
            formData.append("resist_textarea",resist_textarea);
            formData.append("movie_ticket_file",photo_file);
            formData.append("uploadPaths",uploadPaths);

            $.ajax({
                type:"Post",
                url:"/movie_resist/movie_ticket_resist",
                data:formData,
                processData:false,
                contentType:false,
                success:function () {
                    console.log(1);
                }
            })

        }
    }

}