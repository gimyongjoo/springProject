package com.study.springStarter.service;

import com.study.springStarter.dto.BoardDto;
import com.study.springStarter.util.SearchCondition;

import java.util.List;

public interface BoardService {
    int getCount() throws Exception;

    int write(BoardDto dto) throws Exception;

    int remove(Integer bno, String writer) throws Exception;

    int modify(BoardDto dto, String writer) throws Exception;

    BoardDto read(Integer bno) throws Exception;

    List<BoardDto> selectAll() throws Exception;

    List<BoardDto> getPage(Integer offset, Integer pageSize) throws Exception;

    int getSearchResultCnt(SearchCondition sc) throws Exception;

    List<BoardDto> getSearchResultPage(SearchCondition sc) throws Exception;
}
