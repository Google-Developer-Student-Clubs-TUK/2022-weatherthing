package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.CommentDto;
import com.weatherthing.project.board.entity.Board;
import com.weatherthing.project.board.entity.Comment;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.BoardRepository;
import com.weatherthing.project.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentDto writeComment(Long boardId, CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());

        // 게시판 번호로 게시글 찾기
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시판을 찾을 수 없습니다.");
        });

        comment.setUser(user);
        comment.setBoard(board);
        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }
}
