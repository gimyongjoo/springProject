<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
    <style>
        /* 기본 스타일 - 다른 JSP 파일들과 일관성 유지 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 400px; /* 로그인 폼에 적합한 너비로 조정 */
            width: 100%;
            margin: 80px auto; /* 중앙 정렬 */
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
            text-align: center;
        }

        /* 폼 스타일 */
        form {
            display: flex;
            flex-direction: column;
            gap: 15px; /* 요소들 간의 간격 */
        }

        label {
            font-weight: bold;
            color: #555;
            margin-bottom: 5px;
            display: block; /* 라벨이 한 줄 전체를 차지하도록 */
        }

        input[type="email"],
        input[type="password"],
        input[type="text"] {
            width: calc(100% - 20px); /* 패딩 고려 */
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
        }

        /* 버튼 스타일 */
        button[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            font-size: 1.1em;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
            width: 100%; /* 버튼 너비를 100%로 */
            margin-top: 10px;
        }

        button[type="submit"]:hover {
            background-color: #2980b9;
        }

        /* 하단 링크 및 체크박스 스타일 */
        .form-options {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 15px;
            font-size: 0.9em;
        }

        .form-options label {
            display: inline-flex; /* 체크박스와 텍스트를 인라인으로 */
            align-items: center;
            margin-bottom: 0; /* 기본 라벨 마진 제거 */
        }

        .form-options input[type="checkbox"] {
            margin-right: 5px;
        }

        .form-options a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s ease;
        }

        .form-options a:hover {
            color: #2980b9;
            text-decoration: underline;
        }

        /* 메시지 스타일 */
        .message-box {
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            font-size: 0.9em;
        }

        .error-message {
            background-color: #fdd;
            color: #d33;
            border: 1px solid #f99;
        }

        .success-message {
            background-color: #ddf;
            color: #33d;
            border: 1px solid #99f;
        }

    </style>
</head>
<body>
<%@ include file="navi.jsp"%>
<div class="container">
    <c:if test="${not empty errorMessage}">
        <script>
            alert("${errorMessage}");
        </script>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="message-box success-message">
            <script>
                alert("${successMessage}");
            </script>
        </div>
    </c:if>
    <h2>로그인</h2>
    <form action="<c:url value="/login" />" method="post" onsubmit="return formCheck(this)">
        <div id="msg" class="message-box error-message" style="display:none;">
            <c:if test="${not empty param.msg}">
                <i class="fa fa-exclamation-circle"></i>
                ${URLDecoder.decode(param.msg)}
            </c:if>
        </div>

        <label for="email">이메일</label>
        <input type="email" id="email" name="email" value="${cookie.email.value}" required>

        <label for="pwd">비밀번호</label>
        <input type="password" id="pwd" name="pwd" required>

        <input type="hidden" name="toURL" value="${param.toURL}">
        <button type="submit">로그인</button>
        <div class="form-options">
            <label>
                <input type="checkbox" name="rememberId" ${empty cookie.email.value? "" : "checked"}>아이디 기억
            </label>
            <a href="<c:url value='/register/add'/>">회원가입</a>
        </div>
    </form>
</div>

<script>
    // 메시지 표시 함수
    function setMessage(msg, element) {
        let msgBox = document.getElementById('msg');
        msgBox.innerText = msg;
        msgBox.style.display = 'block';
        if (element) {
            element.focus();
        }
    }

    function formCheck(frm) {
        let msgBox = document.getElementById('msg');
        msgBox.style.display = 'none'; // 이전 메시지 숨기기

        if(frm.email.value.length == 0) {
            setMessage('이메일을 입력해주세요.', frm.email);
            return false;
        }
        if(frm.pwd.value.length == 0) {
            setMessage('비밀번호를 입력해주세요.', frm.pwd); // pwd 필드에 포커스
            return false;
        }
        return true;
    }

    // 초기 로드 시 param.msg가 있으면 메시지 박스 표시
    window.onload = function() {
        if (document.querySelector('#msg i')) {
            document.getElementById('msg').style.display = 'block';
        }
    };
</script>

<%@ include file="footer.jsp"%>
</body>
</html>