package com.study.springStarter.service;

import com.study.springStarter.dto.CommentDto;
import com.study.springStarter.mapper.BoardMapper;
import com.study.springStarter.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;
    private BoardMapper boardMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, BoardMapper boardMapper) {
        this.commentMapper = commentMapper;
        this.boardMapper = boardMapper;
    }

    @Override
    public int getCount(Integer bno) throws Exception {
        return commentMapper.count(bno);
    }

    @Override
    public int remove(Integer cno, Integer bno, String commenter) throws Exception {
        int res = commentMapper.delete(cno, commenter);
        if(res == 1) {
            res = boardMapper.updateCommentCnt(bno, -1);
        }
        return res;
    }

    @Override
    public int write(CommentDto commentDto) throws Exception {
        int res = commentMapper.insert(commentDto);
        if(res == 1) {
            boardMapper.updateCommentCnt(commentDto.getBno(), 1);
        }
        return res;
    }

    @Override
    public List<CommentDto> getList(Integer bno) throws Exception {
        return commentMapper.selectAll(bno);
    }

    @Override
    public CommentDto read(Integer bno) throws Exception {
        return commentMapper.select(bno);
    }

    @Override
    public int modify(CommentDto commentDto) throws Exception {
        return commentMapper.update(commentDto);
    }
}
