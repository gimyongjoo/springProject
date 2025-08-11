<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${note.title} - 노트 상세</title>
    <style>
        /* 기본 스타일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 960px;
            margin: 40px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        h2 {

        }

        h3 {
            color: #34495e;
            margin-top: 30px;
            margin-bottom: 15px;
            border-bottom: 1px solid #eee;
            padding-bottom: 5px;
        }

        p {
            margin-bottom: 1em;
            word-wrap: break-word; /* 긴 텍스트 줄바꿈 */
        }

        .note-info {
            font-size: 0.9em;
            color: #777;
            margin-bottom: 20px;
        }

        .note-info span {
            margin-right: 15px;
        }

        .note-title {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        .note-content {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #eee;
            min-height: 150px; /* 내용이 적어도 일정 높이를 가지도록 */
            white-space: pre-wrap; /* 줄바꿈 유지 */
            word-wrap: break-word;
        }

        /* 버튼 그룹 */
        .button-group {
            margin-top: 30px;
            display: flex;
            gap: 10px;
            justify-content: flex-end; /* 오른쪽 정렬 */
        }

        /* 버튼 통일 스타일 */
        .button-group .btn,
        .button-group button.btn {
            display: inline-block;
            height: 44px;
            min-width: 90px;
            padding: 0 20px;
            line-height: 44px;
            border-radius: 5px;
            font-size: 1em;
            font-weight: bold;
            border: none;
            cursor: pointer;
            text-align: center;
            vertical-align: middle;
            box-sizing: border-box;
            background-color: #3498db;
            color: white;
            transition: background-color 0.3s;
            margin: 0;
            text-decoration: none;
        }

        .btn-delete {
            background-color: red !important;
        }

        .btn-delete:hover {
            background-color: #c0392b !important;
        }

        .button-group .btn:hover,
        .button-group button.btn:hover {
            background-color: #2980b9;
        }

        .button-group .btn-secondary {
            background-color: #6c757d;
        }
        .button-group .btn-secondary:hover {
            background-color: #5a6268;
        }

        .button-group .btn-delete {
            background-color: #e74c3c;
        }
        .button-group .btn-delete:hover {
            background-color: #c0392b;
        }

        .button-group form {
            display: inline;
            margin: 0;
            padding: 0;
        }

        /* --- Todo List Styles --- */
        .todo-list {
            list-style: none;
            padding: 0;
            margin-top: 15px;
        }

        .todo-list li {
            display: flex;
            align-items: center;
            background-color: #fefefe;
            padding: 10px 15px;
            border-bottom: 1px solid #eee;
            margin-bottom: 5px;
            border-radius: 5px;
        }

        .todo-list li:last-child {
            border-bottom: none;
        }

        .todo-list .todo-checkbox {
            margin-right: 10px;
            transform: scale(1.2); /* 체크박스 크기 키우기 */
        }

        .todo-list .todo-content {
            flex-grow: 1; /* 내용이 남은 공간 차지 */
            word-break: break-all; /* 긴 단어도 줄바꿈 */
        }

        .todo-list .todo-completed {
            text-decoration: line-through;
            color: #888;
        }

        .todo-list .todo-actions {
            margin-left: 10px;
            display: flex;
            gap: 5px;
        }

        .todo-list .todo-actions a {
            font-size: 0.8em;
            padding: 3px 8px;
            border-radius: 3px;
            text-decoration: none;
            color: white;
            background-color: #6c757d; /* 수정 버튼 색 */
        }

        .todo-list .todo-actions a.delete-todo-btn {
            background-color: #e74c3c; /* 삭제 버튼 색 */
        }

        .todo-input-group {
            display: flex;
            margin-top: 15px;
        }

        .todo-input-group input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px 0 0 5px;
            font-size: 1em;
        }

        .todo-input-group button {
            padding: 10px 15px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 0 5px 5px 0;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .todo-input-group button:hover {
            background-color: #2980b9;
        }

        /* --- Checklist Styles --- */
        .checklist-container {
            margin-top: 15px;
            border-top: 1px solid #eee;
            padding-top: 15px;
        }

        .checklist-list {
            list-style: none;
            padding: 0;
            margin-top: 10px;
        }

        .checklist-list li {
            display: flex;
            align-items: center;
            background-color: #fefefe;
            padding: 10px 15px;
            border-bottom: 1px solid #eee;
            margin-bottom: 5px;
            border-radius: 5px;
        }

        .checklist-list li:last-child {
            border-bottom: none;
        }

        .checklist-list .checklist-checkbox {
            margin-right: 10px;
            transform: scale(1.2);
        }

        .checklist-list .checklist-content {
            flex-grow: 1;
            word-break: break-all;
            cursor: pointer; /* 클릭해서 편집 가능하도록 */
        }

        .checklist-list .checklist-content.checked {
            text-decoration: line-through;
            color: #888;
        }

        .checklist-list .checklist-actions {
            margin-left: 10px;
            display: flex;
            gap: 5px;
        }

        .checklist-list .checklist-actions a {
            font-size: 0.8em;
            padding: 3px 8px;
            border-radius: 3px;
            text-decoration: none;
            color: white;
            background-color: #6c757d;
        }

        .checklist-list .checklist-actions a.delete-checklist-btn {
            background-color: #e74c3c;
        }

        .checklist-input-group {
            display: flex;
            margin-top: 15px;
        }

        .checklist-input-group input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px 0 0 5px;
            font-size: 1em;
        }

        .checklist-input-group button {
            padding: 10px 15px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 0 5px 5px 0;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .checklist-input-group button:hover {
            background-color: #2980b9;
        }

        .note-content img {
            max-width: 100%;
            height: auto;
            display: block;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<%@ include file="navi.jsp" %>
<div class="container">
    <c:if test="${not empty errorMessage}">
        <script>
            alert("${errorMessage}");
        </script>
    </c:if>
    <c:if test="${not empty successMessage}">
        <script>
            alert("${successMessage}");
        </script>
    </c:if>

    <h2 class="note-title">${note.title}</h2>

    <div class="note-info">
        <span>
            생성일: <fmt:parseDate value="${note.createdDate}" pattern="yyyy-MM-dd'T'HH:mm" var="createdDateObj"/>
            <fmt:formatDate value="${createdDateObj}" pattern="yyyy년 MM월 dd일 HH:mm"/>
        </span>
        <span>
            수정일: <fmt:parseDate value="${note.updatedDate}" pattern="yyyy-MM-dd'T'HH:mm" var="updatedDateObj"/>
            <fmt:formatDate value="${updatedDateObj}" pattern="yyyy년 MM월 dd일 HH:mm"/>
        </span>

        <span>
            핀고정:
            <c:choose>
                <c:when test="${note.isPinned}">
                    <span style="font-family: 'Apple Color Emoji', 'Segoe UI Emoji', 'Noto Color Emoji', 'Arial', sans-serif;">📌</span>
                </c:when>
                <c:otherwise>
                    <span style="font-family: 'Apple Color Emoji', 'Segoe UI Emoji', 'Noto Color Emoji', 'Arial', sans-serif;">❌</span>
                </c:otherwise>
            </c:choose>
        </span>
        <c:if test="${not empty note.folderId}">
            <span>폴더: ${folderName}</span>
        </c:if>
    </div>

    <div class="note-content">
        <c:out value="${htmlContent}" escapeXml="false"/>
    </div>

    <div class="button-group">
        <a href="<c:url value="/note/edit?noteId=${note.noteId}" />" class="btn">수정</a>
        <form id="deleteNoteForm" action="<c:url value='/note/delete'/>" method="post" style="display: inline;">
            <input type="hidden" name="noteId" value="${note.noteId}" />
            <button type="submit" class="btn btn-delete"
                    onclick="return confirm('정말 이 노트를 삭제하시겠습니까?');">삭제</button>
        </form>
        <a href="<c:url value='/note/list?page=${page}&folderId=${folderId}'/>" class="btn btn-secondary">목록으로</a>
    </div>

    <hr/>

    <h3>할 일</h3>
    <ul id="todo-list-container" class="todo-list">
        <c:forEach var="todo" items="${todos}">
            <li>
                <input type="checkbox" class="todo-checkbox"
                       onclick="location.href='<c:url
                               value="/todo/toggleDone?todoId=${todo.todoId}&noteId=${note.noteId}"/>'"
                       <c:if test="${todo.done}">checked</c:if>>
                <span class="todo-content <c:if test="${todo.done}">todo-completed</c:if>">${todo.content}</span>
                <div class="todo-actions">
                    <a href="<c:url value="/todo/delete?todoId=${todo.todoId}&noteId=${note.noteId}"/>"
                       class="btn delete-todo-btn" onclick="return confirm('이 할 일을 삭제하시겠습니까?');">삭제</a>
                </div>
            </li>
        </c:forEach>
        <c:if test="${empty todos}">
            <li>아직 할 일이 없습니다. 새로운 할 일을 추가해보세요!</li>
        </c:if>
    </ul>

    <%-- 새 할 일 추가 폼 --%>
    <form action="<c:url value="/todo/add"/>" method="post" class="todo-input-group">
        <input type="hidden" name="noteId" value="${note.noteId}">
        <input type="text" name="content" placeholder="새 할 일 추가" required>
        <button type="submit">추가</button>
    </form>

    <hr/>

    <%-- --- 체크리스트(Checklist) 섹션 --- --%>
    <h3>체크리스트</h3>
    <div class="checklist-container">
        <ul id="checklist-list-container" class="checklist-list">
            <%-- 체크리스트는 JavaScript로 동적으로 로드 및 렌더링됩니다. --%>
        </ul>

        <div class="checklist-input-group">
            <input type="text" id="newChecklistContent" placeholder="새 체크리스트 항목 추가" required>
            <button type="button" id="addChecklistBtn">추가</button>
        </div>
    </div>

</div>

<%@ include file="footer.jsp" %>

<script>
    // 체크리스트 관련 JavaScript
    document.addEventListener('DOMContentLoaded', function () {
        const noteId = `${note.noteId}`;
        console.log(noteId)
        const checklistContainer = document.getElementById('checklist-list-container');
        const newChecklistContentInput = document.getElementById('newChecklistContent');
        const addChecklistBtn = document.getElementById('addChecklistBtn');

        // 체크리스트 목록 로드 함수
        function loadChecklists() {
            fetch('<c:url value="/api/checklist/list/"/>' + noteId)
                .then(response => {
                    if (!response.ok) {
                        if (response.status === 401 || response.status === 403) {
                            return response.text().then(text => {
                                throw new Error(text || '인증/권한 오류');
                            });
                        }
                        return response.text().then(text => {
                            throw new Error(text || '체크리스트 로드 실패');
                        });
                    }
                    return response.json();
                })
                .then(checklists => {
                    console.log('서버에서 받은 체크리스트:', checklists);
                    checklistContainer.innerHTML = '';
                    if (checklists.length === 0) {
                        checklistContainer.innerHTML = '<li>아직 체크리스트 항목이 없습니다. 추가해보세요!</li>';
                    } else {
                        checklists.forEach(item => {
                            console.log('content before li:', item.content);
                            const li = document.createElement('li');
                            li.dataset.id = item.checkListId;
                            li.innerHTML = `
                                <input type="checkbox" class="checklist-checkbox" ${item.checked ? 'checked' : ''}>
                                <span class="checklist-content ${item.checked ? 'checked' : ''}" style="color: black; background: yellow;">테스트값</span>
                                <div class="checklist-actions">
                                    <a href="#" class="btn delete-checklist-btn">삭제</a>
                                </div>
                            `;
                            console.log('li.innerHTML:', li.innerHTML);
                                    let checked = `${item.checked}`;
                                    let content = `${item.content}`;
                                    console.log('checked' + checked);
                                    console.log('content' + content);
                                    console.log('li:' + li)
                            checklistContainer.appendChild(li);
                        });
                    }
                })
                .catch(error => {
                    console.error('Error loading checklists:', error);
                    alert('체크리스트 로드 중 오류가 발생했습니다: ' + error.message);
                    if (error.message.includes('로그인') || error.message.includes('권한')) {
                        window.location.href = '<c:url value="/login"/>';
                    }
                });
        }

        // 새 체크리스트 항목 추가 함수
        addChecklistBtn.addEventListener('click', function () {
            const content = newChecklistContentInput.value.trim();
            if (content === '') {
                alert('체크리스트 내용을 입력해주세요.');
                return;
            }

            fetch('<c:url value="/api/checklist"/>', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    noteId: noteId,
                    content: content,
                    isChecked: false
                })
            })
                .then(response => {
                    if (!response.ok) {
                        if (response.status === 401 || response.status === 403) {
                            return response.text().then(text => {
                                throw new Error(text || '인증/권한 오류');
                            });
                        }
                        return response.text().then(text => {
                            throw new Error(text || '체크리스트 추가 실패');
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    newChecklistContentInput.value = '';
                    loadChecklists();
                })
                .catch(error => {
                    console.error('Error adding checklist:', error);
                    alert('체크리스트 추가 중 오류가 발생했습니다: ' + error.message);
                    if (error.message.includes('로그인') || error.message.includes('권한')) {
                        window.location.href = '<c:url value="/login"/>';
                    }
                });
        });

        // 체크리스트 항목 이벤트 위임 (체크박스 토글, 삭제 버튼)
        checklistContainer.addEventListener('change', function (event) {
            if (event.target.classList.contains('checklist-checkbox')) {
                const li = event.target.closest('li');
                const checklistId = li.dataset.id;
                const isChecked = event.target.checked;

                fetch('<c:url value="/api/checklist/"/>' + checklistId, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({isChecked: isChecked})
                })
                    .then(response => {
                        if (!response.ok) {
                            if (response.status === 401 || response.status === 403) {
                                return response.text().then(text => {
                                    throw new Error(text || '인증/권한 오류');
                                });
                            }
                            return response.text().then(text => {
                                throw new Error(text || '체크 상태 업데이트 실패');
                            });
                        }
                        const contentSpan = li.querySelector('.checklist-content');
                        if (isChecked) {
                            contentSpan.classList.add('checked');
                        } else {
                            contentSpan.classList.remove('checked');
                        }
                    })
                    .catch(error => {
                        console.error('Error toggling checklist completion:', error);
                        alert('체크리스트 상태 업데이트 중 오류가 발생했습니다: ' + error.message);
                        if (error.message.includes('로그인') || error.message.includes('권한')) {
                            window.location.href = '<c:url value="/login"/>';
                        }
                    });
            }
        });

        checklistContainer.addEventListener('click', function (event) {
            // 삭제 버튼 클릭
            if (event.target.classList.contains('delete-checklist-btn')) {
                event.preventDefault();
                if (!confirm('정말 이 체크리스트 항목을 삭제하시겠습니까?')) {
                    return;
                }

                const li = event.target.closest('li');
                const checklistId = li.dataset.id;

                fetch('<c:url value="/api/checklist/"/>' + checklistId, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (!response.ok) {
                            if (response.status === 401 || response.status === 403) {
                                return response.text().then(text => {
                                    throw new Error(text || '인증/권한 오류');
                                });
                            }
                            return response.text().then(text => {
                                throw new Error(text || '체크리스트 삭제 실패');
                            });
                        }
                        return response.text();
                    })
                    .then(message => {
                        li.remove();
                        if (checklistContainer.children.length === 0 || (checklistContainer.children.length === 1 && checklistContainer.children[0].textContent.includes('아직 체크리스트 항목이 없습니다'))) {
                            checklistContainer.innerHTML = '<li>아직 체크리스트 항목이 없습니다. 추가해보세요!</li>';
                        }
                    })
                    .catch(error => {
                        console.error('Error deleting checklist:', error);
                        alert('체크리스트 삭제 중 오류가 발생했습니다: ' + error.message);
                        if (error.message.includes('로그인') || error.message.includes('권한')) {
                            window.location.href = '<c:url value="/login"/>';
                        }
                    });
            }
            // 내용 클릭 시 수정 가능하도록 (선택 사항 - 고급 기능)
            if (event.target.classList.contains('checklist-content')) {
                const span = event.target;
                const originalContent = span.textContent;
                const li = span.closest('li');
                const checklistId = li.dataset.id;

                const input = document.createElement('input');
                input.type = 'text';
                input.value = originalContent;
                input.style.width = 'calc(100% - 100px)';
                input.style.padding = '5px';
                input.style.border = '1px solid #ccc';
                input.style.borderRadius = '3px';
                span.replaceWith(input);
                input.focus();

                input.addEventListener('keypress', function (e) {
                    if (e.key === 'Enter') {
                        input.blur();
                    }
                });

                input.addEventListener('blur', function () {
                    const newContent = input.value.trim();
                    if (newContent !== originalContent && newContent !== '') {
                        fetch('<c:url value="/api/checklist/"/>' + checklistId, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({content: newContent})
                        })
                            .then(response => {
                                if (!response.ok) {
                                    if (response.status === 401 || response.status === 403) {
                                        return response.text().then(text => {
                                            throw new Error(text || '인증/권한 오류');
                                        });
                                    }
                                    return response.text().then(text => {
                                        throw new Error(text || '체크리스트 내용 업데이트 실패');
                                    });
                                }
                                return response.text();
                            })
                            .then(() => {
                                span.textContent = newContent;
                                input.replaceWith(span);
                            })
                            .catch(error => {
                                console.error('Error updating checklist content:', error);
                                alert('체크리스트 내용 업데이트 중 오류가 발생했습니다: ' + error.message);
                                span.textContent = originalContent;
                                input.replaceWith(span);
                                if (error.message.includes('로그인') || error.message.includes('권한')) {
                                    window.location.href = '<c:url value="/login"/>';
                                }
                            });
                    } else {
                        span.textContent = originalContent;
                        input.replaceWith(span);
                    }
                });
            }
        });


        // 페이지 로드 시 체크리스트 목록 로드
        loadChecklists();
    });
</script>
</body>
</html>