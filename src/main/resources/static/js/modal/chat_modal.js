document.addEventListener('DOMContentLoaded', () => {
    const modalOpenButton = document.querySelector(".modalOpenButton");
    const modalCloseButton = document.querySelector('.chat_modal_button');
    const chat_modals = document.querySelector(".chat_modals");

    if (!chat_modals || !modalOpenButton || !modalCloseButton) {
        console.error("채팅 모달 요소를 찾지 못했습니다.");
        return;
    }

    // ---- 데이터: 채팅 서버(API)가 생기면 이 부분만 응답으로 바꾸면 된다. 지금은 화면에서만 유지된다 ----
    const friends = [
        {name: "김영화", status: "오늘 개봉작 보러 갑니다", fav: false},
        {name: "박팝콘", status: "", fav: false},
        {name: "이시네마", status: "주말 예매 같이 해요", fav: false},
        {name: "최스크린", status: "", fav: false}
    ];
    const blocked = [];   // 차단한 친구
    const rooms = [
        {name: "주말 영화 모임", last: "토요일 3시 괜찮아요?", time: "오후 2:41", unread: 3},
        {name: "예매 같이 해요", last: "좌석 붙어서 잡았어요", time: "오전 11:08", unread: 0},
        {name: "개봉작 수다방", last: "엔딩 보셨어요?", time: "어제", unread: 12}
    ];
    const movieRooms = [
        {name: "어벤져스: 엔드게임", last: "쿠키 영상 몇 개예요?", time: "오후 1:20", unread: 5},
        {name: "반지의 제왕", last: "확장판 추천해요", time: "어제", unread: 0},
        {name: "너의 이름은.", last: "OST 다들 들어보셨죠", time: "월요일", unread: 0}
    ];
    const settings = {notify: true};

    // 샘플 방은 마지막 메시지를 대화 한 줄로 시작한다. 새로 만든 방은 빈 대화.
    [...rooms, ...movieRooms].forEach(r => {
        r.messages = [{me: false, who: r.name, text: r.last, time: r.time}];
    });

    const COLORS = ["#f5a623", "#7ed321", "#4a90e2", "#bd10e0", "#50e3c2", "#e8505b", "#9b9b9b"];
    const TITLES = {friends: "친구목록", rooms: "채팅방", movie: "영화채팅방", more: "기타"};

    // ---- 작은 도우미 ----
    function colorOf(name) {
        let sum = 0;
        for (const ch of name) sum += ch.charCodeAt(0);
        return COLORS[sum % COLORS.length];
    }

    function el(tag, className, text) {
        const node = document.createElement(tag);
        if (className) node.className = className;
        if (text) node.textContent = text;   // 사용자 입력도 textContent로만 넣는다
        return node;
    }

    function avatar(name, round) {
        const node = el("div", "chat_avatar" + (round ? " is-round" : ""), name.charAt(0));
        node.style.background = colorOf(name);
        return node;
    }

    function empty(text) {
        return el("p", "chat_empty", text);
    }

    function nowLabel() {
        const d = new Date();
        const h = d.getHours();
        return (h < 12 ? "오전 " : "오후 ") + ((h % 12) || 12) + ":" + String(d.getMinutes()).padStart(2, "0");
    }

    const q = selector => chat_modals.querySelector(selector);
    const panels = {};
    chat_modals.querySelectorAll(".chat_panel").forEach(p => panels[p.dataset.panel] = p);
    const tabs = chat_modals.querySelectorAll(".chat_tab");
    const title = q(".chat_title");

    // ---- 친구목록 ----
    const friendsList = q(".chat_friends_list");
    const friendSearch = q(".chat_search input");

    function friendRow(friend) {
        const row = el("li", "chat_row");
        row.appendChild(avatar(friend.name, true));
        const body = el("div", "chat_row_body");
        body.appendChild(el("strong", "chat_row_name", friend.name));
        if (friend.status) body.appendChild(el("span", "chat_row_last", friend.status));
        row.appendChild(body);
        row.addEventListener("click", () => openProfile(friend.name));
        return row;
    }

    function friendSection(label, list) {
        const frag = document.createDocumentFragment();
        frag.appendChild(el("p", "chat_section", label + " " + list.length));
        const ul = el("ul", "chat_list");
        list.forEach(f => ul.appendChild(friendRow(f)));
        frag.appendChild(ul);
        return frag;
    }

    function renderFriends() {
        friendsList.replaceChildren();
        const me = el("ul", "chat_list");
        const meRow = el("li", "chat_row is-me");
        meRow.appendChild(avatar("나", true));
        const meBody = el("div", "chat_row_body");
        meBody.appendChild(el("strong", "chat_row_name", "나"));
        meBody.appendChild(el("span", "chat_row_last", "상태 메시지를 입력하세요"));
        meRow.appendChild(meBody);
        me.appendChild(meRow);
        friendsList.appendChild(me);

        const keyword = friendSearch.value.trim();
        const visible = friends.filter(f => f.name.includes(keyword));
        if (visible.length === 0) {
            friendsList.appendChild(empty(keyword ? "검색 결과가 없습니다." : "친구가 없습니다."));
            return;
        }
        const favorites = visible.filter(f => f.fav);
        const others = visible.filter(f => !f.fav);
        if (favorites.length > 0) friendsList.appendChild(friendSection("즐겨찾기", favorites));
        if (others.length > 0) friendsList.appendChild(friendSection("친구", others));
    }

    friendSearch.addEventListener("input", renderFriends);

    // ---- 채팅방 목록 (상단 고정이 먼저, 나머지는 기존 순서) ----
    function roomRow(room) {
        const row = el("li", "chat_row");
        row.appendChild(avatar(room.name, false));
        const body = el("div", "chat_row_body");
        const nameLine = el("div", "chat_row_nameline");
        nameLine.appendChild(el("strong", "chat_row_name", room.name));
        if (room.muted) nameLine.appendChild(el("span", "chat_flag", "알림 꺼짐"));
        if (room.pinned) nameLine.appendChild(el("span", "chat_flag is-pin", "고정"));
        body.appendChild(nameLine);
        body.appendChild(el("span", "chat_row_last", room.last));
        row.appendChild(body);
        const meta = el("div", "chat_row_meta");
        meta.appendChild(el("span", "chat_row_time", room.time));
        if (room.unread > 0) {
            const silent = room.muted || !settings.notify;
            meta.appendChild(el("span", "chat_badge" + (silent ? " is-muted" : ""), room.unread > 99 ? "99+" : String(room.unread)));
        }
        row.appendChild(meta);
        row.addEventListener("click", () => openRoom(room));
        return row;
    }

    function renderRooms(panel, list, emptyText) {
        panel.replaceChildren();
        if (list.length === 0) {
            panel.appendChild(empty(emptyText));
            return;
        }
        const sorted = [...list.filter(r => r.pinned), ...list.filter(r => !r.pinned)];
        const ul = el("ul", "chat_list");
        sorted.forEach(room => ul.appendChild(roomRow(room)));
        panel.appendChild(ul);
    }

    // ---- 기타: 알림 설정 / 차단한 친구 / 이용 안내 ----
    const moreBody = q(".chat_more_body");
    let moreView = "main";

    function moreItem(label, desc, onClick) {
        const item = el("button", "chat_more_item");
        item.type = "button";
        const text = el("div", "chat_more_text");
        text.appendChild(el("strong", "", label));
        if (desc) text.appendChild(el("span", "", desc));
        item.appendChild(text);
        item.addEventListener("click", onClick);
        return item;
    }

    function renderMore() {
        moreBody.replaceChildren();
        if (moreView === "blocked") {
            const back = el("button", "chat_more_back", "‹ 차단한 친구 " + blocked.length);
            back.type = "button";
            back.addEventListener("click", () => {
                moreView = "main";
                renderMore();
            });
            moreBody.appendChild(back);
            if (blocked.length === 0) {
                moreBody.appendChild(empty("차단한 친구가 없습니다."));
                return;
            }
            const ul = el("ul", "chat_list");
            blocked.forEach(friend => {
                const row = el("li", "chat_row");
                row.appendChild(avatar(friend.name, true));
                const body = el("div", "chat_row_body");
                body.appendChild(el("strong", "chat_row_name", friend.name));
                row.appendChild(body);
                const unblock = el("button", "chat_unblock", "차단 해제");
                unblock.type = "button";
                unblock.addEventListener("click", () => unblockFriend(friend.name));
                row.appendChild(unblock);
                ul.appendChild(row);
            });
            moreBody.appendChild(ul);
            return;
        }

        const notify = moreItem("전체 알림", settings.notify ? "켜짐" : "꺼짐", () => {
            settings.notify = !settings.notify;
            renderMore();
            render();
        });
        const sw = el("span", "chat_switch" + (settings.notify ? " is-on" : ""));
        sw.setAttribute("role", "switch");
        sw.setAttribute("aria-checked", String(settings.notify));
        notify.appendChild(sw);
        moreBody.appendChild(notify);

        moreBody.appendChild(moreItem("차단한 친구", blocked.length + "명", () => {
            moreView = "blocked";
            renderMore();
        }));
        moreBody.appendChild(moreItem("채팅 이용 안내", "욕설·광고·개인정보 요구는 신고 대상입니다.", () => {
        }));
    }

    function render() {
        renderFriends();
        renderRooms(q(".chat_rooms_list"), rooms, "참여 중인 채팅방이 없습니다.");
        renderRooms(panels.movie, movieRooms, "영화 채팅방이 없습니다.");
        renderMore();
    }

    // ---- 탭 ----
    function showTab(name) {
        Object.keys(panels).forEach(key => panels[key].classList.toggle("is-active", key === name));
        tabs.forEach(tab => {
            const active = tab.dataset.tab === name;
            tab.classList.toggle("is-active", active);
            tab.setAttribute("aria-current", active ? "page" : "false");
        });
        title.textContent = TITLES[name];
    }

    tabs.forEach(tab => tab.addEventListener("click", () => showTab(tab.dataset.tab)));

    // ---- 차단 / 해제 ----
    function blockFriend(name) {
        const index = friends.findIndex(f => f.name === name);
        if (index < 0) return;
        const friend = friends.splice(index, 1)[0];
        friend.fav = false;
        blocked.push(friend);
        // 차단한 친구와의 1:1 방은 목록에서 없앤다
        [rooms, movieRooms].forEach(list => {
            for (let i = list.length - 1; i >= 0; i--) {
                if (list[i].friend === name) list.splice(i, 1);
            }
        });
        render();
    }

    function unblockFriend(name) {
        const index = blocked.findIndex(f => f.name === name);
        if (index < 0) return;
        friends.push(blocked.splice(index, 1)[0]);
        render();
    }

    // ---- 채팅방 화면 ----
    const roomView = q(".chat_room");
    const roomMenu = q(".chat_room_menu");
    const roomMore = q(".chat_room_more");
    const messagesBox = q(".chat_messages");
    const roomForm = q(".chat_input");
    const roomInput = roomForm.querySelector("input");
    let currentRoom = null;

    function messageRow(msg) {
        const row = el("div", "chat_msg" + (msg.me ? " is-me" : ""));
        if (!msg.me) row.appendChild(avatar(msg.who, false));
        const col = el("div", "chat_msg_col");
        if (!msg.me) col.appendChild(el("span", "chat_msg_who", msg.who));
        const line = el("div", "chat_msg_line");
        line.appendChild(el("span", "chat_bubble", msg.text));
        line.appendChild(el("span", "chat_msg_time", msg.time));
        col.appendChild(line);
        row.appendChild(col);
        return row;
    }

    function renderMessages() {
        messagesBox.replaceChildren();
        if (currentRoom.messages.length === 0) {
            messagesBox.appendChild(empty("대화를 시작해 보세요."));
            return;
        }
        currentRoom.messages.forEach(msg => messagesBox.appendChild(messageRow(msg)));
        messagesBox.scrollTop = messagesBox.scrollHeight;
    }

    function closeRoomMenu() {
        roomMenu.hidden = true;
        roomMore.setAttribute("aria-expanded", "false");
    }

    function openRoomMenu() {
        roomMenu.querySelector('[data-action="mute"]').textContent = currentRoom.muted ? "알림 켜기" : "알림 끄기";
        roomMenu.querySelector('[data-action="pin"]').textContent = currentRoom.pinned ? "상단 고정 해제" : "상단 고정";
        roomMenu.querySelector('[data-action="block"]').hidden = !currentRoom.friend;   // 1:1 방에서만
        roomMenu.hidden = false;
        roomMore.setAttribute("aria-expanded", "true");
    }

    function openRoom(room) {
        currentRoom = room;
        room.unread = 0;
        q(".chat_room_title").textContent = room.name;
        renderMessages();
        closeRoomMenu();
        roomView.hidden = false;
        roomInput.focus();
    }

    function closeRoom() {
        closeRoomMenu();
        roomView.hidden = true;
        currentRoom = null;
        roomInput.value = "";
        render();   // 마지막 메시지·읽음·고정 상태를 목록에 반영
    }

    roomMore.addEventListener("click", () => roomMenu.hidden ? openRoomMenu() : closeRoomMenu());
    q(".chat_room_back").addEventListener("click", closeRoom);

    roomMenu.addEventListener("click", event => {
        const button = event.target.closest("button[data-action]");
        if (!button || !currentRoom) return;
        const action = button.dataset.action;
        closeRoomMenu();
        if (action === "mute") {
            currentRoom.muted = !currentRoom.muted;
        } else if (action === "pin") {
            currentRoom.pinned = !currentRoom.pinned;
        } else if (action === "block") {
            if (!window.confirm(currentRoom.friend + " 님을 차단할까요?\n차단하면 친구목록과 채팅방 목록에서 사라집니다.")) return;
            const name = currentRoom.friend;
            currentRoom = null;
            roomView.hidden = true;
            blockFriend(name);
        } else if (action === "leave") {
            if (!window.confirm("채팅방을 나갈까요?\n대화 내용이 모두 사라집니다.")) return;
            [rooms, movieRooms].forEach(list => {
                const i = list.indexOf(currentRoom);
                if (i >= 0) list.splice(i, 1);
            });
            currentRoom = null;
            roomView.hidden = true;
            render();
        }
    });

    roomForm.addEventListener("submit", event => {
        event.preventDefault();
        const text = roomInput.value.trim();
        if (!text || !currentRoom) return;
        const time = nowLabel();
        currentRoom.messages.push({me: true, who: "나", text: text, time: time});
        currentRoom.last = text;
        currentRoom.time = time;
        roomInput.value = "";
        renderMessages();
    });

    // ---- 친구 프로필 → 채팅하기 / 즐겨찾기 / 차단하기 ----
    const profile = q(".chat_profile");
    const profileAvatar = profile.querySelector(".chat_profile_avatar");
    const favButton = profile.querySelector(".chat_profile_fav");
    let profileFriend = null;

    function paintFav() {
        const on = !!(profileFriend && profileFriend.fav);
        favButton.classList.toggle("is-on", on);
        favButton.setAttribute("aria-pressed", String(on));
        favButton.setAttribute("aria-label", on ? "즐겨찾기 해제" : "즐겨찾기");
    }

    function openProfile(name) {
        const friend = friends.find(f => f.name === name);
        if (!friend) return;
        profileFriend = friend;
        profileAvatar.textContent = friend.name.charAt(0);
        profileAvatar.style.background = colorOf(friend.name);
        profile.querySelector(".chat_profile_name").textContent = friend.name;
        profile.querySelector(".chat_profile_status").textContent = friend.status || "";
        paintFav();
        profile.hidden = false;
        profile.querySelector(".chat_profile_chat").focus();
    }

    function closeProfile() {
        profile.hidden = true;
        profileFriend = null;
    }

    // 이미 있는 1:1 방이 있으면 그 방을 맨 위로, 없으면 새로 만든다
    function startChat(friend) {
        const index = rooms.findIndex(r => r.friend === friend.name);
        const room = index >= 0
            ? rooms.splice(index, 1)[0]
            : {name: friend.name, friend: friend.name, last: "대화를 시작해 보세요.", time: "방금", unread: 0, messages: []};
        rooms.unshift(room);
        render();
        closeProfile();
        showTab("rooms");
    }

    profile.querySelector(".chat_profile_close").addEventListener("click", closeProfile);
    profile.querySelector(".chat_profile_chat").addEventListener("click", () => {
        if (profileFriend) startChat(profileFriend);
    });
    favButton.addEventListener("click", () => {
        if (!profileFriend) return;
        profileFriend.fav = !profileFriend.fav;
        paintFav();
        renderFriends();
    });
    profile.querySelector(".chat_profile_block").addEventListener("click", () => {
        if (!profileFriend) return;
        if (!window.confirm(profileFriend.name + " 님을 차단할까요?\n차단하면 친구목록과 채팅방 목록에서 사라집니다.")) return;
        const name = profileFriend.name;
        closeProfile();
        blockFriend(name);
    });

    // ---- 방 만들기: 서버 저장 없이 화면 목록에만 추가 ----
    const form = q(".chat_create");
    const newButton = q(".chat_new");
    newButton.addEventListener("click", () => {
        form.hidden = !form.hidden;
        newButton.setAttribute("aria-expanded", String(!form.hidden));
        if (!form.hidden) form.querySelector("input").focus();
    });
    form.addEventListener("submit", event => {
        event.preventDefault();
        const input = form.querySelector("input");
        const name = input.value.trim();
        if (!name) {
            input.focus();
            return;
        }
        rooms.unshift({name: name, last: "채팅방이 만들어졌습니다.", time: "방금", unread: 0, messages: []});
        input.value = "";
        form.hidden = true;
        newButton.setAttribute("aria-expanded", "false");
        render();
    });

    // ---- 모달 열기·닫기 ----
    function openModal() {
        chat_modals.style.display = "flex";
        showTab("rooms");
    }

    function closeModal() {
        closeProfile();
        if (currentRoom) closeRoom();
        chat_modals.style.display = "none";
    }

    modalOpenButton.onclick = openModal;
    modalCloseButton.onclick = closeModal;
    // 모달 바깥(어두운 영역)을 누르면 닫기
    chat_modals.onclick = event => {
        if (event.target === chat_modals) closeModal();
    };
    // Esc: 열려 있는 가장 안쪽 화면부터 하나씩 닫는다
    document.addEventListener("keydown", event => {
        if (event.key !== "Escape" || chat_modals.style.display !== "flex") return;
        if (!profile.hidden) closeProfile();
        else if (!roomMenu.hidden) closeRoomMenu();
        else if (currentRoom) closeRoom();
        else closeModal();
    });

    render();
    showTab("rooms");
});
