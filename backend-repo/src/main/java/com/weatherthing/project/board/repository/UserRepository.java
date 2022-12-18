package com.weatherthing.project.board.repository;

import com.weatherthing.project.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
