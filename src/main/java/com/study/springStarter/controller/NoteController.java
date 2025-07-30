package com.study.springStarter.controller;

import com.study.springStarter.dto.Folder;
import com.study.springStarter.dto.Note;
import com.study.springStarter.dto.User;
import com.study.springStarter.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService; // userId 조회를 위해 필요
    private final CheckListService checkListService;
    private final ImageService imageService;
    private final TodoService todoService;
    private final FolderService folderService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService, CheckListService checkListService, ImageService imageService, TodoService todoService, FolderService folderService) {
        this.noteService = noteService;
        this.userService = userService;
        this.checkListService = checkListService;
        this.imageService = imageService;
        this.todoService = todoService;
        this.folderService = folderService;
    }

    @GetMapping("/list")
    public String listNotes(@RequestParam("folderId") Integer folderId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
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
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        List<Note> notes;
        try {
            if (folderId != null) {
                Folder folder = folderService.findFolderById(folderId);
                if (folder == null || folder.getUserId() != user.getUserId()) {
                    reatt.addFlashAttribute("errorMessage", "해당 폴더에 접근할 권한이 없습니다.");
                    return "redirect:/dashboard";
                }
                notes = noteService.getNotesByFolderId(folderId);
                m.addAttribute("currentFolder", folder); // 현재 폴더 정보 전달 (옵션)
            } else {
                notes = noteService.getNotesByUserId(user.getUserId()); // 모든 노트
            }
            m.addAttribute("notes", notes);
            m.addAttribute("folderId", folderId); // <--- 이 부분을 추가합니다. noteList.jsp에서 /note/add 링크를 만들 때 folderId를 전달하기 위함
            m.addAttribute("user", user);

        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 목록을 불러오는 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }
        return "noteList";
    }

    @GetMapping("/view")
    public String viewNote(@RequestParam("noteId") Integer noteId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }
        if (noteId == null) {
            reatt.addFlashAttribute("errorMessage", "노트 번호가 잘못되었습니다.");
            return "redirect:/dashboard";
        }
        String email = (String) session.getAttribute("email");

        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            Note note = noteService.findNoteById(noteId);
            if(note == null || note.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 노트를 조회할 권한이 없습니다.");
                return "redirect:/dashboard";
            }

            m.addAttribute("note", note);
            m.addAttribute("checkLists", checkListService.findCheckListsByNoteId(noteId));
            m.addAttribute("images", imageService.findImagesByNoteId(noteId));
            m.addAttribute("todos", todoService.findTodosByNoteId(noteId));
            m.addAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 상세 정보를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }

        return "noteView";
    }

    @GetMapping("/add")
    public String addNoteForm(Integer folderId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
        if(!loginCheck(session)) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login?toURL=" + req.getRequestURL();
        }

        Note note = new Note();
        if(folderId != null) {
            note.setFolderId(folderId);
        }
        m.addAttribute("mode", "add");
        m.addAttribute("note", note); // 폼 바인딩을 위해 빈 Note 객체 전달

        return "noteForm";
    }

    // 노트 수정 폼
    @GetMapping("/edit")
    public String editNoteForm(@RequestParam("noteId") int noteId, Model m, HttpSession session, HttpServletRequest req, RedirectAttributes reatt) {
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
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            Note note = noteService.findNoteById(noteId);
            if (note == null || note.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 노트를 수정할 권한이 없습니다.");
                return "redirect:/dashboard";
            }

            m.addAttribute("note", note);
            m.addAttribute("mode", "edit");
            m.addAttribute("user", user);

        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 정보를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }
        return "noteForm";
    }

    @PostMapping("/add")
    public String addNote(@RequestParam Map<String, String> params, @ModelAttribute Note note, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if(note.getIsPinned() == null) note.setIsPinned(false);
        if (email == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            note.setUserId(user.getUserId()); // 현재 로그인한 사용자의 ID를 노트에 설정

            int res = noteService.addNote(note);
            if (res > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 추가되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 추가 중 오류가 발생했습니다.");
        }
        System.out.println(note.getNoteId());
        System.out.println(note.getFolderId());
        System.out.println(note.getUserId());


        System.out.println(params);
        System.out.println("isPinned from form: " + note.getIsPinned());
        return "redirect:/dashboard";
    }

    @PostMapping("/edit")
    public String editNote(@ModelAttribute Note note, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            // 수정 권한 확인: 노트의 소유자가 현재 로그인한 사용자인지 다시 확인 (보안을 위해 중요)
            Note existingNote = noteService.findNoteById(note.getNoteId());
            if (existingNote == null || existingNote.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 노트를 수정할 권한이 없습니다.");
                return "redirect:/dashboard";
            }
            note.setUserId(user.getUserId()); // 현재 로그인한 사용자 ID를 다시 설정 (필수 아님, 안전성 강화)

            System.out.println("====== Debugging isPinned ======");
            System.out.println("폼에서 @ModelAttribute로 바인딩된 note.getIsPinned(): " + note.getIsPinned());
            System.out.println("====== End Debugging ======");

            int res = noteService.updateNote(note);
            if (res > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 수정되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/note/view?noteId=" + note.getNoteId(); // 수정 후 상세 보기 페이지로 이동
    }

    @GetMapping("/delete") // GET 요청으로 단순 삭제는 권장되지 않으나, 현재 JSP 구조상 GET으로 처리 (POST/DELETE 권장)
    public String deleteNote(@RequestParam("noteId") int noteId, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            reatt.addFlashAttribute("errorMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/login";
        }
        if (user == null) {
            reatt.addFlashAttribute("errorMessage", "유효하지 않은 사용자 정보입니다.");
            return "redirect:/login";
        }

        try {
            // 삭제 권한 확인: 노트의 소유자가 현재 로그인한 사용자인지 다시 확인 (보안을 위해 중요)
            Note existingNote = noteService.findNoteById(noteId);
            if (existingNote == null || existingNote.getUserId() != user.getUserId()) {
                reatt.addFlashAttribute("errorMessage", "해당 노트를 삭제할 권한이 없습니다.");
                return "redirect:/dashboard";
            }

            int result = noteService.deleteNote(noteId);
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 삭제되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/dashboard"; // 삭제 후 대시보드로 이동
    }


    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }
}
