<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- JSTL 코어 태그 라이브러리 추가 --%>
<!DOCTYPE html>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
<h2>회원가입</h2>

<c:if test="${not empty errorMessage}"> <%-- errorMessage가 비어있지 않으면 표시 --%>
    <p style="color: red;">${errorMessage}</p>
</c:if>

<form action="<c:url value='/register/save' />" method="post">
    <label>이름(닉네임)</label>
    <input type="text" name="name" required>
    <br>
    <label>이메일</label>
    <input type="email" name="email" required>
    <br>
    <label>비밀번호</label>
    <input type="password" name="pwd" required>
    <br>
    <button type="submit">가입하기</button>
    <a href="/login">로그인</a>
</form>
</body>
</html>