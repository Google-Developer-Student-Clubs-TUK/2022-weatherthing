package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.BoardMainDto;
import com.weatherthing.project.board.dto.BoardPostDto;
import com.weatherthing.project.board.dto.BoardRequestDto;
import com.weatherthing.project.board.entity.Board;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.BoardRepository;
import com.weatherthing.project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final static int size = 10;

    //게시글 작성
    public BoardPostDto createBoard(BoardRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다.")
        );
        Board board = new Board(requestDto);
        board.addUser(user);
        boardRepository.save(board);
        BoardPostDto boardPostDto = new BoardPostDto(board);
        return boardPostDto;
    }
    // 게시글 조회 페이지네이션 추가 필요 @@
    public List<BoardMainDto> getBoard() {
        List<Board> board = boardRepository.findAllByOrderByUpadatedAtDesc();
        List<BoardMainDto> mainDtoList = new ArrayList<>();
        for(int i=0; i<board.size(); i++){
            BoardMainDto mainDto = new BoardMainDto(board.get(i));
            mainDtoList.add(mainDto);
        }
        return mainDtoList;
    }

}
