<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - 서버 오류</title>
    <style>
        body {
            font-family: 'Segoe UI', '맑은 고딕', Arial, sans-serif;
            background-color: #f8f9fa;
            color: #495057;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            text-align: center;
        }
        .error-container {
            max-width: 600px;
            padding: 40px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }
        .error-container h1 {
            font-size: 8rem;
            color: #dc3545; /* 빨간색 계열 */
            margin: 0;
        }
        .error-container h2 {
            font-size: 2rem;
            color: #212529;
            margin-top: -20px;
            margin-bottom: 20px;
        }
        .error-container p {
            font-size: 1.1rem;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        .error-container a {
            display: inline-block;
            background-color: #007bff;
            color: #fff;
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }
        .error-container a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1>500</h1>
    <h2>서버에 오류가 발생했습니다.</h2>
    <p>서비스 이용에 불편을 드려 죄송합니다.<br>문제가 지속되면 잠시 후 다시 시도해 주시기 바랍니다.</p>
    <a href="<c:url value="/" />">홈으로 돌아가기</a>
</div>
</body>
</html>