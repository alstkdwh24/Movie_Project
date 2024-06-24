let modal = document.getElementById('modalContainer');

document.addEventListener('DOMContentLoaded', (even) => {

    let modalOpenButton = document.querySelector('.modal_btn');
    let modalCloseButton = document.getElementById('modalCloseButton');
    modalOpenButton.addEventListener('click', () => {
        modal.classList.remove('hidden');
    });

    modalCloseButton.addEventListener('click', () => {
        modal.classList.add('hidden');
    });



});

window.onclick = function (event) {
    // 모달 내부를 클릭한 경우를 제외하고 모달을 닫음
    if (!modal.contains(event.target) && event.target !== modal) {
        modal.classList.add('hidden');
    }
}

