<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>마이노션 - 대시보드</title>
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

        h2, h3 {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        /* 버튼 스타일 */
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


        /* 리스트 스타일 */
        ul {
            list-style: none;
            padding: 0;
            margin-bottom: 30px;
        }

        li {
            background-color: #f9f9f9;
            border: 1px solid #eee;
            margin-bottom: 10px;
            padding: 12px 15px;
            border-radius: 5px;
            display: flex; /* Flexbox를 사용하여 내용 정렬 */
            align-items: center; /* 세로 중앙 정렬 */
            justify-content: space-between; /* 양 끝 정렬 */
        }

        li a {
            text-decoration: none;
            color: #34495e;
            font-weight: bold;
            flex-grow: 1; /* 링크가 가능한 공간을 모두 차지하도록 */
        }

        li a:hover {
            color: #3498db;
        }

        /* 핀 고정 아이콘 */
        li span {
            margin-left: 10px;
            font-size: 1.2em;
            color: #f39c12; /* 핀 아이콘 색상 */
        }

        hr {
            border: 0;
            height: 1px;
            background: #e0e0e0;
            margin: 30px 0;
        }

        .logout-link {
            display: block;
            text-align: right;
            margin-top: 30px;
        }
        .logout-link a {
            color: #e74c3c;
            text-decoration: none;
            font-weight: bold;
        }
        .logout-link a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>
<%@ include file="navi.jsp"%>

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
    <h2>내 노트 대시보드</h2>

    <h3>폴더</h3>
        <a href="<c:url value="/folder/add"/>" class="btn">새 폴더 추가</a>
    <ul>
        <c:forEach var="folder" items="${folders}">
            <li>
                <a href="<c:url value="/note/list?folderId=${folder.folderId}"/>">${folder.name}</a>
            </li>
        </c:forEach>
        <c:if test="${empty folders}">
            <li>아직 폴더가 없습니다. 새 폴더를 추가해보세요!</li>
        </c:if>
    </ul>


    <hr>

    <h3>전체 노트 목록</h3>
    <p style="color:#888; font-size:0.95em; margin-bottom:20px;">
        폴더에서 노트를 추가할 수 있습니다.
    </p>
    <ul>
        <c:forEach var="note" items="${notes}">
            <li>
                <a href="<c:url value="/note/view?noteId=${note.noteId}"/>">${note.title}</a>
                <c:if test="${note.isPinned}"><span>📌</span></c:if>
            </li>
        </c:forEach>
        <c:if test="${empty notes}">
            <li>아직 노트가 없습니다. 새 노트를 추가해보세요!</li>
        </c:if>
    </ul>

    <div class="logout-link">
        <a href="<c:url value="/logout"/>">로그아웃</a>
    </div>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>