<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>노트 목록</title>
    <style>
        /* 기본 스타일 - dashboard.jsp, noteForm.jsp, noteView.jsp와 동일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 960px; /* 대시보드와 유사하게 넓게 설정 */
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

        /* 메시지 스타일 */
        .message {
            padding: 10px 15px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-weight: bold;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        /* 환영 메시지 */
        .welcome-message {
            margin-bottom: 20px;
            font-size: 1.1em;
            color: #555;
        }
        .welcome-message b {
            color: #2c3e50;
        }

        /* 현재 폴더 정보 */
        .current-folder-info {
            margin-bottom: 20px;
            font-style: italic;
            color: #777;
        }

        /* 버튼/링크 스타일 - dashboard.jsp와 동일 */
        .btn {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
            margin-bottom: 20px;
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

        /* 테이블 스타일 */
        table {
            width: 100%;
            border-collapse: collapse; /* 셀 경계선 제거 */
            margin-bottom: 20px;
            background-color: #fff; /* 테이블 배경색 */
        }

        th, td {
            border: 1px solid #ddd; /* 모든 셀에 경계선 */
            padding: 12px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
            color: #555;
            font-weight: bold;
            text-transform: uppercase; /* 대문자로 변경 */
            font-size: 0.9em;
        }

        tbody tr:nth-child(even) { /* 짝수 행 배경색 */
            background-color: #f9f9f9;
        }

        tbody tr:hover { /* 마우스 오버 시 행 강조 */
            background-color: #f0f0f0;
        }

        td a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s ease;
        }

        td a:hover {
            color: #2980b9;
            text-decoration: underline;
        }

        /* 핀 아이콘 */
        td span {
            font-size: 1.1em;
            color: #f39c12;
        }

        /* 노트가 없을 때 */
        tbody tr td[colspan="5"] {
            text-align: center;
            padding: 20px;
            color: #777;
            font-style: italic;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>노트 목록</h2>

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

    <c:if test="${not empty user}">
        <div class="welcome-message"><b>${user.name}</b>님, 환영합니다!</div>
    </c:if>

    <c:if test="${not empty currentFolder}"> <%-- currentFolder 객체가 Model에 추가되었을 경우 --%>
        <div class="current-folder-info">현재 폴더: ${currentFolder.name} (ID: ${currentFolder.folderId})</div>
    </c:if>

    <%-- folderId가 존재하면 /note/add 링크에 folderId를 파라미터로 전달 --%>
    <a href="<c:url value="/note/add">
                <c:if test="${not empty folderId}">
                    <c:param name="folderId" value="${folderId}"/>
                </c:if>
             </c:url>" class="btn">+ 새 노트 만들기</a>
    <hr/>

    <table border="1" width="100%">
        <thead>
        <tr>
            <th>제목</th>
            <th>생성일</th>
            <th>핀고정</th>
            <th>수정</th>
            <th>보기</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="note" items="${notes}">
            <tr>
                <td>${note.title}</td>
                <fmt:parseDate value="${note.createdDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createdDateObj" />
                <fmt:formatDate value="${createdDateObj}" pattern="yyyy-MM-dd" var="regDate"/>
                <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" var="today"/>
                <c:choose>
                    <c:when test="${regDate eq today}">
                        <td><fmt:formatDate value="${createdDateObj}" pattern="HH:mm" /></td>
                    </c:when>
                    <c:otherwise>
                        <td>${regDate}</td>
                    </c:otherwise>
                </c:choose>
                <td>
                    <c:choose>
                        <c:when test="${note.isPinned}">
                            📌
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="<c:url value="/note/edit?noteId=${note.noteId}" />">수정</a>
                </td>
                <td>
                    <a href="<c:url value="/note/view?noteId=${note.noteId}" />">보기</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty notes}">
            <tr><td colspan="5" style="text-align:center;">노트가 없습니다.</td></tr>
        </c:if>
        </tbody>
    </table>

    <br>
    <a href="<c:url value="/dashboard" />">대시보드로</a>
</div>
</body>
</html>