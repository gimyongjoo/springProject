package com.study.springStarter.controller;

import com.study.springStarter.dto.BoardDto;
import com.study.springStarter.dto.User;
import com.study.springStarter.service.BoardService;
import com.study.springStarter.service.UserService;
import com.study.springStarter.util.BoardPageHandler;
import com.study.springStarter.util.SearchCondition;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String boardList(SearchCondition sc, HttpSession session, Model m, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        try {
            String email = (String)session.getAttribute("email");
            User user = userService.findByEmail(email);
            m.addAttribute("user", user);

            int count = boardService.getSearchResultCnt(sc); // 게시물 수
            System.out.println("totalcount : " + count);
            BoardPageHandler ph = new BoardPageHandler(count, sc);
            List<BoardDto> list = boardService.getSearchResultPage(sc);
//            m.addAttribute("searchList", searchList);
            m.addAttribute("ph", ph);
//            List<BoardDto> list = boardService.selectAll();
            m.addAttribute("list", list);
            m.addAttribute("today", LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("errorMessage", "게시판 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "boardList";
    }

    @GetMapping("/write")
    public String boardWrite(HttpSession session, Model m, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        String email = (String) session.getAttribute("email");
        try {
            User user = userService.findByEmail(email);
            m.addAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "다시 시도해주세요..");
            return "redirect:/board/list";
        }
        return "boardWrite";
    }

    @PostMapping("/write")
    public String boardWrite(BoardDto dto, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        dto.setWriter(email);
        try {
            int res = boardService.write(dto);
            if(res == 1) {
                reatt.addFlashAttribute("successMessage", "게시물 등록이 완료되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "게시물 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "게시물 등록 중 오류가 발생했습니다.");
        }
       return "redirect:/board/list";
    }

    @GetMapping("/view")
    public String boardView(SearchCondition sc, Integer bno, Model m, HttpSession session, RedirectAttributes reatt, HttpServletRequest req) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        if(bno == null) {
            return "redirect:/board/list";
        }
        try {
            String email = (String)session.getAttribute("email");
            User user = userService.findByEmail(email);
            m.addAttribute("user", user);

            BoardDto dto = boardService.read(bno);
            if (dto == null) {
                reatt.addFlashAttribute("errorMessage", "존재하지 않는 게시물입니다.");
                return "redirect:/board/list";
            }

            m.addAttribute("dto", dto);
            m.addAttribute("sc", sc);
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("errorMessage", "게시판 상세보기를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/board/list";
        }

        return "boardView";
    }

    @GetMapping("/modify")
    public String boardModify(Integer bno, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        if(bno == null) {
            return "redirect:/board/list";
        }
        try {
            String email = (String)session.getAttribute("email");
            User user = userService.findByEmail(email);
            m.addAttribute("user", user);

            BoardDto dto = boardService.read(bno);
            if (dto == null) {
                reatt.addFlashAttribute("errorMessage", "존재하지 않는 게시물입니다.");
                return "redirect:/board/list";
            }
            m.addAttribute("dto", dto);
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("errorMessage", "게시판 수정을 불러오는 중 오류가 발생했습니다.");
            return "redirect:/board/list";
        }
        return "boardModify";
    }

    @PostMapping("/modify")
    public String boardModify(BoardDto dto, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        User user = null;
        String email = (String) session.getAttribute("email");
        dto.setWriter(email);
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        try {
            int res = boardService.modify(dto, dto.getWriter());
            if(res == 1) {
                reatt.addFlashAttribute("successMessage", "게시물 수정이 완료되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "게시물 수정에 실패했습니다.");
                return "redirect:/board/modify?bno=" + dto.getBno();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/board/view?bno=" + dto.getBno();
    }

    @PostMapping("/remove")
    public String boardRemove(BoardDto dto, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        User user = null;
        String email = (String) session.getAttribute("email");
        dto.setWriter(email);
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        try {
            int res = boardService.remove(dto.getBno(), dto.getWriter());
            if(res == 1) {
                reatt.addFlashAttribute("successMessage", "삭제가 성공적으로 완료되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "게시물 삭제에 실패했습니다.");
                return "redirect:/board/view?bno=" + dto.getBno();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/board/list";
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }
}
