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
window.onclick = function (even){
    if(even.target===modal){
        modal.classList.add('hidden');
    }}


