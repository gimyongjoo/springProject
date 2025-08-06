package com.study.springStarter.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.study.springStarter.service.GoogleCalendarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/google")
public class CalendarController {
    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public CalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    // google calendar 인증을 위한 시작점
    // 사용자를 google 인증 페이지로 redirect함
    @GetMapping("/authorize")
    public String authorize(HttpSession session, HttpServletRequest req, RedirectAttributes reatt) throws IOException {
        if((String)session.getAttribute("email") == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        String email = (String) session.getAttribute("email");

        String authUrl = googleCalendarService.getAuthorizationUrl();
        return "redirect:" + authUrl;
    }

    // google로부터 인증코드를 받아 처리하는 콜백 엔드포인트
    @GetMapping("/oauth2callback")
    public ModelAndView oauth2callback(@RequestParam(value = "code") String code, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        ModelAndView mv = new ModelAndView("redirect:/dashboard");
        String email = (String) session.getAttribute("email");
        if(email == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        try {
            Credential credential = googleCalendarService.exchangeCodeForCredential(code, email);
            if(credential != null) {
                session.setAttribute("googleCalendarCredential", "authorized");
                session.setAttribute("successMessage", "Google Calendar 연동에 성공했습니다.");
            } else {
                session.setAttribute("errorMessage", "Google Calendar 연동에 실패했습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Google Calendar 연동 중 오류가 발생했습니다.");
        }
        return mv;
    }

    // google calendar api 테스트를 위한 임시 엔드포인트(개발 완료 후 삭제 또는 수정)
    @GetMapping("/test")
    public String testCalendar(HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }

        try {
            com.google.api.services.calendar.Calendar client = googleCalendarService.getCalendarClient(email);
            if(client != null) {
                // api 호출 성공
                session.setAttribute("successMessage", "Google Calendar API 클라이언트가 정상적으로 생성되었습니다.");
            } else {
                // 인증 토큰이 없거나 만료됨
                session.setAttribute("errorMessage", "Google Calendar에 인증되지 않았습니다. 다시 연동해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Google Calendar에 인증되지 않았습니다. 다시 연동해주세요.");
        }
        return "redirect:/dashboard";
    }
}
