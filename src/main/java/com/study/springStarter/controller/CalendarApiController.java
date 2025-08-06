package com.study.springStarter.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.study.springStarter.dto.CalendarEvent;
import com.study.springStarter.service.GoogleCalendarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarApiController {

    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public CalendarApiController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    // google calendar의 이벤트 목록을 가져오는 api

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        try {
            Calendar client = googleCalendarService.getCalendarClient(email);
            if(client == null) {
                return ResponseEntity.status(500).build(); // Forbidden, 연동되지 않음
            }

            Events events = client.events().list("primary")
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            return ResponseEntity.ok(items);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 새로운 google calendar 이벤트를 생성하는 api
    @PostMapping("/events")
    public ResponseEntity<Event> addEvent(@RequestBody CalendarEvent calendarEvent, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            Calendar client = googleCalendarService.getCalendarClient(email);
            if(client == null) {
                return ResponseEntity.status(403).build();
            }

            DateTime start = new DateTime(calendarEvent.getStartDateTime());
            DateTime end = new DateTime(calendarEvent.getEndDateTime());

            EventDateTime startEventDateTime = new EventDateTime().setDateTime(start);
            EventDateTime endEventDateTime = new EventDateTime().setDateTime(end);

            Event event = new Event()
                    .setSummary(calendarEvent.getSummary())
                    .setLocation(calendarEvent.getLocation())
                    .setDescription(calendarEvent.getDescription())
                    .setStart(startEventDateTime)
                    .setEnd(endEventDateTime);

            Event createdEvent = client.events().insert("primary", event).execute();
            return ResponseEntity.ok(createdEvent);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 기존 google calendar 이벤트를 수정하는 api
    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable String eventId, @RequestBody CalendarEvent calendarEvent, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            Calendar client = googleCalendarService.getCalendarClient(email);
            if(client == null) {
                return ResponseEntity.status(403).build();
            }

            DateTime start = new DateTime(calendarEvent.getStartDateTime());
            DateTime end = new DateTime(calendarEvent.getEndDateTime());

            EventDateTime startEventDateTime = new EventDateTime().setDateTime(start).setTimeZone("Asia/Seoul");
            EventDateTime endEventDateTime = new EventDateTime().setDateTime(end).setTimeZone("Asia/Seoul");

            Event event = new Event()
                    .setSummary(calendarEvent.getSummary())
                    .setStart(startEventDateTime)
                    .setEnd(endEventDateTime);

            Event updatedEvent = client.events().update("primary", eventId, event).execute();
            return ResponseEntity.ok(updatedEvent);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Google Calendar 이벤트를 삭제하는 api
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            Calendar client = googleCalendarService.getCalendarClient(email);
            if(client == null) {
                return ResponseEntity.status(403).build();
            }
            client.events().delete("primary" ,eventId).execute();
            return ResponseEntity.noContent().build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
