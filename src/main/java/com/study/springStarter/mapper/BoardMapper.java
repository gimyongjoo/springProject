package com.study.springStarter.mapper;

import com.study.springStarter.dto.BoardDto;

import java.util.List;

public interface BoardMapper {

    int count() throws Exception;

    int insert(BoardDto dto) throws Exception;

    int update(BoardDto dto) throws Exception;

    int delete(int bno, String writer) throws Exception;

    BoardDto select(int bno) throws Exception;

    List<BoardDto> selectAll() throws Exception;

    int updateViewCnt(int bno) throws Exception;
}
