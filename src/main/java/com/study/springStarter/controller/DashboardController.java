package com.study.springStarter.controller;

import com.study.springStarter.dto.User;
import com.study.springStarter.service.FolderService;
import com.study.springStarter.service.NoteService;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {
    FolderService folderService;
    NoteService noteService;
    UserService userService;

    @Autowired
    public DashboardController(FolderService folderService, NoteService noteService, UserService userService) {
        this.folderService = folderService;
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("dashboard")
    public String dashboard(Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        String email = (String) session.getAttribute("email");

        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            return "redirect:/login";
        }

        int userId = user.getUserId();

        try {
            m.addAttribute("folders", folderService.findFoldersByUserId(userId));
            m.addAttribute("notes", noteService.findAllNotesByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login";
        }
        m.addAttribute("user", user);

        return "dashboard2";
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }

}
