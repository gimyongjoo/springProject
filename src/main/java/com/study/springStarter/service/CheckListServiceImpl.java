//package com.study.springStarter.service;
//
//import com.study.springStarter.dto.CheckList; // CheckList로 변경
//import com.study.springStarter.dto.Note;
//import com.study.springStarter.mapper.CheckListMapper; // CheckListMapper로 변경
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class CheckListServiceImpl implements CheckListService { // CheckListServiceImpl로 변경
//
//    private final CheckListMapper checkListMapper; // CheckListMapper로 변경
//    private final NoteService noteService; // 노트 서비스 주입
//
//    @Autowired
//    public CheckListServiceImpl(CheckListMapper checkListMapper, NoteService noteService) { // CheckListMapper로 변경
//        this.checkListMapper = checkListMapper;
//        this.noteService = noteService;
//    }
//
//    // 노트 소유권 확인 헬퍼 메서드
//    private void checkNoteOwnership(Integer noteId, int userId) throws Exception {
//        if (noteId == null) {
//            throw new IllegalArgumentException("노트 ID는 필수입니다.");
//        }
//        Note note = noteService.findNoteById(noteId, userId); // 노트 조회
//        if (note == null) {
//            throw new IllegalArgumentException("노트를 찾을 수 없습니다.");
//        }
//        if (note.getUserId() != userId) {
//            throw new IllegalAccessException("해당 노트에 대한 접근 권한이 없습니다.");
//        }
//    }
//
//    @Override
//    @Transactional
//    public CheckList addChecklist(CheckList checkList, int userId) throws Exception { // CheckList로 변경
//        // 노트 소유권 확인
//        checkNoteOwnership(checkList.getNoteId(), userId);
//
//        int result = checkListMapper.insertChecklist(checkList);
//        if (result > 0) {
//            return checkList; // insert 성공 후 ID가 채워진 객체 반환
//        }
//        throw new Exception("체크리스트 추가에 실패했습니다.");
//    }
//
//    @Override
//    public List<CheckList> findChecklistsByNoteId(Integer noteId, int userId) throws Exception { // CheckList로 변경
//        // 노트 소유권 확인 (Mapper에서 조인으로 처리하지만, 서비스 계층에서 한 번 더 확인)
//        checkNoteOwnership(noteId, userId);
//        return checkListMapper.selectChecklistsByNoteId(noteId, userId);
//    }
//
//    @Override
//    public CheckList findChecklistById(Integer checkListId, int userId) throws Exception { // CheckList로 변경
//        // 단건 조회 시 Mapper에서 userId로 소유권 확인을 하므로 여기서는 별도의 checkNoteOwnership 불필요
//        CheckList checkList = checkListMapper.selectChecklistByIdAndUserId(checkListId, userId);
//        if (checkList == null) {
//            throw new IllegalArgumentException("체크리스트를 찾을 수 없거나 권한이 없습니다.");
//        }
//        return checkList;
//    }
//
//    @Override
//    @Transactional
//    public CheckList updateChecklist(CheckList checkList, int userId) throws Exception { // CheckList로 변경
//        // 업데이트 권한 확인은 Mapper에서 조인으로 처리하므로, 여기서는 별도의 checkNoteOwnership 불필요
//        int result = checkListMapper.updateChecklist(checkList, userId); // 업데이트 시 userId 전달
//        if (result > 0) {
//            // 업데이트 후 최신 상태를 반환하기 위해 다시 조회 (선택 사항)
//            return checkListMapper.selectChecklistByIdAndUserId(checkList.getCheckListId(), userId);
//        }
//        throw new Exception("체크리스트 업데이트에 실패했거나 권한이 없습니다.");
//    }
//
//    @Override
//    @Transactional
//    public int deleteChecklist(Integer checkListId, int userId) throws Exception {
//        // 삭제 권한 확인은 Mapper에서 조인으로 처리하므로, 여기서는 별도의 checkNoteOwnership 불필요
//        int result = checkListMapper.deleteChecklist(checkListId, userId);
//        if (result == 0) {
//            throw new Exception("체크리스트 삭제에 실패했거나 권한이 없습니다.");
//        }
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public CheckList toggleChecklistDone(Integer checkListId, int userId) throws Exception { // CheckList로 변경
//        // 토글 권한 확인은 Mapper에서 조인으로 처리하므로, 여기서는 별도의 checkNoteOwnership 불필요
//        CheckList checkList = checkListMapper.selectChecklistByIdAndUserId(checkListId, userId);
//        if (checkList == null) {
//            throw new IllegalArgumentException("체크리스트를 찾을 수 없거나 권한이 없습니다.");
//        }
//
//        checkList.setChecked(!checkList.getChecked()); // getChecked()로 변경
//        int result = checkListMapper.updateChecklist(checkList, userId); // 업데이트 시 userId 전달
//        if (result > 0) {
//            return checkList;
//        }
//        throw new Exception("체크리스트 상태 업데이트에 실패했습니다.");
//    }
//}