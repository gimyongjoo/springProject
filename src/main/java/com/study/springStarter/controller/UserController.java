package com.study.springStarter.controller;

import com.study.springStarter.dto.User;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
public class UserController {

    private UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

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

//    private boolean isValid(String email, String pwd) {
//        User user = null;
//        try {
//            user = userService.findByEmail(email);
//            if(user==null) return false;
//            System.out.println(user+ " "+ passwordEncoder.matches(pwd, user.getPwd()));
//            return user.getEmail().equals(email) && (user.getPwd().equals(pwd) || passwordEncoder.matches(pwd, user.getPwd()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
////        return user.getEmail().equals(email) && user.getPwd().equals(pwd);
//    }

    private boolean isValid(String email, String pwd) {
        try {
            User user = userService.findByEmail(email);
            if(user != null && ( user.getPwd().equals(pwd) || passwordEncoder.matches(pwd, user.getPwd()))) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    public String register(String email, User user, RedirectAttributes reatt) throws UnsupportedEncodingException {
        if (user.getName() != null) {
            user.setName(user.getName().trim());
        }
        if (user.getName() == null || !user.getName().matches("^[A-Za-z가-힣]+$")) {
            reatt.addFlashAttribute("errorMessage", "이름은 한글/영문만 가능합니다.");
            return "redirect:/register/add";
        }

        try {
            if(!isValidEmail(email)) {
                reatt.addFlashAttribute("errorMessage", "이메일 형식에 맞춰주시기 바랍니다.");
                return "redirect:/register/add";
            }
            if(userService.countByEmail(user.getEmail()) > 0) {
                reatt.addFlashAttribute("errorMessage", "이미 사용중인 이메일입니다.");
                return "redirect:/register/add";
            }
            if(userService.countByName(user.getName()) > 0) {
                reatt.addFlashAttribute("errorMessage", "이미 사용중인 닉네임입니다.");
                return "redirect:/register/add";
            }
            if(user.getPwd().length() < 6 || user.getPwd().length() > 20 ) {
                reatt.addFlashAttribute("errorMessage", "비밀번호는 6~20 이내로 입력해주세요.");
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

    @GetMapping("/register/pwdFind")
    public String pwdFind() {
        return "pwdFind";
    }

    @PostMapping("/register/pwdFind")
    public String pwdFind(String email, String name, Model m, HttpSession session, RedirectAttributes reatt) {
        try {
            User user = userService.findByEmail(email);
            if(user == null) {
                reatt.addFlashAttribute("errorMessage","등록되지 않은 이메일입니다.");
                return "redirect:/register/pwdFind";
            }
            if(!user.getName().equals(name)){
                reatt.addFlashAttribute("errorMessage","이메일과 이름이 일치하지 않습니다.");
                return "redirect:/register/pwdFind";
            }
            String token = UUID.randomUUID().toString();
            session.setAttribute("RESET_TOKEN", token);
            session.setAttribute("RESET_EMAIL", email);

            m.addAttribute("resetToken", token);
            m.addAttribute("email", email);

            return "pwdFind";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage","서버 오류가 발생했습니다.");
            return "redirect:/register/pwdFind";
        }
    }

    @PostMapping("/register/pwdReset")
    public String pwdReset(String email, String token, @RequestParam("pwd") String newPwd,
                           @RequestParam("pwdConfirm") String newPwdConfirm, HttpSession session, RedirectAttributes reatt) {
        try {
            String sessionToken = (String)session.getAttribute("RESET_TOKEN");
            String sessionEmail = (String)session.getAttribute("RESET_EMAIL");

            if(sessionToken == null || !sessionToken.equals(token) || sessionEmail == null || !sessionEmail.equals(email)) {
                reatt.addFlashAttribute("errorMessage", "재설정 절차가 유효하지 않습니다. 처음부터 다시 시도해주세요.");
                return "redirect:/register/pwdFind";
            }
            if(newPwd == null || newPwd.length() < 6 || newPwd.length() > 20) {
                reatt.addFlashAttribute("errorMessage", "비밀번호는 6~20자로 입력해주세요.");
                return "redirect:/register/pwdFind";
            }
            if(!newPwd.equals(newPwdConfirm)) {
                reatt.addFlashAttribute("errorMessage","비밀번호 확인이 일치하지 않습니다.");
                return "redirect:/register/pwdFind";
            }

            String encoded = passwordEncoder.encode(newPwd);
            userService.updatePasswordByEmail(email, encoded);

            session.removeAttribute("RESET_TOKEN");
            session.removeAttribute("RESET_EMAIL");

            reatt.addFlashAttribute("successMessage","비밀번호가 변경되었습니다. 새 비밀번호로 로그인하세요.");
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage","서버 오류가 발생했습니다.");
            return "redirect:/register/pwdFind";
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
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
