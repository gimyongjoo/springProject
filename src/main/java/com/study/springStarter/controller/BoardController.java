package com.study.springStarter.controller;

import com.study.springStarter.dto.BoardDto;
import com.study.springStarter.dto.User;
import com.study.springStarter.service.BoardService;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;
    private UserService userService;

    @Autowired
    public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String boardList(HttpSession session, Model m, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        try {
            String email = (String)session.getAttribute("email");
            User user = userService.findByEmail(email);
            m.addAttribute("user", user);

            List<BoardDto> list = boardService.selectAll();
            m.addAttribute("list", list);
            m.addAttribute("today", LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("errorMessage", "게시판 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "boardList";
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }

    @GetMapping("/view")
    public String boardView(Integer bno, Model m, HttpSession session, RedirectAttributes reatt, HttpServletRequest req) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        if(bno == null) {
            return "redirect:/board/view";
        }
        try {
            BoardDto dto = boardService.read(bno);
            m.addAttribute("dto", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "boardView";
    }
}
