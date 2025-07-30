package com.study.springStarter.service;

import com.study.springStarter.dto.Note;

import java.util.List;

public interface NoteService {

    int addNote(Note note) throws Exception;

    Note findNoteById(int noteId) throws Exception;

    List<Note> getNotesByUserId(int userId) throws Exception;

    List<Note> getNotesByFolderId(int folderId) throws Exception;

    List<Note> getPinnedNotes(int userId) throws Exception;

    int updateNote(Note note) throws Exception;

    int deleteNote(int noteId) throws Exception;

}
