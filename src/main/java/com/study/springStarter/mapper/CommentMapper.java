package com.study.springStarter.mapper;

import com.study.springStarter.dto.CommentDto;

import java.util.List;

public interface CommentMapper {

    int insert(CommentDto dto) throws Exception;

    int update(CommentDto dto) throws Exception;

    List<CommentDto> selectAll(Integer bno) throws Exception;

    CommentDto select(Integer bno) throws Exception;

    int delete(Integer cno, String commenter) throws Exception;

    int deleteAll(Integer bno) throws Exception;

    int count(Integer bno) throws Exception;

}
