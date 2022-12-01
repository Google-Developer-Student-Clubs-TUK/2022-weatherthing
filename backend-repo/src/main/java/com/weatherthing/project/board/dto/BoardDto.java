package com.weatherthing.project.board.dto;

import com.weatherthing.project.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 박수현
 * @version : 1.0.0
 * @package : com.weatherthing.board.dto
 * @name : BoardDto
 * @create-date: 2022.12.01
 * @update-date :
 * @update-author : 000
 * @update-description :
 */
@Data 
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {

    private String name;
    private String contents;

    public Board toEntity() {
        Board board = Board.builder()
                .name(name)
                .contents(contents)
                .build();
        return board;
    }
}
