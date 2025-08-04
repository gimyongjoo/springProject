<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>노트 작성/수정</title>
    <style>
        /* 기본 스타일 - dashboard.jsp와 동일 */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 800px; /* 폼에 맞게 너비 조정 */
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

        /* 폼 요소 스타일 */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-group input[type="text"],
        .form-group textarea {
            width: calc(100% - 22px); /* padding 고려 */
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            box-sizing: border-box; /* 패딩이 너비에 포함되도록 */
        }

        .form-group textarea {
            min-height: 250px; /* 높이 설정 */
            resize: vertical; /* 세로 크기 조절 가능 */
        }

        .form-group input[type="checkbox"] {
            margin-right: 8px;
        }

        /* 버튼 스타일 - dashboard.jsp와 동일 */
        .btn {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
            margin-right: 10px; /* 버튼 간 간격 */
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #2980b9;
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
<%@ include file="navi.jsp"%>

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
        <c:if test="${mode eq 'add'}">새 노트 작성</c:if>
        <c:if test="${mode eq 'edit'}">노트 수정</c:if>
    </h2>
    <form id="noteForm" action="<c:url value="/note/${mode}"/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="noteId" value="${note.noteId}" />
        <input type="hidden" name="folderId" value="${note.folderId}" />
        <input type="hidden" name="mode" value="${mode}" />

        <div class="form-group">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" value="${note.title}" required>
        </div>

        <div class="form-group">
            <label for="content">내용</label>
            <textarea id="content" name="content">${note.content}</textarea>
        </div>

        <div class="form-group">
            <label for="imageUpload">이미지 첨부</label>
            <input type="file" id="imageUpload" name="files" multiple accept="image/*">
            <small class="form-text text-muted">노트 내용에 이미지를 삽입할 수 있습니다. 이미지는 마크다운 형식으로 자동으로 추가됩니다.</small>
        </div>

        <div class="form-group">
            <input type="checkbox" id="isPinned" name="isPinned" value="true"
                   <c:if test="${note.isPinned eq true}">checked</c:if> />
            <label for="isPinned" style="display: inline;">핀 고정</label>
        </div>

        <button type="submit" class="btn">
            <c:choose>
                <c:when test="${mode == 'add'}">저장</c:when>
                <c:otherwise>수정</c:otherwise>
            </c:choose>
        </button>
        <a href="<c:url value="/dashboard"/>" class="btn btn-secondary">취소</a>
    </form>
</div>

<%@ include file="footer.jsp"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $('#imageUpload').on('change', function(e) {
            var files = e.target.files;
            if (files.length > 0) {
                var formData = new FormData();
                var noteId = $('input[name="noteId"]').val();

                // 노트가 새로 추가되는 경우 noteId가 없으므로, 경고 메시지를 표시합니다.
                if (!noteId || noteId === '0') {
                    alert('새 노트 작성 시에는 먼저 노트를 저장한 후 이미지를 추가해 주세요.');
                    // 파일 선택 취소
                    $(this).val('');
                    return;
                }

                formData.append('noteId', noteId);
                // multiple 속성을 사용하므로 파일들을 루프를 돌며 추가합니다.
                for (var i = 0; i < files.length; i++) {
                    formData.append('file', files[i]);
                }

                $.ajax({
                    url: '<c:url value="/note/uploadImage"/>',
                    type: 'POST',
                    data: formData,
                    processData: false, // data를 쿼리 문자열로 변환하지 않음
                    contentType: false, // 서버에 보낼 데이터의 컨텐츠 타입을 설정하지 않음
                    success: function(response) {
                        if (response.success) {
                            var contentTextarea = $('#content');
                            var currentContent = contentTextarea.val();
                            // 업로드 성공 시, 반환된 파일 경로를 마크다운 형식으로 내용에 추가
                            var newContent = currentContent + '\n\n' + '![image](' + response.filePath + ')\n';
                            contentTextarea.val(newContent);
                            alert('이미지 업로드 성공');
                            // 파일 선택 초기화
                            $('#imageUpload').val('');
                        } else {
                            alert('이미지 업로드 실패: ' + response.error);
                        }
                    },
                    error: function(xhr, status, error) {
                        alert('이미지 업로드 중 오류가 발생했습니다.');
                        console.error("AJAX Error: ", status, error);
                    }
                });
            }
        });
    });
</script>
</body>
</html>