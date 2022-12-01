package com.weatherthing.project.board.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author : 박수현
 * @version : 1.0.0
 * @package : com.weatherthing.board.entity
 * @name : Board
 * @create-date: 2022.12.01
 * @update-date :
 * @update-author : 000
 * @update-description :
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
@Builder
public class Board{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;
}
