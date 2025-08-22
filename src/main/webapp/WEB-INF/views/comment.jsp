<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="comment-area" class="mt-5">
    <h5 class="mb-3">댓글</h5>

    <!-- 댓글 작성 폼 -->
    <form id="commentWriteForm" class="mb-3 d-flex gap-2" onsubmit="return false;">
        <input type="text" id="commentText" class="form-control flex-grow-1" placeholder="댓글을 입력하세요." maxlength="500" required>
        <button type="submit" class="btn btn-primary px-4 text-nowrap flex-shrink-0">등록</button>
    </form>

    <!-- 댓글 목록 -->
    <ul id="commentList" class="list-group"></ul>

    <!-- 페일백 메시지 -->
    <div id="commentEmpty" class="text-muted mt-3" style="display:none;">등록된 댓글이 없습니다.</div>
</div>

<script>
    (() => {
        // ---------------- 기본 값 ----------------
        const ctx       = '<c:out value="${pageContext.request.contextPath}"/>'; // ex) /myno
        const bno       = '<c:out value="${param.bno}"/>'; // include로 넘어온 게시글 번호
        const hasCsrf   = ${not empty _csrf};              // boolean 으로 들어옴
        const csrfHeader= '<c:out value="${_csrf.headerName}"/>';
        const csrfToken = '<c:out value="${_csrf.token}"/>';

        // ⭐ 추가: 세션에서 로그인한 사용자의 이메일을 가져와 JS 변수에 저장
        // 이 변수는 JSP가 렌더링될 때 실제 이메일 주소로 치환됩니다.
        const loggedInUserEmail = '${sessionScope.email}';

        const listEl  = document.getElementById('commentList');
        const emptyEl = document.getElementById('commentEmpty');
        const inputEl = document.getElementById('commentText');
        const formEl  = document.getElementById('commentWriteForm');

        // 공통 fetch 옵션 (CSRF 헤더 자동 추가)
        const withCsrf = (opts = {}) => {
            const headers = new Headers(opts.headers || {});
            if (hasCsrf) headers.set(csrfHeader, csrfToken);
            return {...opts, headers};
        };

        // ---------------- API 경로 ----------------
        const baseBoard = ctx + '/board/' + bno + '/comments';
        const listUrl   = baseBoard;                    // GET
        const writeUrl  = baseBoard;                    // POST
        const patchUrl  = (cno) => ctx + '/comments/' + cno;                     // PATCH
        const deleteUrl = (cno) => ctx + '/board/' + bno + '/comments/' + cno;   // DELETE

        // ---------------- 렌더링 ----------------
        function render(comments) {
            listEl.innerHTML = '';
            if (!comments || comments.length === 0) {
                emptyEl.style.display = 'block';
                return;
            }
            emptyEl.style.display = 'none';

            comments.forEach(c => {
                const li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between align-items-start';
                li.dataset.cno = c.cno;

                // XSS 방지용 escape
                const esc = (s='') => s.replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));

                // ⭐ 댓글 작성자가 로그인한 사용자인지 확인
                const isMyComment = (c.commenter === loggedInUserEmail);
                let buttonGroupHtml = '';

                // ⭐ 조건부 렌더링: 작성자와 로그인 사용자가 일치할 때만 버튼을 표시
                if (isMyComment) {
                    buttonGroupHtml =
                        '<div class="btn-group btn-group-sm">' +
                        '<button type="button" class="btn btn-outline-secondary btn-edit">수정</button>' +
                        '<button type="button" class="btn btn-outline-danger btn-del">삭제</button>' +
                        '</div>';
                }

                li.innerHTML =
                    '<div class="me-3 flex-grow-1">' +
                    '<div class="fw-semibold">' + (c.commenter || '익명') + '</div>' +
                    '<div class="comment-text mt-1">' + esc(c.comment) + '</div>' +
                    '<div class="text-muted small mt-1">' + (c.updateDate || '') + '</div>' +
                    '</div>' +
                    buttonGroupHtml;

                listEl.appendChild(li);
            });
        }

        async function load() {
            try {
                const res = await fetch(listUrl, withCsrf());
                console.log('API Response Status:', res.status); // ⭐ 디버깅용 로그 추가
                if (!res.ok) throw new Error('API call failed with status ' + res.status);
                const data = await res.json();
                console.log('Fetched Comments Data:', data); // ⭐ 디버깅용 로그 추가
                render(data);
            } catch (e) {
                console.error(e);
                listEl.innerHTML = '';
                emptyEl.style.display = 'block';
                emptyEl.textContent = '댓글을 불러오는 중 오류가 발생했습니다.';
            }
        }

        // ---------------- 등록 ----------------
        formEl.addEventListener('submit', async () => {
            const txt = (inputEl.value || '').trim();
            if (!txt) return inputEl.focus();

            try {
                const res = await fetch(writeUrl, withCsrf({
                    method: 'POST',
                    headers: {'Content-Type':'application/json'},
                    body: JSON.stringify({ comment: txt })
                }));
                if (!res.ok) throw new Error('write failed');
                inputEl.value = '';
                await load();
            } catch (e) {
                alert('댓글 등록에 실패했습니다.');
                console.error(e);
            }
        });

        // ---------------- 수정/삭제 (이벤트 위임) ----------------
        listEl.addEventListener('click', async (e) => {
            const li = e.target.closest('li.list-group-item');
            if (!li) return;
            const cno = li.dataset.cno;

            // 삭제
            if (e.target.classList.contains('btn-del')) {
                if (!confirm('이 댓글을 삭제할까요?')) return;
                try {
                    const res = await fetch(deleteUrl(cno), withCsrf({ method: 'DELETE' }));
                    if (!res.ok) throw new Error('delete failed');
                    await load();
                } catch (err) {
                    alert('삭제 실패');
                    console.error(err);
                }
                return;
            }

            // 수정
            if (e.target.classList.contains('btn-edit')) {
                const textDiv = li.querySelector('.comment-text');
                const original = textDiv.textContent;

                textDiv.innerHTML =
                    '<input class="form-control form-control-sm" value="' +
                    original.replace(/"/g, '&quot;') + '" />';

                const input = textDiv.querySelector('input');
                input.focus();

                const btnGroup = li.querySelector('.btn-group');
                btnGroup.innerHTML =
                    '<button type="button" class="btn btn-primary btn-save">저장</button>' +
                    '<button type="button" class="btn btn-secondary btn-cancel">취소</button>';

                // 저장
                btnGroup.querySelector('.btn-save').addEventListener('click', async () => {
                    const newTxt = (input.value || '').trim();
                    if (!newTxt) return alert('내용을 입력하세요.');
                    try {
                        const res = await fetch(patchUrl(cno), withCsrf({
                            method: 'PATCH',
                            headers: {'Content-Type':'application/json'},
                            body: JSON.stringify({ comment: newTxt })
                        }));
                        if (!res.ok) throw new Error('patch failed');
                        await load();
                    } catch (err) {
                        alert('수정 실패');
                        console.error(err);
                    }
                });

                // 취소
                btnGroup.querySelector('.btn-cancel').addEventListener('click', () => {
                    textDiv.textContent = original;
                    btnGroup.innerHTML =
                        '<button type="button" class="btn btn-outline-secondary btn-edit">수정</button>' +
                        '<button type="button" class="btn btn-outline-danger btn-del">삭제</button>';
                });
            }
        });

        // 초기 로드
        load();
    })();
</script>