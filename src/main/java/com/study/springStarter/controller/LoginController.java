//package com.study.springStarter.controller;
//
//import com.study.springStarter.dto.User;
//import com.study.springStarter.service.UserService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    UserService service;
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String login(String email, String pwd, boolean rememberId, String toURL, HttpSession session, HttpServletResponse resp) {
//        toURL = "".equals(toURL) || toURL == null ? "/" : toURL;
//        if(!isValid(email, pwd)) {
//            return "redirect:/login";
//        }
//        if(rememberId) {
//            Cookie cookie = new Cookie("email", email);
//            resp.addCookie(cookie);
//        } else {
//            Cookie cookie = new Cookie("email", "");
//            cookie.setMaxAge(0);
//            resp.addCookie(cookie);
//        }
//
//        session.setAttribute("email", email);
//        return "redirect:" + toURL;
//    }
//
//    private boolean isValid(String email, String pwd) {
//        User user = null;
//        try {
//            user = service.findByEmail(email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(user == null) {
//            return false;
//        }
//        return user.getEmail().equals(email) && user.getPwd().equals(pwd);
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        System.out.println("로그아웃");
//        return "redirect:/";
//    }
//}
