document.addEventListener('DOMContentLoaded', () => {
    const button_modal = document.querySelector(".momo_button");
    const con_close = document.querySelector(".close_lo");
    const momo_modal = document.querySelector(".momo_container");

    // 헤더 검색 버튼이나 모달이 없는 페이지에서는 아무것도 하지 않는다
    if (!button_modal || !con_close || !momo_modal) return;

    const input = momo_modal.querySelector(".momo_input");
    const results = momo_modal.querySelector(".momo_results");
    const label = momo_modal.querySelector(".momo_label");

    // 검색 대상: 사이트 메뉴. keywords 에 있는 말로도 찾는다
    const PAGES = [
        {name: "영화 예매", href: "/movie/Reservation/reservation", keywords: "예매 예약 티켓 좌석 영화"},
        {name: "이벤트", href: "/movie/community/event", keywords: "이벤트 혜택 행사"},
        {name: "스토어", href: "/movie/shops/popcorn_store", keywords: "스토어 매점 팝콘 콤보 기프트 관람권"},
        {name: "자유게시판", href: "/movie/community/freeboard", keywords: "자유 게시판 커뮤니티 글"},
        {name: "공지사항", href: "/movie/community/gboard", keywords: "공지 게시판 커뮤니티"},
        {name: "고객 문의", href: "/movie/chats/Question", keywords: "문의 질문 고객 1:1"},
        {name: "마이페이지", href: "/movie/user/Mypage", keywords: "마이 계정 내정보 회원"},
        {name: "구매내역", href: "/movie/user/myproduct", keywords: "구매 결제 내역 주문"},
        {name: "로그인", href: "/movie/login/login", keywords: "로그인 로그 인"},
        {name: "회원가입", href: "/movie/login/join", keywords: "회원 가입 계정 만들기"}
    ];

    function match(keyword) {
        const key = keyword.trim().toLowerCase();
        if (!key) return PAGES;
        return PAGES.filter(p => p.name.toLowerCase().includes(key) || p.keywords.includes(key));
    }

    function render() {
        const keyword = input.value;
        const found = match(keyword);
        label.textContent = keyword.trim() ? "검색 결과 " + found.length : "바로가기";
        results.replaceChildren();
        if (found.length === 0) {
            const none = document.createElement("li");
            none.className = "momo_none";
            none.textContent = "일치하는 메뉴가 없습니다.";
            results.appendChild(none);
            return;
        }
        found.forEach(page => {
            const li = document.createElement("li");
            const a = document.createElement("a");
            a.href = page.href;
            a.textContent = page.name;
            li.appendChild(a);
            results.appendChild(li);
        });
    }

    function open() {
        momo_modal.style.display = "flex";
        input.value = "";
        render();
        input.focus();
    }

    function close() {
        momo_modal.style.display = "none";
    }

    button_modal.onclick = open;
    con_close.onclick = close;
    // 모달 바깥을 누르면 닫기
    momo_modal.onclick = event => {
        if (event.target === momo_modal) close();
    };
    input.addEventListener("input", render);
    // Enter: 첫 결과로 이동, Esc: 닫기
    momo_modal.querySelector(".momo_search").addEventListener("submit", event => {
        event.preventDefault();
        const first = results.querySelector("a");
        if (first) location.href = first.href;
    });
    document.addEventListener("keydown", event => {
        if (event.key === "Escape" && momo_modal.style.display === "flex") close();
    });
});
