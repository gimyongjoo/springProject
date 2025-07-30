<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- fmt 라이브러리 추가 --%>

<!DOCTYPE html>
<html>
<head>
    <title>${note.title}</title>
    <style>
        /* 기본 스타일 - dashboard.jsp와 동일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 800px; /* 노트 내용에 맞게 너비 조정 */
            margin: 40px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        h2, h3 {
            color: #2c3e50;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        /* 노트 내용 스타일 */
        .note-header {
            display: flex;
            align-items: center;
            margin-bottom: 5px; /* 날짜 정보 추가로 간격 줄임 */
        }
        .note-header h2 {
            margin: 0;
            border-bottom: none;
            padding-bottom: 0;
        }
        .note-header .pinned-icon {
            margin-left: 15px;
            font-size: 1.5em;
            color: #f39c12;
        }

        .note-metadata { /* 새로운 메타데이터 스타일 추가 */
            font-size: 0.9em;
            color: #777;
            margin-bottom: 20px;
            border-bottom: 1px dashed #e0e0e0;
            padding-bottom: 10px;
        }
        .note-metadata p {
            margin: 5px 0;
        }

        .note-content {
            background-color: #fdfdfd;
            border: 1px solid #eee;
            padding: 20px;
            border-radius: 8px;
            min-height: 150px;
            margin-bottom: 30px;
            white-space: pre-wrap; /* 마크다운 텍스트 줄바꿈 유지 */
            word-wrap: break-word; /* 긴 단어 줄바꿈 */
        }

        /* 섹션 스타일 (체크리스트, 이미지, 할 일) */
        .section-header {
            color: #34495e;
            margin-top: 30px;
            margin-bottom: 15px;
            font-size: 1.1em;
            font-weight: bold;
        }

        .item-list ul {
            list-style: none;
            padding: 0;
            margin-bottom: 20px;
        }

        .item-list li {
            background-color: #f9f9f9;
            border: 1px solid #eee;
            margin-bottom: 8px;
            padding: 10px 15px;
            border-radius: 5px;
            display: flex;
            align-items: center;
        }

        .item-list li input[type="checkbox"] {
            margin-right: 10px;
            transform: scale(1.2); /* 체크박스 크기 조절 */
        }

        .item-list li.completed label {
            text-decoration: line-through;
            color: #999;
        }

        .item-list img {
            max-width: 100%; /* 이미지 크기 조절 */
            height: auto;
            display: block; /* 이미지 하단 여백 제거 */
            margin-top: 10px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        /* 버튼 스타일 - dashboard.jsp와 동일 */
        .btn-group {
            margin-top: 30px;
            text-align: right;
        }
        .btn {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
            margin-left: 10px; /* 버튼 간 간격 */
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #2980b9;
        }

        .btn-danger {
            background-color: #e74c3c;
        }
        .btn-danger:hover {
            background-color: #c0392b;
        }

        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="note-header">
        <h2>${note.title}</h2>
        <c:if test="${note.isPinned}"><span class="pinned-icon">📌</span></c:if>
    </div>

    <%-- 날짜 및 사용자 정보 추가 --%>
    <div class="note-metadata">
        <%-- note.createdDate와 note.updatedDate가 String이라고 가정하고 parseDate 사용 --%>
        <fmt:parseDate value="${note.createdDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createdDateObj" />
        <fmt:parseDate value="${note.updatedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updatedDateObj" />

        <p>작성자: <b>${user.name}</b></p>
        <p>
            작성일:
            <c:choose>
                <c:when test="${not empty createdDateObj}">
                    <fmt:formatDate value="${createdDateObj}" pattern="yyyy년 MM월 dd일 HH시 mm분" />
                </c:when>
                <c:otherwise>
                    정보 없음
                </c:otherwise>
            </c:choose>
        </p>
        <p>
            최근 수정일:
            <c:choose>
                <c:when test="${not empty updatedDateObj}">
                    <fmt:formatDate value="${updatedDateObj}" pattern="yyyy년 MM월 dd일 HH시 mm분" />
                </c:when>
                <c:otherwise>
                    정보 없음
                </c:otherwise>
            </c:choose>
        </p>
    </div>

    <div class="note-content">
        <div>${note.content}</div>
    </div>

    <div class="item-list">
        <c:if test="${not empty checkLists}">
            <div class="section-header">체크리스트</div>
            <ul>
                <c:forEach var="checkList" items="${checkLists}">
                    <li class="${checkList.isCompleted ? 'completed' : ''}">
                        <input type="checkbox" ${checkList.isCompleted ? 'checked' : ''} disabled>
                        <label>${checkList.content}</label>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${not empty images}">
            <div class="section-header">이미지</div>
            <ul>
                <c:forEach var="image" items="${images}">
                    <li>
                        <img src="<c:url value="/upload/${image.path}"/>" alt="첨부 이미지" />
                            <%-- 이미지 경로가 "/upload/" 아래에 있다고 가정 --%>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${not empty todos}">
            <div class="section-header">할 일 목록</div>
            <ul>
                <c:forEach var="todo" items="${todos}">
                    <li class="${todo.isCompleted ? 'completed' : ''}">
                        <input type="checkbox" ${todo.isCompleted ? 'checked' : ''} disabled>
                        <label>${todo.content}</label>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <div class="btn-group">
        <a href="<c:url value="/note/edit?noteId=${note.noteId}"/>" class="btn">수정</a>
        <a href="<c:url value="/note/delete?noteId=${note.noteId}"/>" class="btn btn-danger">삭제</a>
        <a href="<c:url value="/dashboard"/>" class="btn btn-secondary">목록으로</a>
    </div>
</div>
</body>
</html>