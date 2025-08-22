<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🗒️</text></svg>">
    <title>회원가입</title>
    <style>
        body {
            background: #f4f7f6;
            font-family: 'Segoe UI', '맑은 고딕', Arial, sans-serif;
            margin: 0;
            padding: 0;
            color: #222;
        }
        .register-container {
            max-width: 400px;
            margin: 80px auto;
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 6px 24px rgba(52, 73, 94, 0.09);
            padding: 36px 34px 30px 34px;
            display: flex;
            flex-direction: column;
        }
        h2 {
            text-align: center;
            color: #2d3436;
            margin-bottom: 36px;
            font-size: 2em;
            font-weight: 700;
            letter-spacing: 1px;
        }
        .form-group {
            margin-bottom: 22px;
        }
        label {
            display: block;
            font-size: 1em;
            font-weight: 600;
            margin-bottom: 7px;
            color: #34495e;
        }

        .check-btn {
            padding: 0 16px;
            background: #b2bec3;
            color: #222;
            border: none;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            font-size: 0.98em;
            transition: background 0.18s;
            margin-left: 2px;
        }
        .check-btn:hover {
            background: #636e72;
            color: #fff;
        }

        input[type="text"], input[type="password"], input[type="email"] {
            width: 100%;
            padding: 12px 13px;
            font-size: 1em;
            border: 1.5px solid #dde1e6;
            border-radius: 6px;
            box-sizing: border-box;
            outline: none;
            transition: border 0.2s;
            background: #f9fafc;
        }
        input[type="text"]:focus, input[type="password"]:focus, input[type="email"]:focus {
            border: 1.5px solid #339af0;
            background: #fff;
        }
        .input-tip {
            font-size: 0.93em;
            color: #7d8590;
            margin-top: 2px;
        }
        .message {
            margin-bottom: 16px;
            padding: 12px;
            border-radius: 6px;
            font-size: 1em;
            text-align: center;
        }
        .message.error {
            background: #ffeaea;
            color: #d0342c;
            border: 1px solid #fabcbc;
        }
        .message.success {
            background: #e9fbe5;
            color: #27863c;
            border: 1px solid #b7e4c7;
        }
        .register-btn {
            width: 100%;
            padding: 13px 0;
            font-size: 1.1em;
            font-weight: bold;
            background: #339af0;
            color: #fff;
            border: none;
            border-radius: 7px;
            cursor: pointer;
            margin-top: 8px;
            transition: background 0.18s;
        }
        .register-btn:hover {
            background: #2162af;
        }
        .link-box {
            margin-top: 22px;
            text-align: center;
        }
        .link-box a {
            color: #339af0;
            text-decoration: none;
            font-size: 0.99em;
            transition: color 0.18s;
            font-weight: 500;
        }
        .link-box a:hover {
            color: #2162af;
            text-decoration: underline;
        }
        @media (max-width: 500px) {
            .register-container {
                max-width: 96vw;
                padding: 18px 6vw 16px 6vw;
            }
            h2 { font-size: 1.4em; }
        }

        #msg {
            margin-top: 10px;
            margin-left: 10px;
        }
    </style>
</head>
<body>
<%@ include file="navi.jsp"%>
<div class="register-container">

    <h2>회원가입</h2>

    <c:if test="${not empty errorMessage}">
        <div class="message error">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="message success">${successMessage}</div>
    </c:if>

    <form id="registerForm" method="post" action="<c:url value='/register/save'/>">
        <div class="form-group" style="position:relative;">
            <label for="email">이메일</label>
            <div style="display:flex; gap:8px;">
                <input type="email" id="email" name="email" required autocomplete="off" placeholder="이메일 입력" maxlength="40" style="flex:1;"/>
                <button type="button" class="check-btn" id="checkEmailBtn" onclick="idcheck()">중복확인</button>
            </div>
            <div id="msg"></div>
        </div>
        <div class="form-group">
            <label for="pwd">비밀번호</label>
            <input type="password" id="pwd" name="pwd" required placeholder="비밀번호 입력" minlength="6" maxlength="20" />
        </div>
        <div class="form-group">
            <label for="name">닉네임</label>
            <input type="text" id="name" name="name" required placeholder="닉네임 입력" maxlength="16" />
        </div>
        <button type="submit" class="register-btn">가입하기</button>
    </form>
    <div class="link-box">
        이미 계정이 있으신가요? <a href="<c:url value='/login'/>">로그인</a>
    </div>
</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
    // 이 스크립트 블록은 원본에서 변경하지 않았습니다.
    function idcheck(){
        let email = document.querySelector("#email")
        console.log(email.value)
        $.ajax({
            type: 'GET',
            url: `<c:url value="/register/idcheck/${'${email.value}'}"/>`, // 요청 URI
            header: {"content-type" : "application/json"}, // 보내는 데이터 타입 명시
            success: function(result) { // 요청이 성공일 때 실행되는 이벤트
                if(result > 0) {
                    email.value = "";
                    document.querySelector("#msg").innerText = "이메일이 중복되었습니다.";
                    document.querySelector("#msg").style.color = "red";
                    email.focus();
                } else {
                    document.querySelector("#msg").innerText = "사용 가능한 이메일입니다.";
                    document.querySelector("#msg").style.color = "blue";
                    email.readOnly = true;
                }
            },
            error: function(request, status, error) {
                console.log("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
            }
        })
    }
</script>
<%@ include file="footer.jsp"%>
</body>
</html>