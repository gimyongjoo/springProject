package com.study.springStarter.controller;

import com.study.springStarter.service.GoogleCalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
