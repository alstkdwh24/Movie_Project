document.addEventListener('DOMContentLoaded', (event) => {
    const modalOpenButton = document.querySelector('.modal_btn');
    const modalCloseButton = document.getElementById('modalCloseButton');
    const modal = document.getElementById('modalContainer');

    modalOpenButton.addEventListener('click', () => {
        modal.classList.remove('hidden');
    });

    modalCloseButton.addEventListener('click', () => {
        modal.classList.add('hidden');
    });
});