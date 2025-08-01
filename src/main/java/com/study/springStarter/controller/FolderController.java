package com.study.springStarter.controller;

import com.study.springStarter.dto.Folder;
import com.study.springStarter.dto.Note;
import com.study.springStarter.dto.User;
import com.study.springStarter.service.FolderService;
import com.study.springStarter.service.NoteService;
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;
    private final NoteService noteService;

    @Autowired
    public FolderController(FolderService folderService, UserService userService, NoteService noteService) {
        this.folderService = folderService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("/add")
    public String addFolderForm(Integer folderId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
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
            reatt.addFlashAttribute("errorMessage" ,"사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if(user == null) {
            reatt.addFlashAttribute("errorMessage" , "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        m.addAttribute("folder", new Folder());
        m.addAttribute("mode", "add");

        return "folderForm";
    }

    @PostMapping("/add")
    public String addFolder(@ModelAttribute Folder folder, HttpSession session,HttpServletRequest req, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }

        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage" ,"사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if(user == null) {
            reatt.addFlashAttribute("errorMessage" , "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        if(folder.getUserId() == null) {
            folder.setUserId(user.getUserId());
        }
        try {
            int res = folderService.addFolder(folder);
            if(res > 0) {
                reatt.addFlashAttribute("successMessage", "폴더가 성공적으로 추가되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "폴더 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "폴더 추가 중 오류가 발생했습니다.");
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/edit")
    public String editFolderForm(Integer folderId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if(user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            Folder folder = folderService.findFolderById(folderId);
            if(folder == null || folder.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 폴더를 수정할 권한이 없습니다.");
                return "redirect:/dashboard";
            }

            m.addAttribute("folder", folder);
            m.addAttribute("mode", "edit");
            m.addAttribute("user", user);

        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("", "해당 파일을 불러오는 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }


        return "folderForm";
    }

    @PostMapping("/edit")
    public String editFolder(@ModelAttribute Folder folder, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }

        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if(user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            Folder editFolder = folderService.findFolderById(folder.getFolderId());
            if(editFolder == null || editFolder.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 폴더를 수정할 권한이 없습니다.");
                return "redirect:/dashboard";
            }

            folder.setUserId(user.getUserId());

            int res = folderService.updateFolder(folder);

            if(res > 0) {
                reatt.addFlashAttribute("successMessage", "폴더가 성공적으로 수정되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "폴더 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/delete")
    public String deleteFolder(@RequestParam("folderId") Integer folderId ,HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }

        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if(user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            Folder folder = folderService.findFolderById(folderId);
            if(folder == null || folder.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 폴더를 삭제할 권리가 없습니다.");
                return "redirect:/dashboard";
            }

            int res = folderService.deleteFolder(folderId);
            if(res > 0) {
                reatt.addFlashAttribute("successMessage", "폴더가 성공적으로 삭제되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "폴더 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "폴더 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/dashboard";
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }
}
