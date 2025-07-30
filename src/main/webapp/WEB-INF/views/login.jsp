<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
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
<h2>로그인</h2>
<form action="<c:url value="/login" />" method="post" onsubmit="return formCheck(this)">
    <div id="msg">
        <c:if test="${not empty param.msg}">
            <i class="fa fa-exclamation-circle">
                ${URLDecoder.decode(param.msg)}
            </i>
        </c:if>
    </div>
    <label>이메일</label>
    <input type="email" name="email" value="${cookie.email.value}" required>
    <br>
    <label>비밀번호</label>
    <input type="password" name="pwd" required>
    <br>
    <input type="hidden" name="toURL" value="${param.toURL}">
    <button type="submit">로그인</button>
    <div>
        <label>
            <input type="checkbox" name="rememberId" ${empty cookie.email.value? "" : "checked"}>아이디 기억
        </label>
        <a href="<c:url value='/register/add'/>">회원가입</a>
    </div>
    <script>
        function formCheck(frm) {
            let msg = '';

            if(frm.email.value.length == 0) {
                setMessage('이메일을 입력해주세요.', frm.email);
                return false;
            }
            if(frm.pwd.value.length == 0) {
                setMessage('비밀번호를 입력해주세요.', frm.email);
                return false;
            }
            return true;
        }

        function setMessage(msg, element) {
            document.getElementById("msg").innerHTML = `${'${msg}'}`;

            if(element) {
                element.select();
            }
        }
    </script>
</form>
</body>
</html>