<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🗒️</text></svg>">
    <title>비밀번호 찾기</title>
    <style>
        body{font-family:'Segoe UI','맑은 고딕',Arial,sans-serif;margin:0;padding:0;background:#f4f7f6;color:#222;}
        .container{max-width:400px;width:100%;margin:80px auto;background:#fff;border-radius:14px;
            box-shadow:0 6px 24px rgba(52,73,94,.09);padding:36px 34px 30px 34px;}
        h2{text-align:center;color:#2d3436;margin-bottom:36px;font-size:2em;font-weight:700;letter-spacing:1px;}
        form{display:flex;flex-direction:column;gap:16px;}
        label{font-weight:600;color:#34495e;margin-bottom:6px;display:block;}
        input[type="text"],input[type="email"],input[type="password"]{
            width: calc(100% - 20px);padding:12px 13px;font-size:1em;border:1.5px solid #dde1e6;border-radius:6px;
            background:#f9fafc;outline:none;transition:border .2s,background .2s;}
        input:focus{border-color:#339af0;background:#fff;}
        .btn{width:100%;padding:13px 0;font-size:1.1em;font-weight:700;background:#339af0;color:#fff;
            border:none;border-radius:7px;cursor:pointer;transition:background .18s;}
        .btn:hover{background:#2162af;}
        .link-row{margin-top:14px;display:flex;justify-content:flex-end;gap:12px;font-size:.95em;}
        .link-row a{color:#339af0;text-decoration:none;font-weight:500;}
        .link-row a:hover{color:#2162af;text-decoration:underline;}
        .message{margin-bottom:10px;padding:12px;border-radius:6px;font-size:.95em;text-align:center;}
        .message.error{background:#ffeaea;color:#d0342c;border:1px solid #fabcbc;}
        .message.success{background:#e9fbe5;color:#27863c;border:1px solid #b7e4c7;}
        #pwdMsg{font-size:.9em;margin-top:-6px;}
    </style>
</head>
<body>
<%@ include file="navi.jsp"%>

<div class="container">
    <c:if test="${not empty errorMessage}">
        <script>alert("${errorMessage}");</script>
    </c:if>
    <c:if test="${not empty successMessage}">
        <script>alert("${successMessage}");</script>
    </c:if>

    <h2>비밀번호 찾기</h2>

    <!-- 2단계: 토큰이 있으면 새 비밀번호 설정 -->
    <c:if test="${not empty resetToken}">
        <div class="message success">본인 확인이 완료되었습니다. 새 비밀번호를 설정하세요.</div>

        <form method="post" action="<c:url value='/register/pwdReset'/>" onsubmit="return validateNewPwd()">
            <input type="hidden" name="token" value="${resetToken}">
            <c:if test="${not empty email}">
                <input type="hidden" name="email" value="${email}">
            </c:if>

            <div>
                <label for="pwd">새 비밀번호</label>
                <input type="password" id="pwd" name="pwd" minlength="6" maxlength="20" required placeholder="6~20자">
            </div>
            <div>
                <label for="pwdConfirm">비밀번호 확인</label>
                <input type="password" id="pwdConfirm" name="pwdConfirm" minlength="6" maxlength="20" required placeholder="다시 입력">
                <small id="pwdMsg"></small>
            </div>
            <button type="submit" class="btn">비밀번호 변경</button>
        </form>

        <div class="link-row">
            <a href="<c:url value='/login'/>">로그인</a>
            <a href="<c:url value='/register/add'/>">회원가입</a>
        </div>
    </c:if>

    <!-- 1단계: 본인 확인 폼 (토큰 없을 때) -->
    <c:if test="${empty resetToken}">
        <form method="post" action="<c:url value='/register/pwdFind'/>" onsubmit="return validateIdentity()">
            <div>
                <label for="email">가입 이메일</label>
                <input type="email" id="email" name="email" maxlength="60" required placeholder="example@domain.com">
            </div>
            <div>
                <label for="name">이름</label>
                <input type="text" id="name" name="name" maxlength="16" required placeholder="이름(한글/영문)">
            </div>
            <button type="submit" class="btn">본인 확인</button>
        </form>

        <div class="link-row">
            <a href="<c:url value='/login'/>">로그인</a>
            <a href="<c:url value='/register/add'/>">회원가입</a>
        </div>
    </c:if>
</div>

<script>
    // 1단계: 이메일 + 이름 검증
    function validateIdentity(){
        const email = document.getElementById('email').value.trim();
        const name  = document.getElementById('name').value.trim();
        const emailRe = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if(!emailRe.test(email)){ alert('이메일 형식이 올바르지 않습니다.'); return false; }
        if(!/^[A-Za-z가-힣]+$/.test(name)){ alert('이름은 한글/영문만 입력해주세요.'); return false; }
        return true;
    }

    // 2단계: 비밀번호 일치/길이 체크
    function validateNewPwd(){
        const p = document.getElementById('pwd').value;
        const c = document.getElementById('pwdConfirm').value;
        if(p.length < 6 || p.length > 20){ alert('비밀번호는 6~20자로 입력해주세요.'); return false; }
        if(p !== c){ alert('비밀번호가 일치하지 않습니다.'); return false; }
        return true;
    }

    // 입력 중 실시간 메시지
    (function(){
        const p = document.getElementById('pwd');
        const c = document.getElementById('pwdConfirm');
        const msg = document.getElementById('pwdMsg');
        if(p && c && msg){
            function check(){
                if(!p.value && !c.value){ msg.textContent=''; return; }
                if(p.value === c.value){ msg.textContent='비밀번호가 일치합니다.'; msg.style.color='green'; }
                else { msg.textContent='비밀번호가 일치하지 않습니다.'; msg.style.color='red'; }
            }
            p.addEventListener('input', check);
            c.addEventListener('input', check);
        }
    })();
</script>

<%@ include file="footer.jsp"%>
</body>
</html>