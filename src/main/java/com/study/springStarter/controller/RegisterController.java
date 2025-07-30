//package com.study.springStarter.controller;
//
//import com.study.springStarter.dto.User;
//import com.study.springStarter.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.UnsupportedEncodingException;
//
//@Controller
//public class RegisterController {
//
//    @Autowired
//    UserService service;
//
//    @GetMapping("/register/add")
//    public String register() {
//        return "register";
//    }
//
//    @PostMapping("/register/save")
//    public String register(User user, RedirectAttributes reatt) throws UnsupportedEncodingException {
//        try {
//            if(service.countByEmail(user.getEmail()) > 0) {
//                reatt.addFlashAttribute("errorMessage", "이미 사용중인 이메일입니다.");
//                return "redirect:/register/add";
//            }
//            if(service.countByName(user.getName()) > 0) {
//                reatt.addFlashAttribute("errorMessage", "이미 사용중인 닉네임입니다.");
//                return "redirect:/register/add";
//            }
//
//            int res = service.register(user);
//
//            if(res == 1) {
//                System.out.println("회원가입 성공");
//                reatt.addFlashAttribute("register", "suc");
//                return "redirect:/login";
//            } else {
//                System.out.println("회원가입 실패");
//                reatt.addFlashAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
//                return "redirect:/register/add";
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // 실제 서비스에서는 로깅 시스템을 사용합니다.
//            System.out.println("회원가입 중 예외 발생: " + e.getMessage());
//            reatt.addFlashAttribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
//            return "redirect:/register/add";
//        }
//    }
//}
