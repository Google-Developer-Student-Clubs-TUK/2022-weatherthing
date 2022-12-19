package com.weatherthing.project.board.repository;

import com.weatherthing.project.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
