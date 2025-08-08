package com.study.springStarter.controller;

import com.study.springStarter.dto.User;
import com.study.springStarter.service.FolderService;
import com.study.springStarter.service.NoteService;
import com.study.springStarter.service.UserService;
import com.study.springStarter.util.NoteSearchCondition;
import com.study.springStarter.util.PageHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String dashboard(@ModelAttribute NoteSearchCondition condition, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
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
        condition.setUserId(userId);

        if (condition.getPage() < 1) condition.setPage(1);
        if (condition.getPageSize() < 1) condition.setPageSize(10);

        try {
            m.addAttribute("folders", folderService.findFoldersByUserId(userId));
            // 조건에 맞는 노트의 총 개수를 가져옵니다.
            int totalCnt = noteService.count(condition);
            // 페이징 처리를 위한 PageHandler 객체를 생성합니다.
            PageHandler ph = new PageHandler(totalCnt, condition.getPage(), condition.getPageSize());
            // 페이지네이션 및 필터링이 적용된 노트 목록을 가져옵니다.
            m.addAttribute("notes", noteService.searchAndFilterAndSortNotes(userId, condition));
            // JSP에서 페이징 링크를 생성할 수 있도록 모델에 추가합니다.
            m.addAttribute("ph", ph);
            // 현재 검색 조건을 모델에 추가하여 페이지 상태를 유지합니다.
            m.addAttribute("condition", condition);
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
