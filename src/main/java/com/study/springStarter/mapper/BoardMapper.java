package com.study.springStarter.mapper;

import com.study.springStarter.dto.BoardDto;
import com.study.springStarter.util.SearchCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoardMapper {

    int count() throws Exception;

    int insert(BoardDto dto) throws Exception;

    int update(@Param("dto") BoardDto dto, @Param("writer") String writer) throws Exception;

    int delete(@Param("bno") int bno, @Param("writer") String writer) throws Exception;

    BoardDto select(int bno) throws Exception;

    List<BoardDto> selectAll() throws Exception;

    int updateViewCnt(int bno) throws Exception;

    int updateCommentCnt(Integer bno, Integer commentCnt) throws Exception;

    List<BoardDto> selectPage(Integer offset, Integer pageSize) throws Exception;

    List<BoardDto> searchSelectPage(SearchCondition sc) throws Exception;

    int searchResultCnt(SearchCondition sc) throws Exception;

}
