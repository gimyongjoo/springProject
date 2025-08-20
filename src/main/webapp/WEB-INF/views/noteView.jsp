<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🗒️</text></svg>">
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

</div>

<%@ include file="footer.jsp" %>

<script>

</script>
</body>
</html>