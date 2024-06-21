const mo = document.querySelectorAll(".modal_good");

const modal_modal = document.querySelectorAll(".modalss");


document.addEventListener('DOMContentLoaded', (event) => {
    const bu = document.querySelectorAll(".community");
    const closeButtons = document.querySelectorAll(".close_closes");
    console.log("Number of buttons:", bu.length);
    console.log("Number of modals:", mo.length);
    console.log("Number of close buttons:", closeButtons.length);

    bu.forEach((button, index) => {
        if (index < mo.length) {
            button.onclick = function () {
                mo[index].style.display = "flex";
                mo[index].style.justifyContent = "center";
                mo[index].style.alignItems = "center";
            }
        }
    });

    closeButtons.forEach((close, index) => {
        if (index < mo.length) {
            close.onclick = function () {
                mo[index].style.display = "none";
            }
        }
    });

    // 모달 창 내부를 클릭했을 때 이벤트 전파를 막기
    mo.forEach((element) => {
        element.addEventListener('click', (event) => {
            const modalContent = element.querySelector('#mains_menus_modal');
            console.log('modalContent:', modalContent);
            if (modalContent && modalContent.contains(event.target)) {
                event.stopPropagation();
            }
        });
    });

    // 모달 창 외부를 클릭했을 때 모달 창 닫기
    // 모달 창 외부를 클릭했을 때 모달 창 닫기
    mo.forEach((element) => {
        element.addEventListener('click', (event) => {
            const modalContent = element.querySelector('#mains_menus_modal');
            console.log('modalContent:', modalContent);
            if (modalContent && modalContent.contains(event.target)) {
                event.stopPropagation();
            }
        });

        // 모달 창 외부를 클릭했을 때 모달 창 닫기
        element.addEventListener('click', (event) => {
            if (event.target === element) {
                element.style.display = "none";
            }
        });
    });
});
