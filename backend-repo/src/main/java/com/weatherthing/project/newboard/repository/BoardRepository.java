package com.weatherthing.project.newboard.repository;

import com.weatherthing.project.newboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public class BoardRepository extends JpaRepository<Board, Long> {
}
