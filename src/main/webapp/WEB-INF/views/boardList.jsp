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
    <style>
        h2 a {
            text-decoration: none;
            color: black;
        }
        .text-start a {
            color: black;
            text-decoration: none;
        }
        .text-start a:hover {
            text-decoration: underline;
        }
        small {
            color: red;
        }

        #page{ display:flex; gap:.25rem; justify-content:center; margin:1rem 0; }
        #page a{ padding:.25rem .5rem; border:1px solid #dee2e6; border-radius:.25rem; text-decoration:none; color:#0d6efd; }
        #page a.active{ background:#0d6efd; color:#fff; }
        #page a.step{ font-weight:600; }
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
    <h2 class="mb-4">
        <a href="<c:url value="/board/list"/>">게시판 목록</a>
    </h2>

    <div class="d-flex justify-content-end mb-3">
        <!-- 검색 폼 -->
        <form class="d-flex" method="get" action="<c:url value='/board/list'/>">
            <select name="option" class="form-select form-select-sm me-2" style="width: 100px;">
                <option value="T" ${param.option eq 'T' ? 'selected' : ''}>제목</option>
                <option value="W" ${param.option eq 'W' ? 'selected' : ''}>작성자</option>
                <option value="A" ${param.option eq 'A' ? 'selected' : ''}>제목+내용</option>
            </select>
            <input type="text" name="keyword" class="form-control form-control-sm me-2"
                   placeholder="검색어" style="width: 200px;" required value="${not empty param.keyword ? param.keyword : ''}">
            <button type="submit" class="btn btn-secondary btn-sm me-2">검색</button>
        </form>

        <!-- 글쓰기 버튼 -->
        <a href="<c:url value='/board/write'/>" class="btn btn-primary btn-sm">글쓰기</a>
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
                    <a href="<c:url value='/board/view${ph.sc.queryString}&bno=${board.bno}'/>">${board.title}
                    <c:if test="${board.commentCnt ne 0}">
                        <small>(<c:out value="${board.commentCnt}"/>)</small>
                    </c:if>
                    </a>
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

    <div id="page">
        <c:if test="${ph.showPrev}">
            <a class="step" href="<c:url value="/board/list${ph.sc.getQueryString(ph.beginPage - 1)}"/> ">&lt;</a>
        </c:if>
        <c:forEach begin="${ph.beginPage}" end="${ph.endPage}" var="i">
            <a class="${i == ph.sc.page ? 'active' : ''}" href="<c:url value="/board/list${ph.sc.getQueryString(i)}"/> ">${i}</a>
        </c:forEach>
        <c:if test="${ph.showNext}">
            <a class="step" href="<c:url value="/board/list${ph.sc.getQueryString(ph.endPage + 1)}"/> ">&gt;</a>
        </c:if>
    </div>
</div>
</body>
</html>