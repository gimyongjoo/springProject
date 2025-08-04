<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>마이노션 - 대시보드</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.css' rel='stylesheet' />

    <style>
        .sidebar {
            background-color: #f8f9fa;
            padding: 20px;
            height: 100vh;
            position: sticky;
            top: 0;
            overflow-y: auto;
        }
        .main-content {
            padding: 30px;
        }
        .note-card {
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .note-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .note-card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        /* 캘린더 날짜 색상 설정 */
        .fc-day-sun .fc-daygrid-day-number {
            color: red;
        }
        .fc-day-sat .fc-daygrid-day-number {
            color: blue;
        }
        /* 공휴일 이벤트 색상 설정 */
        .fc-event-holiday {
            background-color: red;
            border-color: red;
        }
    </style>
</head>
<body>
<%@ include file="navi.jsp"%>
<c:if test="${not empty errorMessage}">
    <script>alert("${errorMessage}");</script>
    <c:remove var="errorMessage" scope="session"/>
</c:if>
<c:if test="${not empty successMessage}">
    <script>alert("${successMessage}");</script>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2 sidebar">
            <h3>폴더</h3>
            <div class="d-grid gap-2">
                <a href="<c:url value='/folder/add'/>" class="btn btn-success">새 폴더 추가</a>
            </div>
            <ul class="list-group mt-3">
                <li class="list-group-item <c:if test="${empty param.folderId}">active</c:if>">
                    <a href="<c:url value='/dashboard2'/>" class="<c:if test="${empty param.folderId}">text-white</c:if> text-decoration-none">모든 노트</a>
                </li>
                <c:forEach var="folder" items="${folders}">
                    <li class="list-group-item <c:if test="${param.folderId == folder.folderId}">active</c:if>">
                        <a href="<c:url value='/note/list?folderId=${folder.folderId}'/>" class="<c:if test="${param.folderId == folder.folderId}">text-white</c:if> text-decoration-none">${folder.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-md-10 main-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>노트 목록</h2>
                <div>
                    <c:if test="${empty sessionScope.googleCalendarCredential}">
                        <a href="<c:url value='/google/authorize'/>" class="btn btn-primary me-2">Google Calendar 연동</a>
                    </c:if>
                    <a href="<c:url value='/note/add'/>" class="btn btn-primary">새 노트 작성</a>
                </div>
            </div>

            <div class="row row-cols-1 row-cols-md-2 g-4">
                <c:forEach var="note" items="${notes}">
                    <div class="col">
                        <div class="card h-100 note-card" onclick="location.href='<c:url value="/note/view?noteId=${note.noteId}"/>'">
                            <div class="card-body">
                                <div class="note-card-header mb-2">
                                    <h5 class="card-title text-truncate me-2">${note.title}</h5>
                                    <div>
                                        <c:if test="${note.isPinned}">
                                            <span class="badge bg-warning text-dark me-2">📌</span>
                                        </c:if>
                                        <a href="<c:url value='/note/edit?noteId=${note.noteId}'/>" class="btn btn-sm btn-outline-secondary p-1 border-0" style="font-size: 0.8rem;" onclick="event.stopPropagation();">수정</a>
                                        <form action="<c:url value='/note/delete'/>" method="post" style="display:inline;" onsubmit="return confirm('정말 이 노트를 삭제하시겠습니까?');">
                                            <input type="hidden" name="noteId" value="${note.noteId}" />
                                            <button type="submit" class="btn btn-sm btn-danger p-1 border-0" style="font-size: 0.8rem;" onclick="event.stopPropagation();">
                                                삭제
                                            </button>
                                        </form>
                                    </div>
                                </div>
                                <p class="card-text text-muted">${note.content.length() > 50 ? note.content.substring(0, 50).concat('...') : note.content}</p>
                            </div>
                            <div class="card-footer text-muted">
                                <fmt:parseDate value="${note.updatedDate}" pattern="yyyy-MM-dd'T'HH:mm" var="updatedDateObj"/>
                                <small>최종 수정일: <fmt:formatDate value="${updatedDateObj}" pattern="yyyy.MM.dd HH:mm"/></small>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty notes}">
                    <div class="col-12">
                        <div class="alert alert-info text-center" role="alert">
                            노트가 없습니다. 새 노트를 추가해 보세요!
                        </div>
                    </div>
                </c:if>
            </div>

            <hr class="my-5">

            <h2 class="mb-4">Google 캘린더</h2>
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div id='calendar'></div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.js'></script>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/google-calendar@6.1.11/index.global.min.js'></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');

        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            editable: true, // 이벤트 수정(드래그, 리사이즈) 가능하게 설정
            droppable: true,
            dayMaxEvents: true, // 하루에 표시할 최대 이벤트 수

            // 캘린더 이벤트 소스 설정 (여러 개 가능)
            eventSources: [
                {
                    // 1. 사용자 기본 캘린더 (우리가 만든 API 연동)
                    url: '<c:url value="/api/calendar/events"/>',
                    failure: function(response) {
                        if (response.status === 403) {
                            // 연동이 안 된 경우 사용자에게 알림
                            alert('Google Calendar가 연동되지 않았습니다. 연동 버튼을 눌러주세요.');
                        } else {
                            alert('Google Calendar 이벤트를 불러오는 중 오류가 발생했습니다.');
                        }
                    }
                },
                {
                    // 2. 한국 공휴일 캘린더 (별도 연동)
                    googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
                    className: 'fc-event-holiday' // 공휴일 이벤트에 적용할 CSS 클래스
                }
            ],

            // 이벤트 생성
            dateClick: function(info) {
                var title = prompt('새로운 이벤트 제목을 입력하세요:');
                if (title) {
                    var newEvent = {
                        title: title,
                        start: info.dateStr,
                        allDay: info.allDay
                    };

                    $.ajax({
                        url: '<c:url value="/api/calendar/events"/>',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(newEvent),
                        success: function(response) {
                            calendar.addEvent(response);
                            alert('이벤트가 추가되었습니다!');
                        },
                        error: function(xhr, status, error) {
                            if (xhr.status === 403) {
                                alert('Google Calendar 연동이 필요합니다.');
                            } else {
                                alert('이벤트 추가에 실패했습니다.');
                            }
                        }
                    });
                }
            },

            // 이벤트 수정 (드래그앤드롭, 리사이즈)
            eventDrop: function(info) {
                var event = info.event;
                var updatedEvent = {
                    id: event.id,
                    title: event.title,
                    start: event.start.toISOString(),
                    end: event.end ? event.end.toISOString() : null,
                    allDay: event.allDay
                };

                $.ajax({
                    url: '<c:url value="/api/calendar/events/"/>' + event.id,
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(updatedEvent),
                    success: function(response) {
                        alert('이벤트가 성공적으로 수정되었습니다.');
                    },
                    error: function(xhr, status, error) {
                        if (xhr.status === 403) {
                            alert('Google Calendar 연동이 필요합니다.');
                        } else {
                            alert('이벤트 수정에 실패했습니다.');
                            info.revert(); // 오류 시 원래대로 되돌리기
                        }
                    }
                });
            },

            // 이벤트 클릭 (수정/삭제)
            eventClick: function(info) {
                var event = info.event;
                var action = confirm("'" + event.title + "' 이벤트를 수정하거나 삭제하시겠습니까?\n\n[확인]을 누르면 삭제, [취소]를 누르면 수정합니다.");

                if (action) { // 확인 (삭제)
                    $.ajax({
                        url: '<c:url value="/api/calendar/events/"/>' + event.id,
                        type: 'DELETE',
                        success: function(response) {
                            event.remove();
                            alert('이벤트가 삭제되었습니다.');
                        },
                        error: function(xhr, status, error) {
                            if (xhr.status === 403) {
                                alert('Google Calendar 연동이 필요합니다.');
                            } else {
                                alert('이벤트 삭제에 실패했습니다.');
                            }
                        }
                    });
                } else { // 취소 (수정)
                    var newTitle = prompt('수정할 이벤트 제목을 입력하세요:', event.title);
                    if (newTitle && newTitle !== event.title) {
                        var updatedEvent = {
                            id: event.id,
                            title: newTitle,
                            start: event.start.toISOString(),
                            end: event.end ? event.end.toISOString() : null,
                            allDay: event.allDay
                        };

                        $.ajax({
                            url: '<c:url value="/api/calendar/events/"/>' + event.id,
                            type: 'PUT',
                            contentType: 'application/json',
                            data: JSON.stringify(updatedEvent),
                            success: function(response) {
                                event.setProp('title', newTitle);
                                alert('이벤트가 수정되었습니다.');
                            },
                            error: function(xhr, status, error) {
                                if (xhr.status === 403) {
                                    alert('Google Calendar 연동이 필요합니다.');
                                } else {
                                    alert('이벤트 수정에 실패했습니다.');
                                }
                            }
                        });
                    }
                }
            }
        });

        calendar.render();
    });
</script>
</body>
</html>