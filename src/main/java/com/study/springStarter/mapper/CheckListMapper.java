//package com.study.springStarter.mapper;
//
//import com.study.springStarter.dto.CheckList; // DTO 이름 변경 (Checklist -> CheckList)
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//
//public interface CheckListMapper { // CheckListMapper로 변경
//    int insertChecklist(CheckList checkList); // CheckList로 변경
//    // noteId와 userId를 함께 받아서 노트 소유자 확인
//    List<CheckList> selectChecklistsByNoteId(@Param("noteId") Integer noteId, @Param("userId") int userId);
//    // 단건 조회 시, checkListId와 userId를 함께 받아서 권한 확인 (노트를 통한 간접 확인)
//    CheckList selectChecklistByIdAndUserId(@Param("checkListId") Integer checkListId, @Param("userId") int userId);
//    // update 시, userId를 함께 받아서 권한 확인
//    int updateChecklist(@Param("checkList") CheckList checkList, @Param("userId") int userId); // @Param 이름도 checkList로 변경
//    // delete 시, userId를 함께 받아서 권한 확인
//    int deleteChecklist(@Param("checkListId") Integer checkListId, @Param("userId") int userId);
//}