package com.study.springStarter.controller;

import com.study.springStarter.dto.*;
import com.study.springStarter.util.NoteSearchCondition;
import com.study.springStarter.service.*;
import com.study.springStarter.util.PageHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
//    private final CheckListService checkListService;
    private final ImageService imageService;
    private final TodoService todoService;
    private final FolderService folderService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public NoteController(NoteService noteService, UserService userService, ImageService imageService, TodoService todoService, FolderService folderService) {
        this.noteService = noteService;
        this.userService = userService;
//        this.checkListService = checkListService;
        this.imageService = imageService;
        this.todoService = todoService;
        this.folderService = folderService;
    }

    @GetMapping("/list")
    public String listNotes(
            @RequestParam(value = "folderId", required = false) Integer folderId,
            @ModelAttribute NoteSearchCondition condition, // 검색 조건을 받는 DTO 추가
            Model m, HttpSession session, RedirectAttributes reatt) {

        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
            condition.setUserId(user.getUserId());

            // condition에 folderId가 있으면 설정해줍니다.
            if (folderId != null) {
                condition.setFolderId(folderId);
            }


            // paging 정보 계산 및 전달
            int totalCnt = noteService.count(condition);
            PageHandler ph = new PageHandler(totalCnt, condition.getPage(), condition.getPageSize());
            condition.setOffset(ph.getOffset());
            
            List<Note> notes = noteService.search(condition);

            m.addAttribute("notes", notes);
            m.addAttribute("condition", condition); // 검색 조건을 JSP로 전달
            m.addAttribute("folders", folderService.findFoldersByUserId(user.getUserId()));
            m.addAttribute("user", user);
            m.addAttribute("ph", ph);
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 목록 조회 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }

        m.addAttribute("currentFolderId", folderId);

        // 새 노트 추가 버튼에 folderId를 전달하기 위한 로직
        if (folderId != null) {
            m.addAttribute("addNoteUrl", "/note/add?folderId=" + folderId);
        } else {
            m.addAttribute("addNoteUrl", "/note/add");
        }

        return "dashboard2";
    }

    // 노트 추가 폼
    @GetMapping("/add")
    public String addNoteForm(@RequestParam(value = "folderId", required = false) Integer folderId, Model m) {
        Note note = new Note();
        note.setFolderId(folderId);
        m.addAttribute("note", note);
        m.addAttribute("mode", "add");
        return "noteForm";
    }

    // 노트 추가 처리
    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
        note.setUserId(user.getUserId());
        try {
            int result = noteService.addNote(note);
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 추가되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 추가 중 오류가 발생했습니다.");
        }

        if (note.getFolderId() != null) {
            return "redirect:/note/list?folderId=" + note.getFolderId();
        } else {
            return "redirect:/note/list";
        }
    }

    // 노트 상세 조회
    @GetMapping("/view")
    public String viewNote(
            @RequestParam("noteId") int noteId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "folderId", required = false) Integer folderId,
            Model m, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
        try {
            Note note = noteService.findNoteById(noteId, user.getUserId()); // userId 추가
            List<Todo> todos = todoService.findTodosByNoteId(noteId, user.getUserId());
            if (note.getFolderId() != null) {
                Folder folderName = folderService.findFolderById(note.getFolderId());;
                m.addAttribute("folderName", folderName.getName());
            }

            Parser parser = Parser.builder().build();
            Node document = parser.parse(note.getContent());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(document);
            htmlContent = htmlContent.replace("src=\"/images/", "src=\"/myno/images/");

            m.addAttribute("htmlContent", htmlContent);
            m.addAttribute("todos", todos);
            m.addAttribute("note", note);
//            m.addAttribute("checklists", checkListService.findChecklistsByNoteId(noteId, user.getUserId())); // userId 추가
            m.addAttribute("user", user);
            m.addAttribute("folderId", folderId);
            m.addAttribute("page", page);
            return "noteView";
        } catch (IllegalArgumentException e) {
            reatt.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 조회 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }
    }

    // 노트 수정 폼
    @GetMapping("/edit")
    public String editNoteForm(@RequestParam("noteId") int noteId, Model m, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
        try {
            Note note = noteService.findNoteById(noteId, user.getUserId()); // userId 추가
            m.addAttribute("note", note);
            m.addAttribute("mode", "edit");
            m.addAttribute("user", user);
            return "noteForm";
        } catch (IllegalArgumentException e) {
            reatt.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 조회 중 오류가 발생했습니다.");
            return "redirect:/dashboard";
        }
    }

    // 노트 수정 처리
    @PostMapping("/edit")
    public String editNote(@ModelAttribute Note note, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
        try {
            int result = noteService.updateNote(note, user.getUserId()); // userId 추가
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 수정되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 수정에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            reatt.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/note/view?noteId=" + note.getNoteId();
    }

    // 노트 삭제 처리
    @PostMapping("/delete")
    public String deleteNote(@RequestParam("noteId") int noteId, HttpSession session, RedirectAttributes reatt) {
        String email = (String) session.getAttribute("email");
        if (!loginCheck(session)) {
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
        try {
            int result = noteService.deleteNote(noteId, user.getUserId()); // userId 추가
            if (result > 0) {
                reatt.addFlashAttribute("successMessage", "노트가 성공적으로 삭제되었습니다.");
            } else {
                reatt.addFlashAttribute("errorMessage", "노트 삭제에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            reatt.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            reatt.addFlashAttribute("errorMessage", "노트 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/dashboard";
    }

    // 이미지 업로드 처리
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("noteId") Integer noteId,
            HttpSession session
    ) {
        Map<String, String> response = new HashMap<>();

        if (!loginCheck(session)) {
            response.put("error", "로그인 후 이용 가능합니다.");
            return ResponseEntity.status(401).body(response);
        }

        if (file.isEmpty()) {
            response.put("error", "업로드할 파일이 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 파일 저장 경로 설정 및 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 고유한 파일명 생성
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 파일 저장
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // DB에 이미지 정보 저장
            Image image = new Image();
            image.setNoteId(noteId);
            // 웹에서 접근할 수 있는 경로를 DB에 저장
            image.setFilePath("/images/" + uniqueFileName);
            imageService.addImage(image);

            response.put("success", "이미지 업로드 성공");
            response.put("filePath", image.getFilePath());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private boolean loginCheck(HttpSession session) {
        return session.getAttribute("email") != null;
    }
}