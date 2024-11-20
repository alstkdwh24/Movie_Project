
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
    resist_img.textContent='';

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

let button_submit=document.getElementById("button_submit");
button_submit.onclick=function () {
    let event_file_name=document.getElementById("resist_image_two");
    console.log("event_file_name" + event_file_name);
    let formData= new FormData();
    let event_file=$("#resist_image")[0].files[0];
    console.log("event_file"+ event_file);

    let event_name=document.getElementById("resist_title").value;
    let resist_textarea=document.getElementById("resist_textarea").value;
    formData.append("event_name",event_name);
    formData.append("resist_textarea", resist_textarea);
    formData.append("movie_filename",event_file);
    console.log("movie_filename:"+ event_file);


    $.ajax({
        type:"POST",
        url:"/movie_resist/event_resist",
        data: formData,
        processData: false, // jQuery가 데이터를 처리하지 않도록 설정
        contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정

        success:function () {


        }

    })
}