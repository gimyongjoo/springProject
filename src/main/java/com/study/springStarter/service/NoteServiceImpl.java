package com.study.springStarter.service;

import com.study.springStarter.dto.Note;
import com.study.springStarter.util.NoteSearchCondition;
import com.study.springStarter.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteMapper noteMapper;

    @Autowired
    public NoteServiceImpl(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @Override
    @Transactional
    public int addNote(Note note) throws Exception {
        return noteMapper.insertNote(note);
    }

    @Override
    public Note findNoteById(int noteId, int userId) throws Exception {
        // userId를 함께 전달하여 본인 노트만 조회
        Note note = noteMapper.selectNoteById(noteId, userId);
        if (note == null) {
            throw new IllegalArgumentException("노트를 찾을 수 없거나 권한이 없습니다.");
        }
        return note;
    }

    @Override
    public List<Note> findAllNotesByUserId(int userId) throws Exception {
        return noteMapper.selectNotesByUserId(userId);
    }

    @Override
    public List<Note> findNotesByFolderId(int folderId, int userId) throws Exception {
        // userId를 함께 전달하여 본인 폴더의 노트만 조회
        return noteMapper.selectNotesByFolderId(folderId, userId);
    }

    @Override
    @Transactional
    public int updateNote(Note note, int userId) throws Exception {
        // 업데이트 전, 해당 노트의 소유권을 확인
        Note existingNote = noteMapper.selectNoteById(note.getNoteId(), userId);
        if (existingNote == null) {
            throw new IllegalArgumentException("노트 수정 권한이 없습니다.");
        }
        note.setUserId(userId); // DTO에 userId를 설정하여 Mybatis XML에 전달
        return noteMapper.updateNote(note);
    }

    @Override
    @Transactional
    public int deleteNote(int noteId, int userId) throws Exception {
        // 삭제 전, 해당 노트의 소유권을 확인하고 바로 삭제 쿼리 실행
        int result = noteMapper.deleteNote(noteId, userId);
        if (result == 0) {
            throw new IllegalArgumentException("노트 삭제 권한이 없거나 노트를 찾을 수 없습니다.");
        }
        return result;
    }

    @Override
    @Transactional
    public int toggleNotePin(int noteId, int userId) throws Exception {
        // 핀 고정 상태를 토글하기 위해 먼저 노트를 조회
        Note note = noteMapper.selectNoteById(noteId, userId);
        if (note == null) {
            throw new IllegalArgumentException("노트 핀 고정 변경 권한이 없습니다.");
        }
        // isPinned 상태 토글
        note.setIsPinned(!note.getIsPinned());
        // 업데이트 쿼리 실행
        return noteMapper.updateNotePin(note);
    }

    @Override
    public List<Note> searchAndFilterAndSortNotes(int userId, NoteSearchCondition condition) throws Exception {
        // 검색, 필터, 정렬 기능을 수행하는 Mapper 메서드 호출
        return noteMapper.searchAndFilterAndSortNotes(userId, condition);
    }

    @Override
    public List<Note> search(NoteSearchCondition condition) throws Exception {
        return noteMapper.search(condition);
    }

    @Override
    public int count(NoteSearchCondition condition) throws Exception {
        return noteMapper.count(condition);
    }
}