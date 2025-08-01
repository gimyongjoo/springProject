<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav style="width: 100%; background: #222f3e; padding: 0; margin: 0;">
    <div style="display: flex; align-items: center; height: 60px;">
        <a href="<c:url value='/dashboard'/>" style="font-size: 1.7em; font-weight: bold; color: #fff; text-decoration: none; margin-left: 32px;">MyNotion</a>
        <div style="flex: 1;"></div>
        <c:choose>
            <c:when test="${not empty user}">
                <span style="color: #fff; margin-right: 16px;">${user.name}님</span>
                <a href="<c:url value='/logout'/>" style="color: #ff7675; background: #fff; border-radius: 5px; padding: 6px 16px; font-weight: bold; text-decoration: none; margin-right: 32px;">로그아웃</a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value='/login'/>" style="color: #fff; margin-right: 25px; font-weight: 500; text-decoration: none;">로그인</a>
                <a href="<c:url value='/register/add'/>" style="color: #fff; margin-right: 32px; font-weight: 500; text-decoration: none;">회원가입</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>