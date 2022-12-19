package com.weatherthing.project.board.controller;

import com.weatherthing.project.board.dto.BoardDto;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.UserRepository;
import com.weatherthing.project.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    // 게시글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/boards")
    public ResponseEntity write(@RequestBody BoardDto boardDto) {

        User user = userRepository.findById(boardDto.getUid()).get();
        return ResponseEntity.ok(boardService.write(boardDto, user));
    }

    // 전체 게시글 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public ResponseEntity getBoards() {
        return ResponseEntity.ok(boardService.getBoards());
    }

    // 개별 게시글 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/{id}")
    public ResponseEntity getBoard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(boardService.getBoard(id));
    }



}
