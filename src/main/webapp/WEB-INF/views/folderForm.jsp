<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>폴더 추가/수정</title>
    <style>
        /* 기본 스타일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6; /* 기존 배경색 */
            color: #333;
            line-height: 1.6;

            /* 모달처럼 보이게 하는 핵심 CSS */
            display: flex;
            justify-content: center; /* 가로 중앙 정렬 */
            align-items: center; /* 세로 중앙 정렬 */
            min-height: 100vh; /* 뷰포트 전체 높이 사용 */
            /* 배경을 어둡게 하여 모달 효과를 줍니다. */
            background-color: rgba(0, 0, 0, 0.5); /* 반투명 검정 배경 */
        }

        .container {
            max-width: 450px; /* 모달에 적합한 너비로 조정 */
            width: 90%; /* 작은 화면에서도 잘 보이도록 반응형 추가 */
            margin: 0; /* body의 flexbox가 중앙 정렬을 하므로 margin을 0으로 설정 */
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2); /* 그림자 더 강하게 */
            position: relative; /* z-index를 위해 */
            z-index: 100; /* 배경보다 위에 오도록 */
            animation: fadeIn 0.3s ease-out; /* 부드러운 등장 애니메이션 */
        }

        /* 모달 등장 애니메이션 */
        @keyframes fadeIn {
            from { opacity: 0; transform: scale(0.95); }
            to { opacity: 1; transform: scale(1); }
        }

        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
            text-align: center; /* 제목 중앙 정렬 */
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-group input[type="text"] {
            width: calc(100% - 22px);
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
            box-sizing: border-box;
        }

        /* 버튼 그룹 스타일 */
        .button-group {
            display: flex;
            gap: 10px; /* 버튼 사이 간격 */
            margin-top: 20px;
            justify-content: space-between; /* 버튼들을 양 끝으로 정렬 */
            flex-wrap: wrap; /* 작은 화면에서 줄바꿈 */
        }

        .button-group .btn,
        .button-group .btn-delete,
        .button-group .btn-cancel { /* 모든 버튼에 공통 스타일 적용 */
            flex-grow: 1; /* 버튼들이 공간을 균등하게 차지하도록 */
            text-align: center;
            padding: 12px 15px; /* 패딩 좀 더 넉넉하게 */
            font-size: 1.0em; /* 글씨 크기 조정 */
            white-space: nowrap; /* 줄바꿈 방지 */
            border-radius: 5px; /* 버튼 모서리 둥글게 */
            text-decoration: none; /* 링크 버튼의 밑줄 제거 */
            font-weight: bold;
            transition: background-color 0.3s ease;
            cursor: pointer;
            box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
        }

        .btn {
            background-color: #3498db;
            color: white;
            border: none;
        }
        .btn:hover {
            background-color: #2980b9;
        }

        .btn-delete {
            background-color: #e74c3c; /* 삭제 버튼 색상 */
            color: white;
            border: none;
        }
        .btn-delete:hover {
            background-color: #c0392b;
        }

        .btn-cancel {
            background-color: #6c757d; /* 취소 버튼 색상 */
            color: white;
            border: none;
        }
        .btn-cancel:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
<div class="container">
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

    <h2>
        <c:choose>
            <c:when test="${mode eq 'add'}">새 폴더 추가</c:when>
            <c:when test="${mode eq 'edit'}">폴더 수정</c:when>
            <c:otherwise>폴더</c:otherwise>
        </c:choose>
    </h2>
    <form action="<c:url value="/folder/${mode}"/>" method="post">
        <c:if test="${mode eq 'edit'}">
            <input type="hidden" name="folderId" value="${folder.folderId}" />
        </c:if>
        <input type="hidden" name="mode" value="${mode}" />

        <div class="form-group">
            <label for="name">폴더 이름</label>
            <input type="text" id="name" name="name" value="${folder.name}" required>
        </div>

        <div class="button-group">
            <button type="submit" class="btn">
                <c:choose>
                    <c:when test="${mode eq 'add'}">폴더 추가</c:when>
                    <c:otherwise>폴더 수정</c:otherwise>
                </c:choose>
            </button>
            <c:if test="${mode eq 'edit'}">
                <a href="<c:url value="/folder/delete?folderId=${folder.folderId}"/>" class="btn btn-delete"
                   onclick="return confirm('이 폴더와 이 폴더 안의 모든 노트가 영구적으로 삭제됩니다. 정말 삭제하시겠습니까?');">폴더 삭제</a>
            </c:if>
            <a href="<c:url value="/dashboard"/>" class="btn btn-cancel">취소</a>
        </div>
    </form>
</div>
</body>
</html>