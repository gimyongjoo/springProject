package com.study.springStarter.service;

import com.study.springStarter.mapper.BoardMapper;
import com.study.springStarter.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardMapper boardMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public int getCount() throws Exception {
        return boardMapper.count();
    }

    @Override
    public int write(BoardDto dto) throws Exception {
        return boardMapper.insert(dto);
    }

    @Override
    public int remove(Integer bno, String writer) throws Exception {
        return boardMapper.delete(bno, writer);
    }

    @Override
    public int modify(BoardDto dto) throws Exception {
        return boardMapper.update(dto);
    }

    @Override
    public BoardDto read(Integer bno) throws Exception {
        int res = boardMapper.updateViewCnt(bno);
        if(res == 1) {
            return boardMapper.select(bno);
        }
        return null;
    }

    @Override
    public List<BoardDto> selectAll() throws Exception {
        return boardMapper.selectAll();
    }

}
