package com.study.springStarter.controller;

import com.study.springStarter.dto.User;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String pwd, boolean rememberId, String toURL, HttpSession session, HttpServletResponse resp, RedirectAttributes reatt) {
        toURL = "".equals(toURL) || toURL == null ? "/" : toURL;
        if(!isValid(email, pwd)) {
            reatt.addFlashAttribute("errorMessage", "아이디나 비밀번호를 잘못 입력하셨습니다.");
            return "redirect:/login";
        }
        if(rememberId) {
            Cookie cookie = new Cookie("email", email);
            resp.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("email", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }

        session.setAttribute("email", email);
        return "redirect:" + toURL;
    }

    private boolean isValid(String email, String pwd) {
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user == null) {
            return false;
        }
        return user.getEmail().equals(email) && user.getPwd().equals(pwd);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("로그아웃");
        return "redirect:/";
    }

    @GetMapping("/register/add")
    public String register() {
        return "register";
    }

    @PostMapping("/register/save")
    public String register(User user, RedirectAttributes reatt) throws UnsupportedEncodingException {
        try {
            if(userService.countByEmail(user.getEmail()) > 0) {
                reatt.addFlashAttribute("errorMessage", "이미 사용중인 이메일입니다.");
                return "redirect:/register/add";
            }
            if(userService.countByName(user.getName()) > 0) {
                reatt.addFlashAttribute("errorMessage", "이미 사용중인 닉네임입니다.");
                return "redirect:/register/add";
            }

            int res = userService.register(user);

            if(res == 1) {
                System.out.println("회원가입 성공");
                reatt.addFlashAttribute("successMessage", "회원가입이 완료되었습니다.");
                return "redirect:/login";
            } else {
                System.out.println("회원가입 실패");
                reatt.addFlashAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
                return "redirect:/register/add";
            }
        } catch (Exception e) {
            e.printStackTrace(); // 실제 서비스에서는 로깅 시스템을 사용합니다.
            System.out.println("회원가입 중 예외 발생: " + e.getMessage());
            reatt.addFlashAttribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "redirect:/register/add";
        }
    }

    @ResponseBody
    @GetMapping("/register/idcheck/{email}")
    public ResponseEntity<String> idcheck(@PathVariable String email) {
        int cnt = 0;
        try {
            cnt = userService.countByEmail(email);
            return new ResponseEntity<>(cnt+"", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(cnt+"", HttpStatus.BAD_REQUEST);
        }
    }

}
