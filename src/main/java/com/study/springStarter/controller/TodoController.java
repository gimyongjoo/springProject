package com.study.springStarter.controller;

import com.study.springStarter.dto.Todo;
import com.study.springStarter.dto.User;
import com.study.springStarter.service.TodoService;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;

    @Autowired
    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    // 사용자 인증 및 권한 확인 헬퍼 메서드
    private User getCurrentUser(HttpSession session, RedirectAttributes reatt, String requestURI) throws Exception {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            throw new IllegalAccessException("User not logged in with redirect path: /login?toURL=" + requestURI);
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            throw new IllegalAccessException("Invalid user information with redirect path: /login");
        }
        return user;
    }

    /**
     * 새로운 할 일 추가 처리 (POST)
     * 노트 상세 페이지에서 폼 제출로 호출됨
     */
    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo, HttpSession session, RedirectAttributes reatt, HttpServletRequest req) {
        int noteId = (todo.getNoteId() != null) ? todo.getNoteId() : 0;

        try {
            User currentUser = getCurrentUser(session, reatt, req.getRequestURL().toString());
            todo.setUserId(currentUser.getUserId());
            todo.setDone(false);

            int result = todoService.addTodo(todo);
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "할 일이 성공적으로 추가되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "할 일 추가에 실패했습니다.");
            }
        } catch (IllegalAccessException e) {
            String redirectPath = e.getMessage().substring(e.getMessage().indexOf("redirect path:") + "redirect path:".length()).trim();
            return redirectPath;
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "할 일 추가 중 오류가 발생했습니다.");
        }
        return "redirect:/note/view?noteId=" + noteId;
    }

    /**
     * 할 일 완료 상태 토글 (GET 요청, 간단한 처리를 위해)
     */
    @GetMapping("/toggleDone")
    public String toggleTodoDone(@RequestParam("todoId") int todoId,
                                 @RequestParam("noteId") int noteId,
                                 HttpSession session, RedirectAttributes reatt, HttpServletRequest req) {
        try {
            User currentUser = getCurrentUser(session, reatt, req.getRequestURL().toString());

            int result = todoService.toggleTodoDone(todoId, currentUser.getUserId());

            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "할 일 상태가 업데이트되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "할 일 상태 업데이트에 실패했습니다.");
            }
        } catch (IllegalAccessException e) {
            String redirectPath = e.getMessage().substring(e.getMessage().indexOf("redirect path:") + "redirect path:".length()).trim();
            return redirectPath;
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "할 일 상태 업데이트 중 오류가 발생했습니다.");
        }
        return "redirect:/note/view?noteId=" + noteId;
    }

    /**
     * 할 일 삭제 처리 (GET 요청, 간단한 처리를 위해)
     */
    @GetMapping("/delete")
    public String deleteTodo(@RequestParam("todoId") int todoId,
                             @RequestParam("noteId") int noteId,
                             HttpSession session, RedirectAttributes reatt, HttpServletRequest req) {
        try {
            User currentUser = getCurrentUser(session, reatt, req.getRequestURL().toString());

            int result = todoService.deleteTodo(todoId, currentUser.getUserId());
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "할 일이 성공적으로 삭제되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "할 일 삭제에 실패했습니다.");
            }
        } catch (IllegalAccessException e) {
            String redirectPath = e.getMessage().substring(e.getMessage().indexOf("redirect path:") + "redirect path:".length()).trim();
            return redirectPath;
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "할 일 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/note/view?noteId=" + noteId;
    }
}