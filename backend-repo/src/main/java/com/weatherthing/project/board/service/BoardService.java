package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.BoardDto;
import com.weatherthing.project.board.entity.Board;

import java.util.List;

/**
 * @author : 박수현
 * @version : 1.0.0
 * @package : com.weatherthing.board.service
 * @name : BoardService
 * @create-date: 2022.12.01
 * @update-date :
 * @update-author : 000
 * @update-description :
 */
public interface BoardService {

    public Board createBoard(BoardDto boarddto);

    public List<Board> retrieveBoardList();

}
