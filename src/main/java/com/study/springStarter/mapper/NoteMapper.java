package com.study.springStarter.mapper;

import com.study.springStarter.dto.Note;

import java.util.List;

public interface NoteMapper {

    int insertNote(Note note) throws Exception; // 노트 추가

    Note selectNoteById(int noteId) throws Exception; // 노트 단건 조회 (ID로)

    List<Note> selectNotesByUserId(int userId) throws Exception; // 유저별 노트 목록 조회

    List<Note> selectNotesByFolderId(int folderId) throws Exception; // 폴더별 노트 목록 조회

    List<Note> selectPinnedNotes(int userId) throws Exception; // 핀 고정 노트 목록 조회

    int updateNote(Note note) throws Exception; // 노트 수정

    int deleteNote(int noteId) throws Exception; // 노트 삭제

}
