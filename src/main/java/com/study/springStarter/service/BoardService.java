package com.study.springStarter.service;

import com.study.springStarter.dto.BoardDto;

import java.util.List;

public interface BoardService {
    int getCount() throws Exception;

    int write(BoardDto dto) throws Exception;

    int remove(Integer bno, String writer) throws Exception;

    int modify(BoardDto dto) throws Exception;

    BoardDto read(Integer bno) throws Exception;

    List<BoardDto> selectAll() throws Exception;
}
