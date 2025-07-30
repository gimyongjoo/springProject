package com.study.springStarter.service;

import com.study.springStarter.dto.CheckList;

import java.util.List;

public interface CheckListService {

    int addCheckList(CheckList checkList) throws Exception;

    CheckList findCheckListById(int checkListId) throws Exception;

    List<CheckList> findCheckListsByNoteId(int noteId) throws Exception;

    int updateCheckList(CheckList checkList) throws Exception;

    int deleteCheckList(int checkListId) throws Exception;

}
