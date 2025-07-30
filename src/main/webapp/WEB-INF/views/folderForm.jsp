<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>폴더 관리</title>
    <style>
        /* 기본 스타일 - 다른 JSP 파일들과 일관성 유지 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 600px; /* 폼에 적합한 너비로 조정 */
            margin: 40px auto;
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

        input[type="text"] {
            width: calc(100% - 20px); /* 패딩 고려 */
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
            box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
        }

        input[type="text"]:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.5);
        }

        /* 버튼 스타일 */
        .btn {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
            border: none; /* input submit button 스타일 초기화 */
            cursor: pointer;
            text-align: center;
            width: fit-content; /* 내용에 따라 너비 조정 */
        }
        .btn:hover {
            background-color: #2980b9;
        }

        .btn-cancel {
            background-color: #6c757d;
            margin-left: 10px; /* 버튼 사이 간격 */
        }
        .btn-cancel:hover {
            background-color: #5a6268;
        }

        .button-group {
            display: flex; /* 버튼들을 한 줄에 배치 */
            justify-content: flex-start; /* 왼쪽 정렬 */
            margin-top: 20px; /* 폼 요소 위 간격 */
        }
    </style>
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

<div class="container">
    <h2>
        <c:choose>
            <c:when test="${folder.folderId != null}">
                폴더 수정
            </c:when>
            <c:otherwise>
                새 폴더 추가
            </c:otherwise>
        </c:choose>
    </h2>

    <form method="post"
          action="<c:choose>
                          <c:when test='${folder.folderId != null}'><c:url value="/folder/edit"/></c:when>
                          <c:otherwise><c:url value="/folder/add"/></c:otherwise>
                      </c:choose>">
        <c:if test="${folder.folderId != null}">
            <input type="hidden" name="folderId" value="${folder.folderId}" />
        </c:if>

        <label for="name">폴더명</label>
        <input type="text" id="name" name="name" value="${folder.name}" required />

        <div class="button-group">
            <button type="submit" class="btn">
                <c:choose>
                    <c:when test="${folder.folderId != null}">수정</c:when>
                    <c:otherwise>등록</c:otherwise>
                </c:choose>
            </button>
            <a href="<c:url value="/dashboard"/>" class="btn btn-cancel">취소</a>
        </div>
    </form>
</div>
</body>
</html>