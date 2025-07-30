package com.study.springStarter.service;

import com.study.springStarter.dto.Note;
import com.study.springStarter.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteMapper mapper;

    @Override
    public int addNote(Note note) throws Exception {
        return mapper.insertNote(note);
    }

    @Override
    public Note findNoteById(int noteId) throws Exception {
        return mapper.selectNoteById(noteId);
    }

    @Override
    public List<Note> getNotesByUserId(int userId) throws Exception {
        return mapper.selectNotesByUserId(userId);
    }

    @Override
    public List<Note> getNotesByFolderId(int folderId) throws Exception {
        return mapper.selectNotesByFolderId(folderId);
    }

    @Override
    public List<Note> getPinnedNotes(int userId) throws Exception {
        return mapper.selectPinnedNotes(userId);
    }

    @Override
    public int updateNote(Note note) throws Exception {
        return mapper.updateNote(note);
    }

    @Override
    public int deleteNote(int noteId) throws Exception {
        return mapper.deleteNote(noteId);
    }

}
