

document.addEventListener('DOMContentLoaded', (event) => {
    const modalOpenButton = document.querySelector(".modalOpenButton");
    const modalCloseButton = document.querySelector('.chat_modal_button');
    const chat_modals = document.querySelector(".chat_modals");

    // 요소가 제대로 선택되었는지 확인
    console.log(chat_modals); // null이 아니어야 함
    console.log(modalOpenButton); // null이 아니어야 함
    console.log(modalCloseButton); // null이 아니어야 함

    // 요소가 존재하는지 확인 후 이벤트 리스너 설정
    if (chat_modals && modalOpenButton && modalCloseButton) {
        modalOpenButton.onclick = function() {
            console.log("Modal open button clicked");
            chat_modals.style.display = "flex";
        }

        modalCloseButton.onclick = function() {
            console.log("Modal close button clicked");
            chat_modals.style.display = "none";
        }

        // 모달 외부를 클릭했을 때 모달을 닫기 위한 이벤트 리스너
        window.onclick = function(event) {
            if (event.target === chat_modals) {
                console.log("Clicked outside of modal contents");
                chat_modals.style.display = "none";
            }
        }


    } else {
        console.error("One or more elements not found");
    }
});

