package com.weatherthing.project.newboard.repository;

import com.weatherthing.project.newboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
