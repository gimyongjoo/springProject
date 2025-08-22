package com.study.springStarter.controller;

import com.study.springStarter.dto.CommentDto;
import com.study.springStarter.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 지정된 게시물의 모든 댓글을 가져오는 메서드
    @ResponseBody
    @GetMapping("/board/{bno}/comments")
    public ResponseEntity<List<CommentDto>> list(@PathVariable Integer bno) {
        List<CommentDto> list = null;
        try {
            list = commentService.getList(bno);
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글을 등록하는 메서드
    @ResponseBody
    @PostMapping("/board/{bno}/comments")
    public ResponseEntity<String> write(@RequestBody CommentDto dto, @PathVariable Integer bno, HttpSession session) {
        String commenter = (String) session.getAttribute("email");
        dto.setCommenter(commenter);
        dto.setBno(bno);
        try {
            int cnt = commentService.write(dto);
            if(cnt != 1) {
                throw new Exception("Write Error");
            }
            return new ResponseEntity<String>("WRITE_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("WRITE_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    // 지정된 댓글을 수정하는 메서드
    @ResponseBody
    @PatchMapping("/comments/{cno}")
    public ResponseEntity<String> modify(@PathVariable Integer cno, @RequestBody CommentDto dto, HttpSession session) {
        String commenter = (String) session.getAttribute("email");
        dto.setCommenter(commenter);
        dto.setCno(cno);
        try {
            int cnt = commentService.modify(dto);
            if(cnt != 1) {
                throw new Exception("Modify Error");
            }
            return new ResponseEntity<>("MODIFY_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MODIFY_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    // 지정된 댓글을 삭제하는 메서드
    @ResponseBody
    @DeleteMapping("/board/{bno}/comments/{cno}")
    public ResponseEntity<String> remove(@PathVariable Integer cno, @PathVariable Integer bno, HttpSession session) {
        String commenter = (String) session.getAttribute("email");
        int rowCnt = 0;
        try {
            rowCnt = commentService.remove(cno, bno, commenter);
            if(rowCnt != 1) {
                throw new Exception("Delete Failed");
            }
            return new ResponseEntity<>("DEL_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DEL_ERR", HttpStatus.BAD_REQUEST);
        }
    }
}
