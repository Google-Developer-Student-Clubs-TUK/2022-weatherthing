package com.weatherthing.project.board.controller;

import com.weatherthing.project.board.dto.CommentDto;
import com.weatherthing.project.board.dto.RegisterDto;
import com.weatherthing.project.board.entity.Board;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.BoardRepository;
import com.weatherthing.project.board.repository.UserRepository;
import com.weatherthing.project.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/comments")
    public ResponseEntity writeComment(@RequestBody CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUid()).get();
        Board board = boardRepository.findById(commentDto.getBoardid()).get();
        return ResponseEntity.ok(commentService.writeComment(board.getId(), commentDto, user));
    }

    // 게시글에 달린 댓글 불러오기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/comments/{boardid}")
    public ResponseEntity getComments(@PathVariable(required = false) Long boardid) {
        return ResponseEntity.ok(commentService.getComments(boardid));
    }

}
