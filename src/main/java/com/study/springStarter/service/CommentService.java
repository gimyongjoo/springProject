package com.study.springStarter.service;

import com.study.springStarter.dto.CommentDto;

import java.util.List;

public interface CommentService {

    int getCount(Integer bno) throws Exception;

    int remove(Integer cno, Integer bno, String commenter) throws Exception;

    int write(CommentDto commentDto) throws Exception;

    List<CommentDto> getList(Integer bno) throws Exception;

    CommentDto read(Integer bno) throws Exception;

    int modify(CommentDto commentDto) throws Exception;

}
