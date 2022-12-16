package com.weatherthing.project.board.repository;

import com.weatherthing.project.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByUpadatedAtDesc();


}
