package com.study.springStarter.service;

import com.study.springStarter.dto.Note;
import com.study.springStarter.util.NoteSearchCondition;
import java.util.List;

public interface NoteService {

    // 새로운 노트 추가
    int addNote(Note note) throws Exception;

    // 노트 수정 (userId 추가)
    int updateNote(Note note, int userId) throws Exception;

    // 노트 삭제 (userId 추가)
    int deleteNote(int noteId, int userId) throws Exception;

    // 노트 상세 조회 (userId 추가)
    Note findNoteById(int noteId, int userId) throws Exception;

    // 특정 유저의 모든 노트 조회
    List<Note> findAllNotesByUserId(int userId) throws Exception;

    // 특정 폴더의 노트 목록 조회 (userId 추가)
    List<Note> findNotesByFolderId(int folderId, int userId) throws Exception;

    // 노트 핀 고정 상태 토글 (userId 추가)
    int toggleNotePin(int noteId, int userId) throws Exception;

    // 새롭게 추가된 메서드: 검색, 필터, 정렬 기능 포함
    List<Note> searchAndFilterAndSortNotes(int userId, NoteSearchCondition condition) throws Exception;

    List<Note> search(NoteSearchCondition condition) throws Exception;

    int count(NoteSearchCondition condition) throws Exception;
}