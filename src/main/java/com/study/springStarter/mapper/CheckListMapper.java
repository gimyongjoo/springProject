package com.study.springStarter.mapper;

import com.study.springStarter.dto.CheckList;

import java.util.List;

public interface CheckListMapper {

    int insertCheckList(CheckList checkList) throws Exception;

    CheckList selectCheckListById(int checkListId) throws Exception;

    List<CheckList> selectCheckListByNoteId(int noteId) throws Exception;

    int updateCheckList(CheckList checkList) throws Exception;

    int deleteCheckList(int checkListId) throws Exception;

}
