package com.weatherthing.project.board.controller;

import com.weatherthing.project.board.dto.BoardDto;
import com.weatherthing.project.board.entity.Board;
import com.weatherthing.project.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : 박수현
 * @version : 1.0.0
 * @package : com.weatherthing.board.controller
 * @name : BoardController
 * @create-date: 2022.12.01
 * @update-date :
 * @update-author : 000
 * @update-description :
 */

@Slf4j
@RestController
@RequestMapping(value = "api/v1/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping
    public List<Board> retrieveTransportation() {
        return boardService.retrieveBoardList();
    }

    @PostMapping
    public Board createTransportation(BoardDto boarddto) {
        return boardService.createBoard(boarddto);
    }
}
