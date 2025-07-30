package com.study.springStarter.service;

import com.study.springStarter.dto.CheckList;
import com.study.springStarter.mapper.CheckListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckListServiceImpl implements CheckListService {

    @Autowired
    CheckListMapper mapper;

    @Override
    public int addCheckList(CheckList checkList) throws Exception {
        return mapper.insertCheckList(checkList);
    }

    @Override
    public CheckList findCheckListById(int checkListId) throws Exception {
        return mapper.selectCheckListById(checkListId);
    }

    @Override
    public List<CheckList> findCheckListsByNoteId(int noteId) throws Exception {
        return mapper.selectCheckListByNoteId(noteId);
    }

    @Override
    public int updateCheckList(CheckList checkList) throws Exception {
        return mapper.updateCheckList(checkList);
    }

    @Override
    public int deleteCheckList(int checkListId) throws Exception {
        return mapper.deleteCheckList(checkListId);
    }
}
