<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
    <h2 class="mb-4">게시판 목록</h2>

    <div class="mb-3 text-end">
        <a href="writeForm.jsp" class="btn btn-primary">글쓰기</a>
    </div>

    <table class="table table-bordered table-hover text-center align-middle">
        <thead class="table-light">
        <tr>
            <th style="width: 10%;">번호</th>
            <th style="width: 40%;">제목</th>
            <th style="width: 20%;">작성자</th>
            <th style="width: 20%;">작성일</th>
            <th style="width: 10%;">조회수</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="board" items="${list}">
            <tr>
                <td>${board.bno}</td>
                <td class="text-start">
                    <a href="<c:url value='/board/view?bno=${board.bno}'/>">${board.title}</a>
                </td>
                <td>${board.writer}</td>
                <td>
                    <c:if test="${not empty board.regDate}">
                        <fmt:parseDate value="${board.regDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="regDateObj"/>
                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" var="today"/>
                        <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd" var="regDate"/>
                        <c:choose>
                            <c:when test="${regDate eq today}">
                                <fmt:formatDate value="${regDateObj}" pattern="HH:mm"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <c:if test="${empty board.regDate}">
                        <span>-</span>
                    </c:if>
                </td>
                <td>${board.viewCnt}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr>
                <td colspan="5">등록된 게시글이 없습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <nav>
        <ul class="pagination justify-content-center">
            <li class="page-item <c:if test='${page==1}'>disabled</c:if>'">
                <a class="page-link" href="?page=${page-1}">이전</a>
            </li>
            <c:forEach begin="1" end="${totalPages}" var="p">
                <li class="page-item <c:if test='${p==page}'>active</c:if>'">
                    <a class="page-link" href="?page=${p}">${p}</a>
                </li>
            </c:forEach>
            <li class="page-item <c:if test='${page==totalPages}'>disabled</c:if>'">
                <a class="page-link" href="?page=${page+1}">다음</a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>