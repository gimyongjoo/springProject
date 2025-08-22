<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .page-title { border-bottom: 2px solid #e0e0e0; padding-bottom: 10px; margin-bottom: 20px; }
        .count { font-size: .875rem; color: #6c757d; }
    </style>
</head>
<body>
<%@ include file="navi.jsp" %>

<div class="container mt-5">
    <!-- 플래시 메시지 -->
    <c:if test="${not empty errorMessage}">
        <script>alert("${errorMessage}");</script>
    </c:if>
    <c:if test="${not empty successMessage}">
        <script>alert("${successMessage}");</script>
    </c:if>

    <h2 class="page-title">게시글 작성</h2>

    <!-- 작성자/날짜 등 안내 (선택) -->
    <div class="mb-3 text-muted">
        <span>작성자: <strong>
            <c:out value="${user.name != null ? user.name : (user.nickname != null ? user.nickname : user.email)}"/>
        </strong></span>
        <span class="ms-3">오늘:
            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd HH:mm"/>
        </span>
    </div>

    <!-- 글쓰기 폼 -->
    <form action="<c:url value='/board/write'/>" method="post" id="writeForm">
        <!-- Spring Security CSRF 사용 시 -->
        <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </c:if>

        <!-- 작성자 hidden으로도 같이 전송 -->
        <input type="hidden" name="writer"
               value="${user.name != null ? user.name : (user.nickname != null ? user.nickname : user.email)}"/>

        <div class="mb-3">
            <label for="title" class="form-label">제목</label>
            <input type="text" id="title" name="title" class="form-control" maxlength="100" required>
            <div class="count mt-1"><span id="titleCount">0</span>/100</div>
        </div>

        <div class="mb-3">
            <label for="content" class="form-label">내용</label>
            <textarea id="content" name="content" class="form-control" rows="12" required></textarea>
        </div>

        <div class="d-flex gap-2 justify-content-end">
            <a class="btn btn-secondary" href="<c:url value='/board/list'/>">취소</a>
            <button type="submit" class="btn btn-primary">작성 완료</button>
        </div>
    </form>
</div>

<script>
    // 제목 글자수 카운트
    (function () {
        const title = document.getElementById('title');
        const counter = document.getElementById('titleCount');
        const update = () => counter.textContent = (title.value || '').length;
        title.addEventListener('input', update);
        update();
    })();
</script>
</body>
</html>