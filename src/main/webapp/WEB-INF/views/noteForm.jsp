<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>노트 작성/수정</title>
    <style>
        /* 기본 스타일 - dashboard.jsp와 동일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 800px; /* 폼에 맞게 너비 조정 */
            margin: 40px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        /* 폼 요소 스타일 */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-group input[type="text"],
        .form-group textarea {
            width: calc(100% - 22px); /* padding 고려 */
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
        }

        .form-group textarea {
            min-height: 250px; /* 높이 설정 */
            resize: vertical; /* 세로 크기 조절 가능 */
        }

        .form-group input[type="checkbox"] {
            margin-right: 8px;
        }

        /* 버튼 스타일 - dashboard.jsp와 동일 */
        .btn {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
            margin-right: 10px; /* 버튼 간 간격 */
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #2980b9;
        }

        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
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
<div class="container">
    <h2>
        <c:if test="${mode eq 'add'}">새 노트 작성</c:if>
        <c:if test="${mode eq 'edit'}">노트 수정</c:if>
    </h2>
    <form action="<c:url value="/note/${mode}"/>" method="post">
        <input type="hidden" name="noteId" value="${note.noteId}" />
        <input type="hidden" name="folderId" value="${note.folderId}" />
        <input type="hidden" name="mode" value="${mode}" />

        <div class="form-group">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" value="${note.title}" required>
        </div>

        <div class="form-group">
            <label for="content">내용</label>
            <textarea id="content" name="content">${note.content}</textarea>
        </div>

        <div class="form-group">
            <input type="checkbox" id="isPinned" name="isPinned" value="true"
                   <c:if test="${note.isPinned eq true}">checked</c:if> />
            <label for="isPinned" style="display: inline;">핀 고정</label>
        </div>

        <button type="submit" class="btn">
            <c:choose>
                <c:when test="${mode == 'add'}">저장</c:when>
                <c:otherwise>수정</c:otherwise>
            </c:choose>
        </button>
        <a href="<c:url value="/dashboard"/>" class="btn btn-secondary">취소</a>
    </form>
</div>
</body>
</html>