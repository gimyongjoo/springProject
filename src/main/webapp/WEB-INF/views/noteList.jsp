<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>노트 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
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

        .note-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        h2 {
            color: #2c3e50;
            margin: 0;
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
<div class="container mt-5">
    <div class="note-header">
        <h2>
            <c:choose>
                <c:when test="${currentFolderId != null}">
                    <c:forEach var="folder" items="${folders}">
                        <c:if test="${folder.folderId == currentFolderId}">
                            '${folder.name}' 폴더의 노트
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    전체 노트 목록
                </c:otherwise>
            </c:choose>
        </h2>
        <c:if test="${currentFolderId != null}">
            <div>
                <a href="<c:url value="/folder/edit?folderId=${currentFolderId}"/>" class="btn btn-sm btn-outline-secondary me-2">수정</a>
                <form action="<c:url value="/folder/delete?folderId=${currentFolderId}"/>" method="get" onsubmit="return confirm('정말로 이 폴더를 삭제하시겠습니까? 폴더 안의 모든 노트도 삭제됩니다.');" class="d-inline">
                    <input type="hidden" name="folderId" value="${currentFolderId}" />
                    <button type="submit" class="btn btn-sm btn-outline-danger">삭제</button>
                </form>
            </div>
        </c:if>
    </div>

    <a href="<c:url value="/note/add?folderId=${currentFolderId}"/>" class="btn btn-success mb-3">새 노트 추가</a>

    <table class="table table-hover">
        <thead>
        <tr>
            <th class="text-center">제목</th>
            <th class="text-center">날짜</th>
            <th class="text-center">고정</th>
            <th class="text-center">액션</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="note" items="${notes}">
            <tr>
                <td class="text-center"><a href="<c:url value="/note/view?noteId=${note.noteId}" />">${note.title}</a></td>
                <fmt:parseDate value="${note.createdDate}" pattern="yyyy-MM-dd'T'HH:mm" var="createdDateObj"/>
                <td class="text-center"><fmt:formatDate value="${createdDateObj}" pattern="yyyy년 MM월 dd일 HH:mm" /></td>
                <td class="text-center">
                    <c:if test="${note.isPinned}">
                        <span class="badge bg-warning text-dark">📌</span>
                    </c:if>
                </td>
                <td class="text-center">
                    <a href="<c:url value="/note/edit?noteId=${note.noteId}" />" class="btn btn-sm btn-outline-primary">수정</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty notes}">
            <tr><td colspan="4" class="text-center">노트가 없습니다.</td></tr>
        </c:if>
        </tbody>
    </table>

    <br>
    <a href="<c:url value="/dashboard" />" class="btn btn-secondary">대시보드로</a>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>