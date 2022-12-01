package com.weatherthing.project.board.service;


import com.weatherthing.project.board.dto.BoardDto;

import com.weatherthing.project.board.entity.Board;
import com.weatherthing.project.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 박수현
 * @version : 1.0.0
 * @package : com.weatherthing.board.service
 * @name : BoardServiceImpl
 * @create-date: 2022.12.01
 * @update-date :
 * @update-author : 000
 * @update-description :
 */
@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Override
    public Board createBoard(BoardDto boarddto) {
        Board board = boarddto.toEntity();
        boardRepository.save(board);
        return board;
    }

    @Override
    public List<Board> retrieveBoardList() {
        List<Board> list = boardRepository.findAllByOrderByBoardIdDesc();

        return list;
    }

}
