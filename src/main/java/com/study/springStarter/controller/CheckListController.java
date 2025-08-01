package com.study.springStarter.controller;

import com.study.springStarter.dto.CheckList; // CheckList로 변경
import com.study.springStarter.dto.User;
import com.study.springStarter.service.CheckListService; // CheckListService로 변경
import com.study.springStarter.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러
@RequestMapping("/api/checklist")
public class CheckListController {

    private final CheckListService checkListService;
    private final UserService userService;

    @Autowired
    public CheckListController(CheckListService checkListService, UserService userService) {
        this.checkListService = checkListService;
        this.userService = userService;
    }

    // 사용자 인증 및 권한 확인 헬퍼 메서드 (컨트롤러 내에서 재사용)
    private User getCurrentUser(HttpSession session) throws Exception {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            throw new IllegalAccessException("로그인 세션이 만료되었거나 유효하지 않습니다.");
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalAccessException("유효하지 않은 사용자 정보입니다.");
        }
        return user;
    }

    // 특정 노트의 체크리스트 목록 조회 (GET /api/checklist/list/{noteId})
    @GetMapping("/list/{noteId}")
    public ResponseEntity<?> getChecklistsByNoteId(@PathVariable("noteId") Integer noteId, HttpSession session) { // noteId Integer로 변경
        try {
            User currentUser = getCurrentUser(session);
            List<CheckList> checkLists = checkListService.findChecklistsByNoteId(noteId, currentUser.getUserId()); // CheckList로 변경
            return ResponseEntity.ok(checkLists);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 노트가 없거나 권한 없는 경우
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("체크리스트 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

     // 새로운 체크리스트 항목 추가 (POST /api/checklist)
    @PostMapping
    public ResponseEntity<?> addChecklist(@RequestBody CheckList checkList, HttpSession session) { // CheckList로 변경
        try {
            User currentUser = getCurrentUser(session);
            // checkList DTO에는 userId가 없지만, 노트의 userId를 통해 간접적으로 권한 확인
            checkList.setChecked(false); // 기본값 false

            CheckList addedChecklist = checkListService.addChecklist(checkList, currentUser.getUserId()); // CheckList로 변경
            return ResponseEntity.status(HttpStatus.CREATED).body(addedChecklist);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 노트가 없거나 권한 없는 경우
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("체크리스트 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

     // 내용 수정 또는 완료 여부 토글을 하나의 API로 처리
    @PutMapping("/{checkListId}")
    public ResponseEntity<?> updateChecklist(@PathVariable("checkListId") Integer checkListId, // Integer로 변경
                                             @RequestBody CheckList checkListUpdate, HttpSession session) { // CheckList로 변경
        try {
            User currentUser = getCurrentUser(session);

            // PathVariable의 ID를 사용하여 업데이트할 객체에 ID 설정
            checkListUpdate.setCheckListId(checkListId);

            CheckList updatedChecklist = checkListService.updateChecklist(checkListUpdate, currentUser.getUserId()); // CheckList로 변경
            return ResponseEntity.ok(updatedChecklist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("체크리스트 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 체크리스트 항목 삭제 (DELETE /api/checklist/{checkListId})
    @DeleteMapping("/{checkListId}")
    public ResponseEntity<?> deleteChecklist(@PathVariable("checkListId") Integer checkListId, HttpSession session) { // Integer로 변경
        try {
            User currentUser = getCurrentUser(session);
            int result = checkListService.deleteChecklist(checkListId, currentUser.getUserId());
            if (result > 0) {
                return ResponseEntity.ok("체크리스트가 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 체크리스트를 찾을 수 없거나 권한이 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("체크리스트 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}