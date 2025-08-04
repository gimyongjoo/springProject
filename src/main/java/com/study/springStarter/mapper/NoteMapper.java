package com.study.springStarter.mapper;

import com.study.springStarter.dto.Note;
import com.study.springStarter.util.NoteSearchCondition; // NoteSearchCondition DTO 사용
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoteMapper {

    // 노트 추가
    int insertNote(Note note) throws Exception;

    // 노트 단건 조회 (userId를 추가하여 보안 강화)
    Note selectNoteById(@Param("noteId") int noteId, @Param("userId") int userId) throws Exception;

    // 유저별 노트 목록 조회
    List<Note> selectNotesByUserId(@Param("userId") int userId) throws Exception;

    // 폴더별 노트 목록 조회 (userId를 추가하여 보안 강화)
    List<Note> selectNotesByFolderId(@Param("folderId") int folderId, @Param("userId") int userId) throws Exception;

    // 핀 고정 노트 목록 조회
    List<Note> selectPinnedNotes(@Param("userId") int userId) throws Exception;

    // 노트 수정 (userId를 추가하여 보안 강화)
    int updateNote(Note note) throws Exception;

    // 노트 삭제 (userId를 추가하여 보안 강화)
    int deleteNote(@Param("noteId") int noteId, @Param("userId") int userId) throws Exception;

    // 노트 핀 고정 토글 상태 변경 (Service Impl에 맞추어 추가)
    int updateNotePin(Note note) throws Exception;

    // 새롭게 추가되는 메서드: 검색, 필터, 정렬 기능
    List<Note> searchAndFilterAndSortNotes(
            @Param("userId") int userId,
            @Param("condition") NoteSearchCondition condition
    );

}