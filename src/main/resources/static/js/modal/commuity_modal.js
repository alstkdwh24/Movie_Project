

document.addEventListener('DOMContentLoaded', (event) => {



    const mos = document.querySelector(".modal_good");
    const communityButtons = document.querySelector(".con");

    communityButtons.onclick = function() {
        mos.style.display="flex";
        mos.style.justifyContent = "center";
        mos.style.alignItems = "center";
    }
    mos.onclick = function () {
        if (event.target === mos) {

            mos.style.display = "none";
        }
    }

})