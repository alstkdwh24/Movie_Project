document.addEventListener('DOMContentLoaded', (event) => {
    const button_modal = document.querySelector(".momo_button");
    const con_close = document.querySelector(".close_lo");
    const momo_modal = document.querySelector(".momo_container");

    // 모달 열기 버튼 클릭 시 모달 표시
    button_modal.onclick = function() {
        momo_modal.style.display = "flex";
    }

    // 모달 닫기 버튼 클릭 시 모달 숨김
    con_close.onclick = function() {
        momo_modal.style.display = "none";
    }

    // 모달 외부 클릭 시 모달 숨김
    window.onclick = function(event) {
        if (momo_modal && event.target === momo_modal) {
            console.log("Window clicked outside momo_modal");
            momo_modal.style.display = "none";
        }
    }
});
