package com.study.springStarter.service;

import com.study.springStarter.dto.CheckList; // CheckList로 변경
import java.util.List;

public interface CheckListService { // CheckListService로 변경
    CheckList addChecklist(CheckList checkList, int userId) throws Exception; // DTO 타입 및 userId 추가
    List<CheckList> findChecklistsByNoteId(Integer noteId, int userId) throws Exception; // noteId를 Integer로 변경
    CheckList findChecklistById(Integer checkListId, int userId) throws Exception; // checkListId를 Integer로 변경 및 userId 추가
    CheckList updateChecklist(CheckList checkList, int userId) throws Exception; // DTO 타입 및 userId 추가
    int deleteChecklist(Integer checkListId, int userId) throws Exception; // checkListId를 Integer로 변경
    CheckList toggleChecklistDone(Integer checkListId, int userId) throws Exception; // checkListId를 Integer로 변경
}