<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🗒️</text></svg>">
    <title>마이노션 - 대시보드</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.css" rel="stylesheet" />
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
        .fc-day-mon .fc-daygrid-day-number,
        .fc-day-tue .fc-daygrid-day-number,
        .fc-day-wed .fc-daygrid-day-number,
        .fc-day-thu .fc-daygrid-day-number,
        .fc-day-fri .fc-daygrid-day-number {
            color: black !important;
        }
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

        .fc-event-title {
            text-align: right;
            width: 100%;
            display: block;
        }

        /* 폴더 목록 내 버튼 그룹 스타일 */
        .folder-actions .btn {
            padding: 0.25rem 0.5rem;
            font-size: 0.7rem;
            line-height: 1.5;
        }

        .pagination {
            justify-content: center !important;
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
                    <a href="<c:url value='/dashboard'/>" class="<c:if test="${empty param.folderId}">text-white</c:if> text-decoration-none">모든 노트</a>
                </li>
                <c:forEach var="folder" items="${folders}">
                    <li class="list-group-item d-flex justify-content-between align-items-center <c:if test="${param.folderId == folder.folderId}">active</c:if>">
                        <a href="<c:url value='/note/list?folderId=${folder.folderId}'/>" class="<c:if test="${param.folderId == folder.folderId}">text-white</c:if> text-decoration-none flex-grow-1">${folder.name}</a>
                        <div class="folder-actions d-flex align-items-center">
                            <a href="<c:url value='/folder/edit?folderId=${folder.folderId}'/>" class="btn btn-sm btn-info text-white p-1 me-1" style="font-size: 0.8rem;">
                                수정
                            </a>
                            <form action="<c:url value='/folder/delete'/>" method="get" onsubmit="return confirm('정말 이 폴더를 삭제하시겠습니까?');">
                                <input type="hidden" name="folderId" value="${folder.folderId}" />
                                <button type="submit" class="btn btn-sm btn-danger p-1 border-0" style="font-size: 0.8rem;">
                                    삭제
                                </button>
                            </form>
                        </div>
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
                    <a href="<c:url value='/note/add?folderId=${param.folderId}'/>" class="btn btn-primary">새 노트 작성</a>
                </div>
            </div>

            <div class="row row-cols-1 row-cols-md-2 g-4">
                <c:forEach var="note" items="${notes}">
                    <div class="col">
                        <div class="card h-100 note-card" onclick="location.href='<c:url value="/note/view?noteId=${note.noteId}&page=${condition.page}&folderId=${condition.folderId}"/>'">
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
            </div> <!-- row-cols 닫는 div 위치 주의 -->

            <div class="text-center mt-4">
                <nav class="d-inline-block">
                    <ul class="pagination justify-content-center">
                        <c:if test="${ph.showPrev}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${ph.beginPage - 1}&keyword=${condition.keyword}&folderId=${condition.folderId}">&laquo;</a>
                            </li>
                        </c:if>
                        <c:forEach begin="${ph.beginPage}" end="${ph.endPage}" var="i">
                            <li class="page-item ${condition.page == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&keyword=${condition.keyword}&folderId=${condition.folderId}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${ph.showNext}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${ph.endPage + 1}&keyword=${condition.keyword}&folderId=${condition.folderId}">&raquo;</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
            <hr class="my-5">
            <h2 class="mb-4">내 캘린더</h2>
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div id='calendar'></div>
                </div>
            </div>
        </div>
    </div>
<!-- 일정 추가 모달 -->
<div class="modal fade" id="eventModal" tabindex="-1" aria-labelledby="eventModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="eventForm">
                <div class="modal-header">
                    <h5 class="modal-title" id="eventModalLabel">일정 추가</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="eventTitle" class="form-label">제목</label>
                        <input type="text" class="form-control" id="eventTitle" required>
                    </div>
                    <div class="mb-3">
                        <label for="startDateTime" class="form-label">시작일시</label>
                        <input type="datetime-local" class="form-control" id="startDateTime" required>
                    </div>
                    <div class="mb-3">
                        <label for="endDateTime" class="form-label">종료일시</label>
                        <input type="datetime-local" class="form-control" id="endDateTime" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">저장</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                </div>
            </form>
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

        var isGoogleCalendarLinked = ${not empty sessionScope.googleCalendarCredential};

        var eventSources = [];

        if (isGoogleCalendarLinked) {
            eventSources.push({
                events: function(fetchInfo, successCallback, failureCallback) {
                    $.ajax({
                        url: '<c:url value="/api/calendar/events"/>',
                        type: 'GET',
                        success: function(response) {
                            console.log(response);
                            const events = response.map(function(event) {
                                var start, end, allDay;
                                if (event.start && event.start.date) {
                                    start = event.start.date;
                                    end = event.end.date;
                                    allDay = true;
                                } else {
                                    start = event.start.dateTime?.value || event.start.date?.value;
                                    end = event.end.dateTime?.value || event.end.date?.value;
                                    allDay = false;
                                }
                                return {
                                    id: event.id,
                                    title: event.summary,
                                    start: start,
                                    end: end,
                                    allDay: true
                                };
                            });
                            console.log("✅ FullCalendar에 전달할 events", events);
                            console.log("📌 첫 번째 이벤트 확인", events[0]);
                            successCallback(events);
                        },
                        error: function(xhr) {
                            failureCallback(xhr);
                        }
                    });
                }
            });
        } else {
            console.log('Google Calendar가 연동되지 않았습니다. API 호출을 건너뜁니다.');
        }

        eventSources.push({
            googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
            className: 'fc-event-holiday'
        });

        var calendar = new FullCalendar.Calendar(calendarEl, {
            googleCalendarApiKey: 'AIzaSyBMiwYomEyL5-Ub0X0Uw-ZydbOXkJGYBTc',
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            editable: true,
            droppable: true,
            dayMaxEvents: true,
            eventSources: eventSources,

            dateClick: function(info) {
                if (!isGoogleCalendarLinked) {
                    alert('Google Calendar 연동이 필요합니다.');
                    return;
                }

                clickedDate = info.dateStr;
                document.getElementById('startDateTime').value = clickedDate + "T10:00";
                document.getElementById('endDateTime').value = clickedDate + "T11:00";
                document.getElementById('eventTitle').value = "";

                const modal = new bootstrap.Modal(document.getElementById('eventModal'));
                modal.show();
            },

            eventDrop: function(info) {
                if (!isGoogleCalendarLinked) {
                    alert('Google Calendar 연동이 필요합니다.');
                    info.revert();
                    return;
                }
                var event = info.event;
                var updatedEventDto = {
                    summary: event.title,
                    startDateTime: event.start.toISOString(),
                    endDateTime: event.end ? event.end.toISOString() : event.start.toISOString()
                };

                $.ajax({
                    url: '<c:url value="/api/calendar/events/"/>' + event.id,
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(updatedEventDto),
                    success: function(response) {
                        alert('이벤트가 성공적으로 수정되었습니다.');
                    },
                    error: function(xhr, status, error) {
                        if (xhr.status === 403) {
                            alert('Google Calendar 연동이 필요합니다.');
                        } else {
                            alert('이벤트 수정에 실패했습니다.');
                            info.revert();
                        }
                    }
                });
            },

            eventClick: function(info) {
                if (!isGoogleCalendarLinked) {
                    alert('Google Calendar 연동이 필요합니다.');
                    return;
                }
                var event = info.event;
                var action = confirm("'" + event.title + "' 이벤트를 삭제하시겠습니까?\n\n[확인]을 누르면 삭제, [취소]를 누르면 수정합니다.");

                if (action) {
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
                } else {
                    var newTitle = prompt('수정할 이벤트 제목을 입력하세요:', event.title);
                    if (newTitle && newTitle !== event.title) {
                        var updatedEventDto = {
                            summary: newTitle,
                            startDateTime: event.start.toISOString(),
                            endDateTime: event.end ? event.end.toISOString() : event.start.toISOString()
                        };

                        $.ajax({
                            url: '<c:url value="/api/calendar/events/"/>' + event.id,
                            type: 'PUT',
                            contentType: 'application/json',
                            data: JSON.stringify(updatedEventDto),
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
        document.getElementById('eventForm').addEventListener('submit', function (e) {
            e.preventDefault();

            const newEventDto = {
                summary: document.getElementById('eventTitle').value,
                startDateTime: document.getElementById('startDateTime').value + ":00+09:00",
                endDateTime: document.getElementById('endDateTime').value + ":00+09:00",
                allDay: false
            };

            $.ajax({
                url: '<c:url value="/api/calendar/events"/>',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(newEventDto),
                success: function (response) {
                    const modalEl = document.getElementById('eventModal');
                    bootstrap.Modal.getInstance(modalEl).hide();
                    calendar.refetchEvents();
                    alert('이벤트가 추가되었습니다!');
                },
                error: function (xhr) {
                    alert('이벤트 추가에 실패했습니다.');
                }
            });
        });
        calendar.render();
    });
</script>
</body>
</html>