<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${board.title} - 게시글 보기</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="navi.jsp"%>

<c:if test="${not empty errorMessage}">
    <script>alert("${errorMessage}");</script>
</c:if>
<c:if test="${not empty successMessage}">
    <script>alert("${successMessage}");</script>
</c:if>

<div class="container mt-5">
    <h2 class="mb-4">게시글 보기</h2>


        <!-- 상단 메타 -->
        <div class="card mb-3">
            <div class="card-body">
                <h3 class="card-title mb-3">${dto.bno}</h3>
                <div class="d-flex flex-wrap gap-3 text-muted small">
                    <div>작성자: <strong>${dto.writer}</strong></div>

                    <!-- LocalDateTime 문자열을 Date로 파싱 후 포맷 -->
                    <c:if test="${not empty dto.regDate}">
                        <fmt:parseDate value="${dto.regDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="regDateObj"/>
                        <div>작성일:
                            <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd HH:mm"/>
                        </div>
                    </c:if>
                    <c:if test="${empty dto.regDate}">
                        <div>작성일: -</div>
                    </c:if>

                    <c:if test="${not empty dto.modiDate}">
                        <fmt:parseDate value="${dto.modiDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="modiDateObj"/>
                        <div>수정일:
                            <fmt:formatDate value="${modiDateObj}" pattern="yyyy-MM-dd HH:mm"/>
                        </div>
                    </c:if>

                    <div>조회수: ${dto.viewCnt}</div>
                    <div>댓글: ${dto.commentCnt}</div>
                </div>
            </div>
        </div>

    <!-- 내용 -->
    <div class="card mb-4">
        <div class="card-body" style="min-height:150px; white-space:pre-wrap; word-break:break-word;">
            <c:out value="${dto.content}" />
        </div>
    </div>

    <!-- 버튼들 -->
    <div class="d-flex gap-2 justify-content-end mb-5">
        <a class="btn btn-secondary" href="<c:url value='/board/list'/>">목록</a>
        <a class="btn btn-primary" href="<c:url value='/edit?bno=${dto.bno}'/>">수정</a>
        <form action="<c:url value='/delete'/>" method="post" onsubmit="return confirm('삭제할까요?');">
            <input type="hidden" name="bno" value="${dto.bno}">
            <button type="submit" class="btn btn-danger">삭제</button>
        </form>
    </div>

    <!-- 댓글 영역 (원하면 나중에 컨트롤러/서비스 연결) -->
    <div class="card">
        <div class="card-header">댓글</div>
        <div class="card-body">
            <c:if test="${empty comments}">
                <div class="text-muted">등록된 댓글이 없습니다.</div>
            </c:if>
            <c:forEach var="cmt" items="${comments}">
                <div class="border-bottom py-2">
                    <div class="d-flex justify-content-between">
                        <strong>${cmt.commenter}</strong>
                        <c:if test="${not empty cmt.regDate}">
                            <fmt:parseDate value="${cmt.regDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="cRegDateObj"/>
                            <span class="text-muted small">
                                <fmt:formatDate value="${cRegDateObj}" pattern="yyyy-MM-dd HH:mm"/>
                            </span>
                        </c:if>
                    </div>
                    <div class="mt-1" style="white-space:pre-wrap; word-break:break-word;">
                        <c:out value="${cmt.comment}" />
                    </div>
                </div>
            </c:forEach>

            <!-- 댓글 작성 폼(로그인 체크는 서버에서) -->
            <form class="mt-3" action="<c:url value='/comment/add'/>" method="post">
                <input type="hidden" name="bno" value="${board.bno}">
                <textarea class="form-control mb-2" name="comment" rows="3" placeholder="댓글을 입력하세요" required></textarea>
                <div class="text-end">
                    <button type="submit" class="btn btn-primary">댓글 등록</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
