<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .page-title { border-bottom: 2px solid #e0e0e0; padding-bottom: 10px; margin-bottom: 20px; }
        .form-control:disabled, .form-control[readonly] { background-color: #f8f9fa; }
        .btn-delete { background-color: #e74c3c; border-color: #e74c3c; }
        .btn-delete:hover { background-color: #c0392b; border-color: #c0392b; }
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

    <h2 class="page-title">게시글 수정</h2>

    <!-- 작성/수정 메타 -->
    <div class="mb-3 text-muted">
        <c:if test="${not empty board.regDate}">
            <%-- LocalDateTime을 출력용으로 포맷 --%>
            <fmt:parseDate value="${board.regDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="regDateObj"/>
            <span>작성일:
                <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd HH:mm"/>
            </span>
        </c:if>
        <c:if test="${not empty board.modiDate}">
            <fmt:parseDate value="${board.modiDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="modiDateObj"/>
            <span class="ms-3">수정일:
                <fmt:formatDate value="${modiDateObj}" pattern="yyyy-MM-dd HH:mm"/>
            </span>
        </c:if>
        <span class="ms-3">조회수: ${board.viewCnt}</span>
    </div>

    <!-- 수정 폼 -->
    <form action="<c:url value='/modify'/>" method="post" id="modifyForm">
        <!-- Spring Security CSRF (사용 중일 때) -->
        <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </c:if>

        <!-- 글 번호 (숨김) -->
        <input type="hidden" name="bno" value="${board.bno}"/>

        <div class="mb-3">
            <label for="title" class="form-label">제목</label>
            <input type="text" id="title" name="title" class="form-control"
                   value="${board.title}" maxlength="100" required>
            <div class="count mt-1"><span id="titleCount">0</span>/100</div>
        </div>

        <div class="mb-3">
            <label for="writer" class="form-label">작성자</label>
            <!-- 작성자는 보통 수정 불가로 둠 -->
            <input type="text" id="writer" name="writer" class="form-control" value="${board.writer}" readonly>
        </div>

        <div class="mb-3">
            <label for="content" class="form-label">내용</label>
            <textarea id="content" name="content" class="form-control" rows="12" required>${board.content}</textarea>
        </div>

        <div class="d-flex gap-2 justify-content-end">
            <a class="btn btn-secondary"
               href="<c:url value='/view?bno=${board.bno}'/>">취소</a>
            <button type="submit" class="btn btn-primary">수정 저장</button>
        </div>
    </form>

    <!-- 필요 시 삭제 버튼도 여기 배치 (선택) -->
    <!--
    <form action="<c:url value='/delete'/>" method="post" class="mt-2 text-end"
          onsubmit="return confirm('정말 삭제할까요?');">
        <input type="hidden" name="bno" value="${board.bno}">
        <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </c:if>
        <button type="submit" class="btn btn-delete text-white">삭제</button>
    </form>
    -->
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