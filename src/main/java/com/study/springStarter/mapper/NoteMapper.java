package com.study.springStarter.mapper;

import com.study.springStarter.dto.Note;

import java.util.List;

public interface NoteMapper {

    int insertNote(Note note) throws Exception;

    Note selectNoteById(int noteId) throws Exception;

    List<Note> selectNotesByUserId(int userId) throws Exception;

    List<Note> selectNotesByFolderId(int folderId) throws Exception;

    List<Note> selectPinnedNotes(int userId) throws Exception;

    int updateNote(Note note) throws Exception;

    int deleteNote(int noteId) throws Exception;

}
