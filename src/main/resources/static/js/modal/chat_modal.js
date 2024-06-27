const chat_modal = document.querySelector(".chat_modal");


document.addEventListener('DOMContentLoaded', (event) => {
    const modalOpenButton = document.getElementById("menu1");
    const modalCloseButton = document.querySelector('.chat_modal_button');
    const modalContent1 = document.querySelector('.modal_modal');

    // 요소가 제대로 선택되었는지 확인
    console.log(chat_modal); // null이 아니어야 함
    console.log(modalOpenButton); // null이 아니어야 함
    console.log(modalCloseButton); // null이 아니어야 함

    // 요소가 존재하는지 확인 후 이벤트 리스너 설정
    if (chat_modal && modalOpenButton && modalCloseButton) {
        modalOpenButton.onclick = function() {
            console.log("Modal open button clicked");
            chat_modal.style.display = "flex";
        }

        modalCloseButton.onclick = function() {
            console.log("Modal close button clicked");
            chat_modal.style.display = "none";
        }

        // 모달 외부를 클릭했을 때 모달을 닫기 위한 이벤트 리스너
        window.onclick = function(event) {
            if (event.target === chat_modal) {
                console.log("Clicked outside of modal contents");
                chat_modal.style.display = "none";
            }
        }

        // 모달 내부 클릭 시 이벤트 전파 중지
        modalContent1.onclick = function(event) {
            event.stopPropagation();
        }
    } else {
        console.error("One or more elements not found");
    }
});

