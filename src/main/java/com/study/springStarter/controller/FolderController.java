package com.study.springStarter.controller;

import com.study.springStarter.service.FolderService;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;

    @Autowired
    public FolderController(FolderService folderService, UserService userService) {
        this.folderService = folderService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addFolderForm(Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }



        return "folderForm";
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }
}
