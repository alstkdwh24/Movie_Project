document.addEventListener('DOMContentLoaded', (event) => {
    const galleryModals = document.querySelector(".m_c");
    const communityButtons = document.querySelector(".con");
    const closeButtons = document.querySelector(".c_c");

    // communityButton 클릭 시 galleryModal을 보여줌
    communityButtons.onclick = function() {
        galleryModals.style.display = "flex";
        console.log(1);
    }

    // closeButton 클릭 시 galleryModal을 숨김
    closeButtons.onclick = function() {
        galleryModals.style.display = "none";
    }

    // galleryModal 외부를 클릭했는지 확인하고 숨김
    galleryModals.onclick = function(event) {
        if (event.target === galleryModals) {
            console.log("Window clicked outside galleryModal");
            galleryModals.style.display = "none";
        }
    }
});
